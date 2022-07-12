package com.zsj.gulimall.ware.Vo;

import lombok.Data;

import java.util.List;

@Data
public class mergeVo {

    /**
    *@date 2022/6/21 22:23
    *@Author Dabao
    * 1.purchaseId: 1, //整单id
     *   items:[1,2,3,4] //合并项集合
    * 2.
    **/
    private Long purchaseId;   //采购单id
    private List<Long> items; //采购需求的集合

}
