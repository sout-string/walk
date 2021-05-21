package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.domain.db.Settings;
import com.tanhua.dubbo.mapper.SettingsMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : TuGen
 * @date : 2021/5/8 23:39
 */
@Service
public class SettingsApiImpl implements SettingsApi {
    @Autowired
    private SettingsMapper settingsMapper;

    /**
     * 根据用户ID查询
     * @param id
     * @return
     */
    @Override
    public Settings findByUserId(Long id) {
        QueryWrapper<Settings> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        return settingsMapper.selectOne(queryWrapper);
    }

    /**
     * 保存用户设置
     *
     * @param settings
     */
    @Override
    public void save(Settings settings) {
        settingsMapper.insert(settings);
    }

    /**
     * 更新用户设置
     *
     * @param settings
     */
    @Override
    public void update(Settings settings) {
        settingsMapper.updateById(settings);
    }
}
