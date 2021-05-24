package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.domain.db.Questionlist;
import com.tanhua.dubbo.mapper.QuestionlistMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @description
 * @author:Dell
 * @date:2021/5/23 11:16
 */
@Service
@Slf4j
public class QuestionlistApiImpl implements QuestionlistApi {
    @Autowired
    QuestionlistMapper questionlistMapper;

    @Override
    public List<Questionlist> findAllTable() {
        //查询数据灵魂问卷表
        QueryWrapper<Questionlist> query = new QueryWrapper<>();
        List<Questionlist> result = questionlistMapper.selectList(query);
        log.info("灵魂问卷表返回数据{}", result);
        return result;
    }
}
