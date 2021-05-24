package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.domain.db.Questionoptions;
import com.tanhua.dubbo.mapper.QuestionOptionsMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  选项表
 */
@Service
public class QuestionoptionsApiImpl implements QuestionoptionsApi {
    @Autowired
    private QuestionOptionsMapper questionOptionsMapper;
    /**
     * 根据题目和选项id查询选项
     *
     * @param id
     * @param optionId
     * @return
     */
    @Override
    public Questionoptions findById(Integer id, String optionId) {
        QueryWrapper<Questionoptions> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionId",id).eq("optionsId",optionId);
        return questionOptionsMapper.selectOne(queryWrapper);
    }
}
