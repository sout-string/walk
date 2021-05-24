package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.domain.db.SoulReport;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 *  报告
 */
@Mapper
public interface SoulReportMapper extends BaseMapper<SoulReport> {

    @Select("select user_id from tb_soul_report where score between #{minScore} and #{maxScore}")
    List<Long> querySimilar(@Param("minScore") Integer minScore, @Param("maxScore") Integer maxScore);
}
