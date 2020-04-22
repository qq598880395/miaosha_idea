package com.imooc.miaosha.dao;

import org.apache.ibatis.annotations.*;

import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.OrderInfo;

import java.util.List;

@Mapper
public interface OrderDao {
	//由用户id和商品id得到订单
	@Select("select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
	public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);
	//创建订单
	@Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
			+ "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
	@SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
	public long insert(OrderInfo orderInfo);
	//创建订单关联信息
	@Insert("insert into miaosha_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
	public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);
	//获取用户所有订单
	@Select("select * from order_info where user_id=#{userId}")
    public List<OrderInfo> getMyOrderListByUserId(@Param("userId")long userId);
	//支付成功
	@Update("update order_info set status = 1 where id = #{id}")
    void paySeccess(int id);
	//由订单id得到一条我的订单信息
	@Select("select * from order_info where id=#{id}")
	public OrderInfo getMyOrderDetailByOrderId(@Param("id") int id);
	//得到订单信息
	@Select("select * from order_info where id = #{orderId}")
	public OrderInfo getOrderById(@Param("orderId")long orderId);
}
