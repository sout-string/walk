package com.tanhua.server.interceptor;

import com.tanhua.domain.db.User;

/**
 * ThreadLocal保存用户信息，用于验证token
 *
 * @author : TuGen
 * @date : 2021/5/8 22:36
 */
public class UserHolder {
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    /**
     * 保存用户信息
     * @param user
     */
    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    /**
     * 获取User
     * @return
     */
    public static User getUser() {
        return userThreadLocal.get();
    }

    /**
     * 获取用户ID
     * @return
     */
    public static Long getUserId() {
        return userThreadLocal.get().getId();
    }
}
