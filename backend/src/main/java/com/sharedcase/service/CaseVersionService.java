package com.sharedcase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sharedcase.entity.CaseVersion;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * ClassName: CaseVersionService
 * Package: com.sharedcase.service
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:20
 */
public interface CaseVersionService extends IService<CaseVersion> {
    List<CaseVersion> getAllVersions(String caseId);
    CaseVersion uploadVersion(String caseId, String versionCode, File file, String summary, Long uploaderId) throws Exception;
}
