package com.zsj.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.gulimall.product.dao.ProductAttrValueDao;
import com.zsj.gulimall.product.entity.ProductAttrValueEntity;
import com.zsj.gulimall.product.service.ProductAttrValueService;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    /**
    *@date 2022/6/19 21:57
    *@Author Dabao
    * 1.为什么这里不他妈直接用savebatch 弹幕说为了他妈规范，方便以后改代码？
    * 2.
    **/
    @Override
    public void saveProductAttr(List<ProductAttrValueEntity> productAttrValueEntityList) {
        this.saveBatch(productAttrValueEntityList);
    }

}