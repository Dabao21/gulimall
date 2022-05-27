package com.zsj.gulimall.product.dao;

import com.zsj.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author zhangshijie
 * @email 642011598@gmail.com
 * @date 2022-05-27 07:12:15
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
