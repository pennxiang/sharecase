package com.sharedcase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sharedcase.entity.CaseVersion;
import com.sharedcase.entity.HotDiseaseDTO;
import com.sharedcase.entity.User;

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
    /**
     * 获取某个病例的总数
     * @param icdCode
     * @return
     */
    Long getTotal(String icdCode);

    /**
     * 获取某个病人的历史版本
     * @param user
     * @return
     */
    List<CaseVersion> getAllVersions(User user);

    /**
     * 获取某个病例的版本列表
     * @param caseId
     * @return
     */



    public List<HotDiseaseDTO> getDiseaseStatsByDate(String startDate, String endDate, boolean ascending) throws Exception;

    public CaseVersion uploadVersion(String caseId, String versionCode, File file, String summary, Long uploaderId) throws Exception;

}
