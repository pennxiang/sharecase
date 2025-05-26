package com.sharedcase.controller;

import cn.hutool.core.util.IdUtil;
import com.CaseContract;
import com.sharedcase.DTO.CaseInfo;
import com.sharedcase.DTO.IcdFrequency;
import com.sharedcase.entity.*;
import com.sharedcase.service.CaseService;
import com.sharedcase.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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


    /**
     * 创建病例 【医生端】
     * @param caseDetail 病例详情
     * @return
     */
    @Operation(summary = "医生创建病例")
    @PostMapping("/create")
    public AjaxResult create(@RequestBody CaseDetail caseDetail) {
        try {
            // 设置caseId（使用雪花算法）
            caseDetail.setCaseId(IdUtil.getSnowflake().nextIdStr());
            // 调用服务层方法创建病例
            caseService.createCase(caseDetail);
            // 返回成功结果
            return AjaxResult.success("病例创建成功");
        } catch (Exception e) {
            // 捕获异常并返回错误结果
            return AjaxResult.error("创建失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取平台所有病例信息")
    @GetMapping("/listAll")
    public AjaxResult listAll() {
        try {
            // 调用服务层方法获取所有病例信息
            List<CaseDetail> cases = caseService.listAll();
            // 返回成功结果和病例信息列表
            return AjaxResult.success(cases);
        } catch (Exception e) {
            // 如果查询过程中发生异常，返回错误结果和异常信息
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取某人的历史病例信息
     * 通过患者的地址来查询该患者的所有病例记录
     *
     * @param patientAddress 患者的地址，用于标识患者并查询其病例信息
     * @return 返回一个AjaxResult对象，其中包含查询到的病例信息列表或错误信息
     */
    @Operation(summary = "某人的历史病例")
    @GetMapping("/list")
    public AjaxResult getCasesByPatient(@RequestParam String patientAddress) {
        try {
            // 调用服务层方法，根据患者地址获取病例信息列表
            List<CaseInfo> cases = caseService.getCasesByPatient(patientAddress);
            // 如果查询成功，返回成功结果和病例信息列表
            return AjaxResult.success(cases);
        } catch (Exception e) {
            // 如果查询过程中发生异常，返回错误结果和异常信息
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 按ICD查询病例
     *
     * 该接口用于根据患者的地址和ICD代码查询相关的病例信息
     * 主要目的是为了方便用户根据疾病分类（ICD代码）获取特定患者的医疗记录
     *
     * @param patientAddress 患者地址，用于定位患者信息
     * @param icdCode ICD代码，用于指定疾病分类
     * @return 返回查询结果，包括所有匹配的病例信息如果查询失败，返回错误信息
     */
    @Operation(summary = "按ICD查询病例")
    @GetMapping("/filter")
    public AjaxResult getCasesByIcd(@RequestParam String patientAddress, @RequestParam String icdCode) {
        try {
            // 调用服务层方法，根据患者地址和ICD代码查询病例信息
            List<CaseInfo> cases = caseService.getCasesByIcd(patientAddress, icdCode);
            // 如果查询成功，返回成功结果和病例信息
            return AjaxResult.success(cases);
        } catch (Exception e) {
            // 如果查询过程中发生异常，返回错误结果和错误信息
            return AjaxResult.error("按ICD查询失败: " + e.getMessage());
        }
    }

    /**
     * 统计某人某段时间内的所有病例数
     *
     * @param patientAddress 患者地址，用于识别特定患者
     * @param from 开始时间，统计病例的起始时间
     * @param to 结束时间，统计病例的截止时间
     * @return 返回一个AjaxResult对象，包含统计结果或错误信息
     */
    @Operation(summary = "统计某人某段时间类的所有病例数")
    @GetMapping("/patient/cases/count")
    public AjaxResult getIcdStats(@RequestParam String patientAddress,
                                  @RequestParam String from,
                                  @RequestParam String to) {
        try {
            // 解析开始时间和结束时间，确保时间格式正确
            LocalDateTime fromTime = LocalDateTime.parse(from);
            LocalDateTime toTime = LocalDateTime.parse(to);

            // 调用服务层方法，根据患者地址和时间范围获取病例统计结果
            Map<String, Long> stats = caseService.getIcdStats(patientAddress, fromTime, toTime);

            // 返回成功结果，包含统计信息
            return AjaxResult.success(stats);
        } catch (Exception e) {
            // 捕获异常，返回错误结果，包含异常信息
            return AjaxResult.error("统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取某人在某段时间内的所有病例
     *
     * @param address 患者的地址，用于识别特定的患者
     * @param from 查询的起始时间，格式为字符串，需要转换为LocalDateTime
     * @param to 查询的结束时间，格式为字符串，需要转换为LocalDateTime
     * @return 返回包含患者在指定时间范围内所有病例的列表如果查询成功，否则返回错误信息
     */
    @Operation(summary = "某人的某段时间类的所有病例")
    @GetMapping("/patient/cases/list")
    public AjaxResult getCasesByPatientAndTime(@RequestParam String address,
                                               @RequestParam String from,
                                               @RequestParam String to) {
        try {
            // 将字符串形式的起始时间转换为LocalDateTime对象
            LocalDateTime start = LocalDateTime.parse(from);
            // 将字符串形式的结束时间转换为LocalDateTime对象
            LocalDateTime end = LocalDateTime.parse(to);
            // 调用服务层方法，根据患者地址和时间范围获取病例列表
            List<CaseDetail> list = caseService.getCasesByPatientAndTimeRange(address, start, end);
            // 返回成功结果，包含病例列表
            return AjaxResult.success(list);
        } catch (Exception e) {
            // 捕获异常，返回错误结果，包含异常信息
            return AjaxResult.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取全平台ICD频率统计信息
     * 此接口用于获取平台上所有ICD代码的使用频率统计，可以根据频率进行升序或降序排列
     *
     * @param desc 是否降序排序的标志，默认为true表示降序，false表示升序
     *             这里使用@RequestParam注解来获取请求参数，并设置默认值为true
     * @return 返回一个AjaxResult对象，包含执行结果
     *         如果操作成功，将返回成功信息和ICD频率统计列表
     *         如果操作失败，将返回错误信息
     */
    @Operation(summary = "全平台 ICD 频率统计")
    @GetMapping("/icd-frequency")
    public AjaxResult getICDFrequency(@RequestParam(defaultValue = "true") boolean desc) {
        try {
            // 调用服务层方法获取排序后的ICD频率统计列表
            List<IcdFrequency> list = caseService.getICDFrequencySorted(desc);
            // 如果操作成功，返回成功信息和统计列表
            return AjaxResult.success(list);
        } catch (Exception e) {
            // 如果操作失败，捕获异常并返回错误信息
            return AjaxResult.error("统计失败: " + e.getMessage());
        }
    }

    /**
     * 筛选某时间段的病例
     *
     * 该接口用于根据指定的时间段筛选病例信息
     * 它通过接收起始时间和结束时间作为参数，返回该时间段内的所有病例详情
     *
     * @param from 起始时间字符串，格式为"yyyy-MM-dd'T'HH:mm:ss"
     * @param to 结束时间字符串，格式为"yyyy-MM-dd'T'HH:mm:ss"
     * @return 返回一个AjaxResult对象，包含筛选结果或错误信息
     */
    @Operation(summary = "筛选某时间段的病例")
    @GetMapping("/filterByTime")
    public AjaxResult filterByTime(@RequestParam String from,
                                   @RequestParam String to) {
        try {
            // 将字符串参数转换为LocalDateTime对象
            LocalDateTime fromTime = LocalDateTime.parse(from);
            LocalDateTime toTime = LocalDateTime.parse(to);

            // 调用服务层方法，获取指定时间段内的病例详情列表
            List<CaseDetail> cases = caseService.getCasesByTimeRange(fromTime, toTime);

            // 返回成功结果，包含病例详情列表
            return AjaxResult.success(cases);
        } catch (Exception e) {
            // 捕获异常，返回错误结果，包含异常信息
            return AjaxResult.error("筛选失败: " + e.getMessage());
        }
    }

    /**
     * 筛选某时间段的病发率
     * 该接口用于获取指定时间范围内的疾病统计信息
     *
     * @param from 开始时间，格式为yyyy-MM-dd'T'HH:mm:ss
     * @param to 结束时间，格式为yyyy-MM-dd'T'HH:mm:ss
     * @return 返回包含疾病统计信息的AjaxResult对象
     *         如果统计成功，返回成功结果和统计信息
     *         如果统计失败，返回错误信息
     */
    @Operation(summary = "筛选某时间段的病发率")
    @GetMapping("/cases/range/icd-stat")
    public AjaxResult getIcdStats(@RequestParam String from,
                                  @RequestParam String to) {
        try {
            // 解析开始时间和结束时间
            LocalDateTime start = LocalDateTime.parse(from);
            LocalDateTime end = LocalDateTime.parse(to);

            // 调用服务层方法，获取指定时间范围内的疾病统计信息
            Map<String, Long> stat = caseService.getIcdStatsByTimeRange(start, end);

            // 返回成功结果和统计信息
            return AjaxResult.success(stat);
        } catch (Exception e) {
            // 如果发生异常，返回错误信息
            return AjaxResult.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 模糊搜索病例信息
     *
     * @param keyword 搜索关键词
     * @return 匹配的病例列表
     */
    @Operation(summary = "模糊搜索病例")
    @GetMapping("/search")
    public AjaxResult searchCases(@RequestParam String keyword) {
        try {
            // 调用服务层方法，进行模糊搜索
            List<CaseDetail> matched = caseService.searchCasesFuzzy(keyword);
            // 返回成功结果
            return AjaxResult.success(matched);
        } catch (Exception e) {
            // 捕获异常，返回错误信息
            return AjaxResult.error("模糊搜索失败: " + e.getMessage());
        }
    }

    /**
     * 比较两个病例的 PDF 差异
     * 此方法通过病例的哈希值识别病例，然后比较它们的差异
     * 使用 GET 请求映射到 "/compare" 路径
     *
     * @param hash1 第一个病例的哈希值，用于唯一标识一个病例
     * @param hash2 第二个病例的哈希值，用于唯一标识另一个病例
     * @return 返回一个 AjaxResult 对象，包含成功或错误的响应
     *         如果比较成功，返回成功结果，包含差异信息
     *         如果发生异常，返回错误结果，包含错误信息
     */
    @Operation(summary = "比较两个病例的 PDF 差异")
    @GetMapping("/compare")
    public AjaxResult compareCases(@RequestParam String hash1, @RequestParam String hash2) {
        try {
            // 调用服务层方法比较病例差异
            Map<String, List<String>> diff = caseService.compareCases(hash1, hash2);
            // 返回成功结果，包含差异信息
            return AjaxResult.success(diff);
        } catch (Exception e) {
            // 如果发生异常，返回错误结果，包含错误信息
            return AjaxResult.error("对比失败: " + e.getMessage());
        }
    }
}
