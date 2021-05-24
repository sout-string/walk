package com.tanhua.dubbo.api;

import com.tanhua.domain.vo.SoulQuestionVo;

import java.util.List;

/**
 * @description
 * @author:Dell
 * @date:2021/5/23 11:45
 */


public interface SoulQuestionApi {

    /**
     * 查询相对题目表
     * @param id
     * @return
     */
    List<SoulQuestionVo> findLIstTable(Integer id);
}
