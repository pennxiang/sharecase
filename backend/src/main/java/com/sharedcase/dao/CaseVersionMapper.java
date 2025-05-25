/*
package com.sharedcase.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

*/
/**
 * ClassName: CaseVersionMapper
 * Package: com.sharedcase.dao
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:19
 *//*


public interface CaseVersionMapper extends BaseMapper<CaseVersion> {
    List<CaseVersion> selectVersionByCaseId(Integer id);

    @Select("SELECT * FROM case_version WHERE case_id = #{caseId} ORDER BY create_time DESC LIMIT 1")
    CaseVersion selectLatestByCaseId(@Param("caseId") String caseId);
}
*/
