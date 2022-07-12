package com.zsj.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String,Object> params);
    /**
    *@date 2022/6/22 6:12
    *@Author Dabao
    * 1.
    * 2.
    **/
    public List<PurchaseDetailEntity> getPurchaseDetailByPurchaseId(Long id);

    }
