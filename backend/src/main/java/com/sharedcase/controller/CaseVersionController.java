package com.sharedcase.controller;

/**
 * ClassName: CaseVsersionController
 * Package: com.sharedcase.controller
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:21
 */

import com.sharedcase.entity.AjaxResult;
import com.sharedcase.entity.CaseVersion;
import com.sharedcase.service.CaseVersionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Tag(name = "病例版本", description = "病例操作")
@RestController
@RequestMapping("/case-version")
public class CaseVersionController {

    @Autowired
    private CaseVersionService caseVersionService;

//    @GetMapping("/list/{caseId}")
//    public AjaxResult getVersions(@PathVariable String caseId) {
//        List<CaseVersion> versions = caseVersionService.getAllVersions(caseId);
//        return AjaxResult.success("获取成功", versions);
//    }

    @PostMapping("/upload")
    public AjaxResult uploadVersion(
            @RequestParam String caseId,
            @RequestParam MultipartFile file,
            @RequestParam String summary,
            @RequestParam Long uploaderId
    ) {
        try {
            // 上传至ipfs，返回哈希函数

            // 将ipfs得到的哈希函数，存储到fisco，返回哈希函数

            // 存储fisco返回的哈希函数到用户表

            // 返回结果，中间任有未完成的回滚


            // 写入临时文件以便 IPFS 读取
            File tempFile = File.createTempFile("upload_", "_" + file.getOriginalFilename());
            file.transferTo(tempFile);
            // 先查当前用户是否有xx：的版本
            String versionCode = "0xtest";
            CaseVersion version = caseVersionService.uploadVersion(caseId, versionCode, tempFile, summary, uploaderId);
            return AjaxResult.success("版本上传成功", version);
        } catch (Exception e) {
            return AjaxResult.error("上传失败: " + e.getMessage());
        }
    }

    // TODO
    @PostMapping("/download")
    public AjaxResult downloadVersion() {

        // 传入fisco哈希函数、caseId，fisco进行认证，返回ipfs哈希

        // 根据ipfs哈希，用户希望保存的地址找到资源下载

        return AjaxResult.success("下载成功");
    }

    // TODO
    /**
     * 统计某个时间段的Top k发病率的病症
     */
}