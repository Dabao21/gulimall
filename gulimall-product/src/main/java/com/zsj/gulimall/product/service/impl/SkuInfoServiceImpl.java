package com.zsj.gulimall.product.service.impl;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.gulimall.product.dao.SkuInfoDao;
import com.zsj.gulimall.product.entity.SkuInfoEntity;
import com.zsj.gulimall.product.service.SkuInfoService;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
    *@date 2022/6/20 1:06
    *@Author Dabao
    * 1.
    * 2.
    **/
    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }
    
    /**
    *@date 2022/6/21 6:30 
    *@Author Dabao
    * 1.
    * 2.
    **/
    @Override
    public PageUtils queryPageByConditon(Map<String, Object> params) {

        QueryWrapper<SkuInfoEntity> skuInfoEntityQueryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            skuInfoEntityQueryWrapper.and(item->{
                item.eq("sku_id",key).or().like("sku_name",key);
            });
        }
        String brandId = (String)params.get("brandId");
        if (!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)) {
            System.out.println("brandId:"+brandId);
            skuInfoEntityQueryWrapper.eq("brand_id",brandId);
        }
        String catelogId = (String)params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)) {
            System.out.println("catelogId:"+catelogId);
            skuInfoEntityQueryWrapper.eq("catalog_id",catelogId);
        }
        String min = (String)params.get("min");
        if (!StringUtils.isEmpty(min)) {
            System.out.println("min:"+min);
            skuInfoEntityQueryWrapper.ge("price",min);
        }

        String max = (String)params.get("max");
        if (!StringUtils.isEmpty(max)) {
            //=1是大于 0是等于 -1是小于
            BigDecimal bigDecimal = new BigDecimal(max); //  2022/6/21 6:48  把这里面的max打上双引号了，结果找半天
            if (bigDecimal.compareTo(BigDecimal.ZERO)>0) {
                System.out.println("max:"+max);
                skuInfoEntityQueryWrapper.le("price",max);
            }
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                skuInfoEntityQueryWrapper

        );

        return new PageUtils(page);


    }

}