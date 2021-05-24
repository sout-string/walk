package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.domain.db.Questionoptions;
import org.apache.ibatis.annotations.Mapper;

/**
 *  选项表
 */

public interface QuestionoptionsApi  {
    /**
     * 根据题目和选项id查询选项
     * @param id
     * @param optionId
     * @return
     */
    Questionoptions findById(Integer id, String optionId);
}
