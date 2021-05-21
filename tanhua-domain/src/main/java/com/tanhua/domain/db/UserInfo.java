package com.tanhua.domain.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author : TuGen
 * @date : 2021/5/7 10:14
 */
@Data
public class UserInfo extends BasePojo{
    @TableId(type= IdType.INPUT)
    private Long id;
    private String nickname;
    private String avatar;
    private String birthday;
    private String gender;
    private Integer age;
    private String city;
    private String income;
    private String education;
    private String profession;
    private Integer marriage;
    private String tags;
    private String coverPic;
}
