package com.tanhua.dubbo.api;

import com.tanhua.domain.db.Settings;

/**
 * @author : TuGen
 * @date : 2021/5/8 23:37
 */
public interface SettingsApi {



    Settings findByUserId(Long id);

    void save(Settings settings);

    void update(Settings settings);
}
