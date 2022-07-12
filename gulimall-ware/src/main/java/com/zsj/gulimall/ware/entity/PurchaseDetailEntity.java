package com.zsj.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
*@date 2022/6/21 18:01
*@Author Dabao
* 1.采购需求表实体
* 2.
**/
@Data
@TableName("wms_purchase_detail")
public class PurchaseDetailEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 采购需求id
     */
    @TableId
    private Long id;
    /**
     * 采购单id
     */
    private Long purchaseId;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * sku_num
     */
    private Integer skuNum;
    /**
     * sku_price
     */
    private Integer skuPrice;
    /**
     * ware_id 仓库id
     */
    private Long wareId;

    /**
     * status 需求状态
     */
    private Integer status;


}
