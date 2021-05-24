package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.domain.db.QuestionBank;
import org.apache.ibatis.annotations.Mapper;

/**
 *  题目
 */
@Mapper
public interface QuestionSoulMapper extends BaseMapper<QuestionBank> {
}
