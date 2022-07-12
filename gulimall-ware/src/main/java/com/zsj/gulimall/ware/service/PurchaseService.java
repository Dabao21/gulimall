package com.zsj.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.common.utils.PageUtils;
import com.zsj.gulimall.ware.Vo.PurchaseDoneVo;
import com.zsj.gulimall.ware.Vo.mergeVo;
import com.zsj.gulimall.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author zhangshijie
 * @email 642011598@gmail.com
 * @date 2022-05-28 02:12:54
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void mergePurchase(mergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo purchaseDoneVo);
}

