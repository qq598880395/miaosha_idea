package com.imooc.miaosha.dao;

import com.imooc.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MiaoshaUserDao {
    //由id获取用户
    @Select("select * from miaosha_user where id= #{id}")
    public MiaoshaUser getById(@Param("id") long id);
    //添加新用户
    @Insert("insert into miaosha_user " +
            "(id,nickname,password,salt)values(#{id},#{nickname},#{password},#{salt})")
    public int addUser(MiaoshaUser miaoshaUser );
}
