package com.tanhua.dubbo.api;

import com.tanhua.domain.db.SoulReport;

import java.util.List;

/**
 *  报告表
 */

public interface SoulReportApi {
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
