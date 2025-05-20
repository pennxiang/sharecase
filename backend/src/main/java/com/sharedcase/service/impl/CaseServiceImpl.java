package com.sharedcase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharedcase.dao.CaseDetailMapper;
import com.sharedcase.entity.CaseDetail;
import com.sharedcase.service.CaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: CaseDetailServiceImpl
 * Package: com.sharedcase.service.impl
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:25
 */
@Service
public class CaseServiceImpl extends ServiceImpl<CaseDetailMapper, CaseDetail> implements CaseService {
    @Override
    public List<CaseDetail> getAllByUserId(Long userId) {
        return lambdaQuery().eq(CaseDetail::getUserId, userId).list();
    }
}
