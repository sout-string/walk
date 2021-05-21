package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.domain.db.BlackList;
import com.tanhua.domain.db.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author : TuGen
 * @date : 2021/5/9 1:04
 */
public interface BlackListMapper extends BaseMapper<BlackList> {
    /**
     * 查询 黑名单用户信息
     * 需要 tb_user_info 和 tb_black_list两张变联合查询
     * 分页查询
     */
    @Select(value = "select tui.id,tui.avatar,tui.nickname,tui.gender,tui.age from  tb_user_info tui,tb_black_list tbl where tui.id = tbl.black_user_id and tbl.user_id=#{userId}")
    IPage<UserInfo> findBlackList(Page<UserInfo> page, @Param("userId") Long userId);
}
