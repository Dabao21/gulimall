package com.zsj.gulimall.coupon.service.impl;

import com.zsj.common.to.SkuReductionTo;
import com.zsj.gulimall.coupon.entity.MemberPriceEntity;
import com.zsj.gulimall.coupon.entity.SkuLadderEntity;
import com.zsj.gulimall.coupon.service.MemberPriceService;
import com.zsj.gulimall.coupon.service.SkuLadderService;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.gulimall.coupon.dao.SkuFullReductionDao;
import com.zsj.gulimall.coupon.entity.SkuFullReductionEntity;
import com.zsj.gulimall.coupon.service.SkuFullReductionService;
import org.springframework.transaction.annotation.Transactional;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;

    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    /**
    *@date 2022/6/20 3:51
    *@Author Dabao
    * 1.sku的满减信息    `gulimall_sms`->`sms_sku_ladder`\`sms_sku_full_reduction`  \`sms_member_price`
    * 2.被远程调用的接口
    **/
    @Override
    @Transactional
    public void saveSkuRduction(SkuReductionTo skuReductionTo) {
        //  2022/6/20 3:51         sms_sku_ladder 满数量减
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());   //  2022/6/20 4:06   满（数量）AddOther减对应的CountStatus
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        //skuLadderEntity.setPrice();
        skuLadderService.save(skuLadderEntity);

        //  2022/6/20 3:57      sms_sku_full_reduction 满价格减
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo,skuFullReductionEntity);
        skuFullReductionEntity.setAddOther(skuReductionTo.getPriceStatus()); //  2022/6/20 4:05  满（价格）AddOther减对应的PriceStatus
        this.save(skuFullReductionEntity);

        //  2022/6/20 4:08      `sms_member_price`
        List<SkuReductionTo.MemberPrice> memberPriceList = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> memberPriceEntityList = memberPriceList.stream().map(item -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setMemberPrice(item.getPrice());
            memberPriceEntity.setMemberLevelName(item.getName());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setAddOther(1);
            return memberPriceEntity;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntityList);

    }

}