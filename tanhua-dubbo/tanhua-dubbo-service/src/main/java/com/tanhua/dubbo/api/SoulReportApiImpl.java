package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.domain.db.SoulReport;
import com.tanhua.dubbo.mapper.SoulReportMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.dubbo.mapper.SoulReportMapper;
import lombok.extern.slf4j.Slf4j;
import com.tanhua.domain.db.SoulReport;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *  报告表
 */
@Service
@Slf4j
public class SoulReportApiImpl implements SoulReportApi {
    @Autowired
    private SoulReportMapper soulReportMapper;
    /**
     * 根据用户id查询报告表
     * @param id
     * @return
     */

    @Autowired
    SoulReportMapper soulReportMapper;
    /**
     * 查询是该用户是否有报告生成
     *
     * @param userId
     * @return
     */
    @Override
    public List<SoulReport> getReportMap(Long userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        List result = soulReportMapper.selectList(queryWrapper);
        log.info("查询是该用户是否有报告生成{}",result);
        return result;
    }
    @Override
    public List<SoulReport> findByList(Long id) {
        QueryWrapper<SoulReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",id);
       return soulReportMapper.selectList(queryWrapper);
    }

    /**
     * 解锁用户等级填充数据
     *
     * @param soulcomment
     */
    @Override
    public void insert(SoulReport soulcomment) {
        int insert = soulReportMapper.insert(soulcomment);


    }
}
    /**
     * 查询编号
     *
     * @param reportId
     * @return
     */
    @Override
    public SoulReport queryReport(Integer reportId) {
        return soulReportMapper.selectById(reportId);
    }

    /**
     * 查询分数相同的人
     *
     * @param minScore
     * @param maxScore
     * @return
     */
    @Override
    public List<Long> similar(Integer minScore, Integer maxScore) {
        return soulReportMapper.querySimilar(minScore, maxScore);
    }
}