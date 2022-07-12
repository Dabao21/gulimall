package com.zsj.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.zsj.gulimall.ware.Vo.PurchaseDoneVo;
import com.zsj.gulimall.ware.Vo.mergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zsj.gulimall.ware.entity.PurchaseEntity;
import com.zsj.gulimall.ware.service.PurchaseService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;



/**
 * 采购信息
 *
 * @author zhangshijie 采购需求
 * @email 642011598@gmail.com
 * @date 2022-05-28 02:12:54
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;
    /**
    *@date 2022/6/22 5:25
    *@Author Dabao
    * 1./ware/purchase/done 完成采购单
    * 2.
    **/
    @PostMapping("/done")
    public R done(@RequestBody PurchaseDoneVo purchaseDoneVo)
    {
        purchaseService.done(purchaseDoneVo);
        return R.ok();
    }

    /**
    *@date 2022/6/22 4:24
    *@Author Dabao
    * 1.领取采购单
    * 2.
    **/
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids)
    {

        purchaseService.received(ids);
        return R.ok();
    }



    /**
    *@date 2022/6/21 22:25
    *@Author Dabao
    * 1./ware/purchase/merge  采购需求合并到未出发的采购单
    * 2.
    **/
    @RequestMapping("/merge")
    public R merge(@RequestBody mergeVo mergeVo)
    {
        purchaseService.mergePurchase(mergeVo);

        return R.ok();
    }

    /**
    *@date 2022/6/21 19:00
    *@Author Dabao
    * 1./ware/purchase/unreceive/list 查询未被领取的采购单
    * 2.
    **/
    @RequestMapping("unreceive/list")
    //@RequiresPermissions("ware:purchase:list")
    public R unreceivelist(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnreceive(params);

        return R.ok().put("page", page);
    }
    
    /**
     * 列表
     */
    /**
    *@date 2022/6/21 17:37
    *@Author Dabao
    * 1.采购单的查询
    * 2.
    **/
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
