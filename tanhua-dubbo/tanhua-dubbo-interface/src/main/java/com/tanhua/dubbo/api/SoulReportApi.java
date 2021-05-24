package com.tanhua.dubbo.api;

import com.tanhua.domain.db.Soulcomment;

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
    List<Soulcomment> getReportMap(Long userId);
}
