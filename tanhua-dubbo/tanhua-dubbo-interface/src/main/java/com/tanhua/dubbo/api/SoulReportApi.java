package com.tanhua.dubbo.api;

import com.tanhua.domain.db.SoulReport;

import java.util.List;

public interface SoulReportApi {
    /**
     * 查询编号
     * @param reportId
     * @return
     */
    SoulReport queryReport(Integer reportId);

    /**
     * 查询分数相同的人
     * @param minScore
     * @param maxScore
     * @return
     */
    List<Long> similar(Integer minScore, Integer maxScore);
}
