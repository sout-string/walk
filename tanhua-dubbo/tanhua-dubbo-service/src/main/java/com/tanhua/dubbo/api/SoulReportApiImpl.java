package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
     * 根据用户id查询报告表
     * @param id
     * @return
     */

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
