package com.sharedcase.util;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * ClassName: IpfsUtil
 * Package: com.sharedcase.util
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 11:18
 */
public class IpfsUtil {

    private static final Logger logger = LoggerFactory.getLogger(IpfsUtil.class);

    @Value("${ipfs.multiAddr}")
    private String ipfsMultiAddr;

    @Value("${ipfs.gateway}")
    private String ipfsGateway;

    private IPFS ipfs;

    @PostConstruct
    public void init() {
        ipfs = new IPFS(ipfsMultiAddr);
        logger.info("IPFS连接成功!");
    }

    /**
     * 上传文件到ipfs
     */
    public String upload(File file, String DirectoryName) throws Exception {
        String fileName = file.getName();

        // 上传到ipfs
        NamedStreamable.FileWrapper fileWrapper = new NamedStreamable.FileWrapper(file);
        MerkleNode node = ipfs.add(fileWrapper).get(0);
        Multihash hash = node.hash;
        String cid = hash.toBase58();

        // 防止被gc
        ipfs.pin.add(hash);

        // 显示在ui或者桌面界面
        String mfsPath = DirectoryName + "/" + fileName;
        ipfs.files.cp("/ipfs/" + cid, mfsPath, true);

        logger.info("上传成功, CID: {}", cid);
        return cid;
    }

    /**
     * 下载文件（以 byte[] 返回）
     */
    public File download(String cid, String mfsPath, String saveDir) throws IOException {
        // 1. 校验参数
        if (cid == null || mfsPath == null || saveDir == null) {
            throw new IllegalArgumentException("CID、MFS路径和保存目录不能为空");
        }

        // 2. 提取文件名
        String filename = mfsPath.substring(mfsPath.lastIndexOf("/") + 1);

        // 3. 构造本地保存目录并确保存在
        File directory = new File(saveDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("创建目录失败: " + saveDir);
            }
        }

        // 4. 下载文件内容
        Multihash hash = Multihash.fromBase58(cid);
        byte[] fileBytes = ipfs.cat(hash);

        // 5. 构造保存路径
        File targetFile = new File(directory, filename);
        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            fos.write(fileBytes);
        }

        logger.info("下载成功，CID: {}, 保存路径: {}", cid, targetFile.getAbsolutePath());
        return targetFile;
    }


    /**
     * 删除文件（取消 pin + 回收 GC）
     */
    public void delete(String cid, String mfsPath) throws IOException {
        if (mfsPath != null && !mfsPath.isEmpty()) {
            ipfs.files.rm(mfsPath, true, true);
        }
        Multihash hash = Multihash.fromBase58(cid);
        try {
            ipfs.pin.rm(hash);
            ipfs.repo.gc();
            logger.info("已删除（取消 pin 并回收 GC）: {}", cid);
        } catch (Exception e) {
            logger.error("删除失败，CID: {}, 原因: {}", cid, e.getMessage());
            throw e;
        }
    }

    /**
     * 获取公共访问地址
     */
    public String getGatewayUrl(String cid) {
        return ipfsGateway + cid;
    }
}
