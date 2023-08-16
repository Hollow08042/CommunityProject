package com.chen.community.dao;

import com.chen.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @Description 登录凭证
 * @date 2023-08-04 14:20
 **/
@Mapper
//设置为不推荐使用
@Deprecated
public interface LoginTicketMapper {
    /*@Insert({
        "insert into login_ticket(user_id,ticket,status,expired) ",
         "values (#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")*/
    int insertLoginTicket(LoginTicket loginTicket);

    /*@Select({"select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"})*/
    LoginTicket selectByTicket(String ticket);

    /*@Update({"update login_ticket set status=#{status} where ticket=#{ticket}"})*/
    int updateStatus(String ticket,Integer status);
}
