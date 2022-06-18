package com.zsj.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.gulimall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author zhangshijie
 * @email 642011598@gmail.com
 * @date 2022-05-27 07:12:15
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetai(BrandEntity brand);
}

