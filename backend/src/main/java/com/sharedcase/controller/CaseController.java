package com.sharedcase.controller;

import com.sharedcase.entity.*;
import com.sharedcase.service.CaseContractService;
import com.sharedcase.service.CaseService;
import com.sharedcase.service.CaseVersionService;
import com.sharedcase.service.UserService;
import com.sharedcase.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: CaseController
 * Package: com.sharedcase.controller
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:21
 */
@Tag(name = "病例管理", description = "病例详情")
@RestController
@RequestMapping("/case")
@RequiredArgsConstructor
public class CaseController {

    @Autowired
    private CaseService caseService;

    @Autowired
    private CaseContractService caseContractService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private CaseVersionService caseVersionService;

    /**
     * 医生创建病例
     * @param caseDetail
     * @return
     */
    @Operation(summary = "医生创建原始病例")
    @PostMapping("/create")
    public AjaxResult create(@RequestBody CaseDetail caseDetail) {
        // TODO
        // 添加版本号到fisco  链上
        return AjaxResult.success(caseService.createCase(caseDetail));
    }

    /**
     * 医生查看当前病人相关的病例
     */
    @Operation(summary = "医生查看某病人指定 ICD 的病例详情")
    @GetMapping("/detail/{id}")
    public AjaxResult getCaseDetail(@PathVariable Long id,
                                    @RequestParam String icd) {
        try {
            // 获取链上病例版本（根据 userId + icdCode）查链上最新版本
            String idCard = userServiceImpl.getById(id).getIdCard();
            String ipfsHash = caseContractService.getCaseVersionHash(idCard, icd); // 示例：合约查询返回 CID
            if (ipfsHash == null || ipfsHash.isBlank()) {
                return AjaxResult.error("未找到对应的病例版本");
            }

            // 2. 从 IPFS 获取并解析 PDF -> CaseDetail 对象
            CaseDetail detail = caseService.getCaseByHash(ipfsHash);
            if (detail == null) {
                return AjaxResult.error("IPFS 上未找到对应病例");
            }

            return AjaxResult.success(detail);

        } catch (Exception e) {
            return AjaxResult.error("获取病例失败: " + e.getMessage());
        }
    }

    /**
     * 用户查看病例
     * @param caseId
     * @return
     */
    @Operation(summary = "用户查看自己的病例详情")
    @GetMapping("/view")
    public AjaxResult viewCase(@RequestParam String caseId) {
        try {
            // 1. 获取该病例的最新版本哈希（从链上或数据库查）
            String ipfsHash = caseContractService.getLatestVersionHash(caseId);
            if (ipfsHash == null) {
                return AjaxResult.error("未找到该病例的最新版本");
            }

            // 2. 从 IPFS 下载并解析
            CaseDetail detail = caseService.getCaseByHash(ipfsHash);
            if (detail == null) {
                return AjaxResult.error("病例解析失败");
            }

            return AjaxResult.success(detail);

        } catch (Exception e) {
            return AjaxResult.error("获取病例失败: " + e.getMessage());
        }
    }

    /**
     * 用户查看历史版本
     */
    @Operation(summary = "用户查看病例历史版本列表")
    @GetMapping("/history")
    public AjaxResult viewHistory(@RequestParam String caseId) {
        try {
            // 1. 查询链上版本列表（返回 List<CaseVersionMeta>）
            List<CaseVersion> versions = caseContractService.getAllVersions(caseId);
            if (versions == null || versions.isEmpty()) {
                return AjaxResult.error("没有找到任何历史版本");
            }

            return AjaxResult.success(versions);

        } catch (Exception e) {
            return AjaxResult.error("获取历史版本失败: " + e.getMessage());
        }
    }


    /**
     * 医保局/医院查看疾病排行
     * @param start 时间段开始
     * @param end 时间段结束
     * @param asc 是否升序
     * @return
     */
    @Operation(summary = "医保局/医院查看疾病排行")
    @GetMapping("/disease-stats")
    public AjaxResult getDiseaseStats(@RequestParam String start,
                                      @RequestParam String end,
                                      @RequestParam(defaultValue = "false") boolean asc) {
        List<HotDiseaseDTO> result = null;
        try {
            result = caseVersionService.getDiseaseStatsByDate(start, end, asc);
            return AjaxResult.success(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
