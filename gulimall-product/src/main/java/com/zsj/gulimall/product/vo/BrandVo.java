package com.zsj.gulimall.product.vo;

import com.zsj.common.valid.AddGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BrandVo {

    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 品牌名
     */

    private String brandName;

}
