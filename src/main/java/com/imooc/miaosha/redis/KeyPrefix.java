package com.imooc.miaosha.redis;

public interface KeyPrefix {
    //缓存存活时间
    public int expireSeconds();
    //缓存字段名
    public String getPrefix();

}
