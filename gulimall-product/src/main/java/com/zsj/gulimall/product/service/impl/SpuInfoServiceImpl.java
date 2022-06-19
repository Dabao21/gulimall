package com.zsj.gulimall.product.service.impl;

import com.zsj.common.to.SkuReductionTo;
import com.zsj.common.to.SpuBoundTo;
import com.zsj.common.utils.R;
import com.zsj.gulimall.product.entity.*;
import com.zsj.gulimall.product.feign.CouponFeignService;
import com.zsj.gulimall.product.service.*;
import com.zsj.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
    *@date 2022/6/19 18:49
    *@Author Dabao
    * 1.大保存
    * 2.  //  2022/6/20 4:25    终于搞完。。。。。。。。。。。
    **/
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuInfo) {
            // 2022/6/19 18:49 1.保存spu基本信息 `pms_spu_info`
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);            //保存之后会回写id
            // 2022/6/19 20:21 2.保存spu的描述图片 `pms_spu_images`
        List<String> decript = spuInfo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);
        // 2022/6/19 20:21 3.保存spu的图片集`pms_spu_images`
        List<String> images = spuInfo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(),images);
            // 2022/6/19 20:22 4.保存spu的规格参数属性集  `pms_product_attr_value`
        List<SpuSaveVo.BaseAttrs> baseAttrs = spuInfo.getBaseAttrs();
        List<ProductAttrValueEntity> productAttrValueEntityList = baseAttrs.stream().map(item -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setQuickShow(item.getShowDesc());
            productAttrValueEntity.setAttrValue(item.getAttrValues());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            AttrEntity attrEntity = attrService.getById(item.getAttrId());
            productAttrValueEntity.setAttrId(item.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(productAttrValueEntityList); //  2022/6/19 21:55 这里为什么不直接用savebatch()，弹幕说方便以后改代码吧？
        //  2022/6/19 20:41  5.保存spu的积分信息   `gulimall_sms`->`sms_spu_bounds`  远程调用
        SpuSaveVo.Bounds bounds = spuInfo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);//  2022/6/20 3:22 远程调用接口
        if (r.getcode()!=0) {
            log.error("远程保存spu积分失败");
        }
        //  2022/6/19 20:26  5.保存当前spu对应的所有sku信息
        List<SpuSaveVo.Skus> skus = spuInfo.getSkus();
        if (skus!=null) {
            skus.forEach(item->{

                String defaultImg="";
                for (SpuSaveVo.Skus.Images image : item.getImages()) {    //  2022/6/20 1:07 skus中每一个sku有一个图片集合 找出默认图片
                    if (image.getDefaultImg()==1)
                        defaultImg=image.getImgUrl();
                }
                //  2022/6/19 20:34  5.1sku的基本信息  `pms_sku_info`
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity); //  2022/6/20 1:06 保存完回写主键id
                Long skuId = skuInfoEntity.getSkuId();
                //  2022/6/19 20:34  5.2 sku的图片信息 `pms_sku_images`
                // TODO 2022/6/20 5:20 没有图片的路径无需保存
                List<SkuImagesEntity> skuImagesEntities = item.getImages().stream().map(image -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setDefaultImg(image.getDefaultImg());
                    skuImagesEntity.setImgUrl(image.getImgUrl());
                    skuImagesEntity.setSkuId(skuId);
                    return skuImagesEntity;
                }).filter(entity->{
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(skuImagesEntities);
                //  2022/6/19 20:35  5.3 sku的销售属性  `pms_sku_sale_attr_value`
                List<SpuSaveVo.Skus.Attr> attrs = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = attrs.stream().map(a -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntityList);

                //  2022/6/19 20:37  5.4 sku的满减信息    `gulimall_sms`->`sms_sku_ladder`\`sms_sku_full_reduction`  \`sms_member_price`
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                R r1 = couponFeignService.saveSkuReduction(skuReductionTo);//  2022/6/20 4:24         远程调用
                if (r1.getcode()!=0) {
                    log.error("远程保存sku的满减信息失败");
                }
            });


        }






    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }




}