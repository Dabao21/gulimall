package com.zsj.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.gulimall.product.entity.AttrEntity;
import com.zsj.gulimall.product.vo.AttrRespVo;
import com.zsj.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author zhangshijie
 * @email 642011598@gmail.com
 * @date 2022-05-27 07:12:15
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseOrSaleAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrRespVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    PageUtils getNoRelationAttr(Long attrgroupId, Map<String, Object> params);
}

