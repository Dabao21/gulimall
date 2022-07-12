package com.zsj.gulimall.ware.Vo;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseDoneVo {

    private Long id;
    private List<PurchaseItemDoneVo> items;

}
