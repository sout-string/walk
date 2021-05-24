package com.tanhua.dubbo.api;

import com.tanhua.domain.db.SoulReport;
import com.tanhua.dubbo.mapper.SoulReportMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *  报告表
 */
@Service
public class SoulReportApiImpl implements SoulReportApi {

    @Autowired
    private SoulReportMapper soulReportMapper;

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