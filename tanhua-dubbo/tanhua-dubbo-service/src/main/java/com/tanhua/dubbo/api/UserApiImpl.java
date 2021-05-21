package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.domain.db.User;
import com.tanhua.dubbo.mapper.UserMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author : 涂根
 * @date : 2021/05/03 下午 5:44
 */
@Service
public class UserApiImpl implements UserApi {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Long save(User user) {
        //设置了自动配置，试试注释后能否配置
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public User findByMobile(String mobile) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User findById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public void updateById(User user) {
        userMapper.updateById(user);
    }
}
