package com.zsj.gulimall.ware.service.impl;

import com.zsj.common.constant.WareConstant;
import com.zsj.common.utils.Constant;
import com.zsj.gulimall.ware.Vo.PurchaseDoneVo;
import com.zsj.gulimall.ware.Vo.PurchaseItemDoneVo;
import com.zsj.gulimall.ware.Vo.mergeVo;
import com.zsj.gulimall.ware.entity.PurchaseDetailEntity;
import com.zsj.gulimall.ware.service.WareSkuService;
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

import com.zsj.gulimall.ware.dao.PurchaseDao;
import com.zsj.gulimall.ware.entity.PurchaseEntity;
import com.zsj.gulimall.ware.service.
        PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailServiceImpl purchaseDetailService;

    @Autowired
    WareSkuService wareSkuService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }
    /**
    *@date 2022/6/21 19:04
    *@Author Dabao
    * 1. 查询未被领取的采购单
    * 2.
    **/
    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }
    /**
    *@date 2022/6/21 22:42
    *@Author Dabao
    * 1.合并采购需求到采购单
    * 2.
    **/
    @Transactional
    @Override
    public void mergePurchase(mergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId==null) {
            //  2022/6/21 22:42         没有采购单就新建一个
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATESD.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();

        }
        //  2022/6/22 5:09         确认采购单状态是0或者1才可以合并
        PurchaseEntity purchaseEntity1 = this.getById(purchaseId);
        Integer status = purchaseEntity1.getStatus();
        if (status == WareConstant.PurchaseStatusEnum.CREATESD.getCode()||status == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
            //  2022/6/22 5:14         合并
            List<Long> items = mergeVo.getItems();
            Long finalPurchaseId = purchaseId;
            List<PurchaseDetailEntity> purchaseDetailEntityList = items.stream().map(i -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setId(i);
                purchaseDetailEntity.setPurchaseId(finalPurchaseId);
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
                return purchaseDetailEntity;
            }).collect(Collectors.toList());

            purchaseDetailService.updateBatchById(purchaseDetailEntityList);
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setId(purchaseId);
            purchaseEntity.setUpdateTime(new Date());
            this.updateById(purchaseEntity);
        }


    }
    /**
    *@date 2022/6/22 4:41
    *@Author Dabao
    * 1.
    * 2.
    **/
    @Override
    public void received(List<Long> ids) {

        //  2022/6/22 4:35         1.确认当前采购单是新建或者已分配状态
        List<PurchaseEntity> purchaseEntityList = ids.stream().map(id -> {
            PurchaseEntity purchaseEntity = this.getById(id);
            return purchaseEntity;

        }).filter(item -> {
            return item.getStatus() == WareConstant.PurchaseStatusEnum.CREATESD.getCode() || item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode();

        }).map(item->{
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());
        //  2022/6/22 4:35 改变采购单的状态
        this.updateBatchById(purchaseEntityList);
        //  2022/6/22 4:40         改变采购需求的状态
        purchaseEntityList.forEach(item->{
            Long id = item.getId();
            //  2022/6/22 5:04         先根据getPurchaseDetailByPurchaseId查出PurchaseDetailEntity
            List<PurchaseDetailEntity> purchaseDetailEntity=purchaseDetailService.getPurchaseDetailByPurchaseId(id);
            List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailEntity.stream().map(item1 -> {
                item1.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return item1;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(purchaseDetailEntityList);
        });

    }
    /**
    *@date 2022/6/22 5:33
    *@Author Dabao
    * 1.和recived一样，改变一下状态
    * 2.
    **/
    @Transactional
    @Override
    public void done(PurchaseDoneVo purchaseDoneVo) {

        //  2022/6/22 5:37         改变采购单的状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();


        boolean[] flag = {false};
        //  2022/6/22 5:39         改变采购需求的状态
        List<PurchaseItemDoneVo> items = purchaseDoneVo.getItems();
        List<PurchaseDetailEntity> collect = items.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item.getItemId());
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                flag[0] = true;

            }
            else {
                //  2022/6/22 6:06         采购成功的入库
                PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(entity.getSkuId(),entity.getWareId(),entity.getSkuNum());
            }
            purchaseDetailEntity.setStatus(item.getStatus());

            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);

        //  2022/6/22 5:53         如果flag为1则采购失败
        purchaseEntity.setStatus(flag[0]?WareConstant.PurchaseStatusEnum.FINISH.getCode():WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
        //  2022/6/22 5:54         3改变库存状态

    }

}