package com.sharedcase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sharedcase.entity.CaseDetail;

import java.util.List;

/**
 * ClassName: CaseDetailService
 * Package: com.sharedcase.service
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:20
 */
public interface CaseService extends IService<CaseDetail> {
    List<CaseDetail> getAllByUserId(Long userId);
}
