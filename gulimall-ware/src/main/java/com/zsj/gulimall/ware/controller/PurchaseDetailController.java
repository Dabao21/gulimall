package com.zsj.gulimall.ware.controller;


import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;
import com.zsj.gulimall.ware.entity.PurchaseDetailEntity;
import com.zsj.gulimall.ware.entity.PurchaseEntity;
import com.zsj.gulimall.ware.service.PurchaseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("ware/purchasedetail")
public class PurchaseDetailController {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    /**
    *@date 2022/6/21 18:20
    *@Author Dabao
    * 1.
    * 2.
    **/
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseDetailService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    *@date 2022/6/21 19:09
    *@Author Dabao
    * 1.保存
    * 2.
    **/
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseDetailEntity purchaseDetail){
        purchaseDetailService.save(purchaseDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseDetailEntity purchaseDetail){
        purchaseDetailService.updateById(purchaseDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
        purchaseDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
        PurchaseDetailEntity  purchaseDetail= purchaseDetailService.getById(id);

        return R.ok().put("purchaseDetail", purchaseDetail);
    }
}
