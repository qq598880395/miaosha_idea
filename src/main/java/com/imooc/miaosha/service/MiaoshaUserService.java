package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.MiaoshaUserDao;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    public static final String COOKI_NAME_TOKEN="token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;
    @Autowired
    RedisService redisService;
    public MiaoshaUser getById(long id){
        return miaoshaUserDao.getById(id);
    }
    public boolean login(HttpServletResponse response,LoginVo loginVo){
        if(loginVo ==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile=loginVo.getMobile();
        String formPass=loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if(user==null){
            throw new GlobalException( CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass=user.getPassword();
        String saltDB = user.getSalt();
        String calcPass =MD5Util.formPassToDbPass(formPass,saltDB);
        System.out.println(dbPass);
        System.out.println(calcPass);
        if(!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response,token,user);

        return true;
    }

    public boolean regist(HttpServletResponse response,LoginVo loginVo){
        if(loginVo ==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile=loginVo.getMobile();
        long tel = Long.parseLong(mobile);
        String fromPass=loginVo.getPassword();
        MiaoshaUser user = getById(Long.parseLong(mobile));       //判断手机号是否存在
        if(user!=null){
            throw new GlobalException( CodeMsg.MOBILE_EXIST);
        }
        String salt = "1a2b3c4d";
        String dbPass = MD5Util.formPassToDbPass(fromPass,salt);
        MiaoshaUser newUser = new MiaoshaUser();
        newUser.setId(tel);
        newUser.setNickname(mobile);
        newUser.setPassword(dbPass);
        newUser.setSalt(salt);
        miaoshaUserDao.addUser(newUser);
        return true;
    }
    private void addCookie(HttpServletResponse response,String token ,MiaoshaUser user ){

        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());//当token过期 cookie就过期
        cookie.setPath("/");
        response.addCookie(cookie);//cookie存到客户端
    }
    public MiaoshaUser getByToken(HttpServletResponse response,String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser user =  redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);
       if(user!=null) {
           addCookie(response,token,user);//延长有效期
       }
       return user;
    }
}
