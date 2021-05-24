package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.domain.db.Soulcomment;
import com.tanhua.dubbo.mapper.SoulReportMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 报告表
 */
@Service
@Slf4j
public class SoulReportApiImpl implements SoulReportApi {

    @Autowired
    SoulReportMapper soulReportMapper;
    /**
     * 查询是该用户是否有报告生成
     *
     * @param userId
     * @return
     */
    @Override
    public List<Soulcomment> getReportMap(Long userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        List result = soulReportMapper.selectList(queryWrapper);
        log.info("查询是该用户是否有报告生成{}",result);
        return result;
    }
}
