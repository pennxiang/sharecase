package com.sharedcase.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharedcase.entity.CaseVersion;

import java.util.List;

/**
 * ClassName: CaseVersionMapper
 * Package: com.sharedcase.dao
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:19
 */
public interface CaseVersionMapper extends BaseMapper<CaseVersion> {
    List<CaseVersion> selectVersionByCaseId(Integer id);
}
