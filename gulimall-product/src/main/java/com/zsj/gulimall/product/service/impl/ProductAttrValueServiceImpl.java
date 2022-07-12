package com.zsj.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.gulimall.product.dao.ProductAttrValueDao;
import com.zsj.gulimall.product.entity.ProductAttrValueEntity;
import com.zsj.gulimall.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;


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

    @Override
    public List<ProductAttrValueEntity> baseAttrListForSpu(Long spuId) {

        QueryWrapper<ProductAttrValueEntity> productAttrValueEntityQueryWrapper = new QueryWrapper<>();
        productAttrValueEntityQueryWrapper.eq("spu_id",spuId);
        List<ProductAttrValueEntity> productAttrValueEntityList = this.baseMapper.selectList(productAttrValueEntityQueryWrapper);

        return productAttrValueEntityList;
    }
    /**
    *@date 2022/6/22 8:27  spu管理的规格按钮 规格维护
    *@Author Dabao
    * 1.
    * 2.
    **/
    @Transactional
    @Override
    public void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities) {
        //  2022/6/22 8:28         删除souId之前对应的属性
        this.baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));

        //  2022/6/22 8:31         加上现在的属性
        List<ProductAttrValueEntity> collect = entities.stream().map(item -> {
            item.setSpuId(spuId);
            return item;
        }).collect(Collectors.toList());
        this.saveBatch(collect);


        
    }

}