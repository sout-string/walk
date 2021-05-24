package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.domain.db.QuestionBank;
import org.apache.ibatis.annotations.Mapper;

/**
 *  题目表
 */


public interface QuestionSoulApi  {
    /**
     *  根据题目id查询题目表
     * @param questionId
     * @return
     */
    QuestionBank findById(String questionId);
}
