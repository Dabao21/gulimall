package com.zsj.gulimall.order.dao;

import com.zsj.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author zhangshijie
 * @email 642011598@gmail.com
 * @date 2022-05-28 01:43:54
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
