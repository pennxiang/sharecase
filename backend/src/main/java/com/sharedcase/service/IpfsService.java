package com.sharedcase.service;

import com.sharedcase.config.IpfsConfig;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

/**
 * ClassName: IpfsService
 * Package: com.sharedcase.service
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:22
 */
@Service
public class IpfsService {

    private static final Logger logger = LoggerFactory.getLogger(IpfsService.class);

    private final IPFS ipfs;

    public IpfsService(IpfsConfig props) {
        this.ipfs = new IPFS(props.getMultiAddr());
        logger.info("IPFS 连接成功: {}", props.getMultiAddr());
    }

    public String upload(File file, String dir) throws Exception {
        NamedStreamable.FileWrapper fileWrapper = new NamedStreamable.FileWrapper(file);
        MerkleNode node = ipfs.add(fileWrapper).get(0);
        Multihash hash = node.hash;

        ipfs.pin.add(hash);
        ipfs.files.mkdir(dir, true);
        ipfs.files.cp("/ipfs/" + hash.toBase58(), dir + "/" + file.getName(), true);

        //上传成功后，尝试删除本地文件
        if (file.exists()) {
            if (file.delete()) {
                logger.info("临时文件已删除: {}", file.getAbsolutePath());
            } else {
                logger.warn("临时文件删除失败: {}", file.getAbsolutePath());
            }
        }

        return hash.toBase58();
    }

    /**
     * 获取文件
     * @param ipfsHash ipfsHash
     * @return file
     */
    public File download(String ipfsHash) throws Exception {
        try {
            Multihash filePointer = Multihash.fromBase58(ipfsHash);
            byte[] data = ipfs.cat(filePointer);

            // 临时目录保存，后缀为 .pdf
            File tempFile = Files.createTempFile("ipfs_" + ipfsHash.substring(0, 6), ".pdf").toFile();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                out.write(data);
                out.flush();
            }

            logger.info("IPFS 文件下载成功: {} -> {}", ipfsHash, tempFile.getAbsolutePath());
            return tempFile;

        } catch (Exception e) {
            logger.error("IPFS 下载失败: {}", ipfsHash, e);
            throw new RuntimeException("IPFS 下载失败: " + ipfsHash, e);
        }
    }
}
