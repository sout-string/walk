package com.tanhua.dubbo.api;

import com.tanhua.domain.db.SoulReport;

import java.util.List;

import com.tanhua.domain.db.SoulReport;

import java.util.List;
/**
 *  报告表
 */

public interface SoulReportApi {
    /**
     * 查询是该用户是否有报告生成
     * @param userId
     * @return
     */
    List<SoulReport> getReportMap(Long userId);
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
    /**
     * 根据用户id查询报告表
     * @param id
     * @return
     */
    List<SoulReport> findByList(Long id);

    /**
     *  解锁用户等级填充数据
     * @param soulcomment
     */
    void insert(SoulReport soulcomment);


}
