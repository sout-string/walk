package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.domain.db.Soulcomment;
import org.apache.ibatis.annotations.Mapper;

/**
 *  报告
 */
@Mapper
public interface SoulReportMapper extends BaseMapper<Soulcomment> {
}