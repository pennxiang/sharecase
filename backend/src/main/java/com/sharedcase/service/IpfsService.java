package com.sharedcase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.io.File;

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

    /**
     * 上传文件
     * @param file 文件
     * @param DirectoryName 文件目录
     * @return ipfsHash
     */
    String upload(File file, String DirectoryName) throws Exception {
        // TODO
        return null;
    };


    /**
     * 获取文件
     * @param ipfsHash ipfsHash
     * @return file
     */
    File download(String ipfsHash) throws Exception {
        // TODO
        return null;
    };
}
