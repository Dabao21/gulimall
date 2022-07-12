package com.zsj.gulimall.product.feign;

import com.zsj.common.to.SkuReductionTo;
import com.zsj.common.to.SpuBoundTo;
import com.zsj.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    /**
    *@date 2022/6/20 3:22 
    *@Author Dabao
    * 1.只要json数据模型是兼容的，双方服务无需同一个to
    * 2.
    **/

    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);


    @PostMapping("/coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
