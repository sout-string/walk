package com.tanhua.manage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LogMapper {

    /**
     * 过去?天活跃用户数
     * @param date
     * @return
     */
    @Select("select count(distinct user_id) from tb_log where log_time>#{date}")
    Integer countActiveUserAfterDate(String date);
}
