package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.domain.db.BlackList;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.UserInfoVoAge;
import com.tanhua.dubbo.mapper.BlackListMapper;
import com.tanhua.dubbo.mapper.UserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/9 0:52
 */
@Service
@Slf4j
public class BlackListApiImpl implements BlackListApi {
    @Autowired
    private BlackListMapper blackListMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public PageResult findPageBlackList(Long page, Long pagesize, Long userId) {
        //查询黑名单表，分页查询获取ID
        Page<BlackList> userInfoPage1 = new Page<>(page, pagesize);
        QueryWrapper<BlackList> blackListQueryWrapper = new QueryWrapper<>();
        blackListQueryWrapper.eq("user_id", userId);
        IPage<BlackList> blackListIPage = blackListMapper.selectPage(userInfoPage1, blackListQueryWrapper);
        //构建结果
        PageResult pageResult = new PageResult();
        //设置信息
        pageResult.setCounts(blackListIPage.getTotal());
        pageResult.setPagesize(blackListIPage.getSize());
        pageResult.setPages(blackListIPage.getPages());
        pageResult.setItems(blackListIPage.getRecords());
        pageResult.setPage(page);
        return pageResult;
    }

    @Override
    public void deleteBlackList(Long userId,long blackUserId) {
        QueryWrapper<BlackList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("black_user_id", blackUserId);
        blackListMapper.delete(queryWrapper);
    }
}
