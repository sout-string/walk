package com.tanhua.domain.vo;

import com.tanhua.domain.db.UserInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class SoulReportVo implements Serializable {

    private String conclusion;//测试的结果
    private String cover;//鉴定的结果

    //储存纬度的对象或者构建在list中储存map集合
    private List<Map<String,String>> dimensions;//维度
    //list集合中个人的详细信息
    private List<UserInfo> similarYou;
}
