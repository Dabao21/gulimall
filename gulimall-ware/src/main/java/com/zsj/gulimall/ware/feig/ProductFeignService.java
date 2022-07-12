package com.zsj.gulimall.ware.feig;


import com.zsj.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-product")
public interface ProductFeignService {

    /**
    *@date 2022/6/22 7:00
    *@Author Dabao
    * 1./product/skuinfo/info/{skuId}
    * 2.
    **/
    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
