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

    @GetMapping("/list/{caseId}")
    public AjaxResult getVersions(@PathVariable String caseId) {
        List<CaseVersion> versions = caseVersionService.getAllVersions(caseId);
        return AjaxResult.success("获取成功", versions);
    }

    @PostMapping("/upload")
    public AjaxResult uploadVersion(
            @RequestParam String caseId,
            @RequestParam MultipartFile file,
            @RequestParam String summary,
            @RequestParam Long uploaderId
    ) {
        try {
            // 写入临时文件以便 IPFS 读取
            File tempFile = File.createTempFile("upload_", "_" + file.getOriginalFilename());
            file.transferTo(tempFile);
            String versionCode = "0xtest";
            CaseVersion version = caseVersionService.uploadVersion(caseId, versionCode, tempFile, summary, uploaderId);
            return AjaxResult.success("版本上传成功", version);
        } catch (Exception e) {
            return AjaxResult.error("上传失败: " + e.getMessage());
        }
    }
}