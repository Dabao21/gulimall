package com.zsj.gulimall.ware.Vo;

import lombok.Data;

@Data
public class PurchaseItemDoneVo {

    //  2022/6/22 5:28         itemid:1,status:3,reason:""
    private Long itemId;
    private Integer status;
    private String reason;
}
