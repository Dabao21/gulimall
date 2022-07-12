package com.zsj.gulimall.ware.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;
import com.zsj.gulimall.ware.dao.PurchaseDetailDao;
import com.zsj.gulimall.ware.entity.PurchaseDetailEntity;
import com.zsj.gulimall.ware.service.PurchaseDetailService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    /**
    *@date 2022/6/21 18:22 
    *@Author Dabao
    * 1.http://localhost:8001/#/ware-purchase 采购单页面
    * 2. status 状态
     * 3.wareId 仓库id
    **/
    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<PurchaseDetailEntity> purchaseDetailEntityQueryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            purchaseDetailEntityQueryWrapper.and(item->{
                item.eq("purchase_id",key).or().eq("sku_id",key);
            });

        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(key)) {
            purchaseDetailEntityQueryWrapper.eq("ware_id",wareId);

        }

        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            purchaseDetailEntityQueryWrapper.eq("status",status);

        }
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                purchaseDetailEntityQueryWrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }


    @Override
    public List<PurchaseDetailEntity> getPurchaseDetailByPurchaseId(Long id) {
        QueryWrapper<PurchaseDetailEntity> purchaseDetailEntityQueryWrapper = new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", id);
        List<PurchaseDetailEntity> purchaseDetailEntityList = this.list(purchaseDetailEntityQueryWrapper);
        return purchaseDetailEntityList;
    }


}
