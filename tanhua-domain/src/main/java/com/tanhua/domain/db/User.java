package com.tanhua.domain.db;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :Gen Tu
 * @date :2021/5/03 下午 5:37
 */
@Data
public class User extends BasePojo implements Serializable {
    private Long id;
    /** 手机号 */
    private String mobile;
    /** 密码，json序列化时忽略 */
    @JSONField(serialize = false)
    private String password;
}