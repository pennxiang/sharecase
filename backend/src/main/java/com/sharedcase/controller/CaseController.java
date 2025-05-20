package com.sharedcase.controller;

import com.sharedcase.entity.AjaxResult;
import com.sharedcase.entity.CaseDetail;
import com.sharedcase.service.CaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "创建一个新的病例")
    @PostMapping("/create")
    public AjaxResult create(@RequestBody CaseDetail caseDetail) {
        caseDetail.setCreateTime(LocalDateTime.now());
        caseDetail.setUpdateTime(LocalDateTime.now());
        caseService.save(caseDetail);
        return AjaxResult.success("创建成功");
    }

    @Operation(summary = "更新数据库中病例信息")
    @PostMapping("/update")
    public AjaxResult update(@RequestBody CaseDetail caseDetail) {
        caseDetail.setUpdateTime(LocalDateTime.now());
        caseService.updateById(caseDetail);
        return AjaxResult.success("更新成功");
    }

    @Operation(summary = "根据用户id获取病例")
    @GetMapping("/list")
    public AjaxResult listByUser(@RequestParam Long userId) {
        return AjaxResult.success(caseService.getAllByUserId(userId));
    }
}
