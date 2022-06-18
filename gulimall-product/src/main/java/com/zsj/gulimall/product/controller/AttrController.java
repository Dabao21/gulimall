package com.zsj.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.zsj.gulimall.product.vo.AttrRespVo;
import com.zsj.gulimall.product.vo.AttrVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zsj.gulimall.product.entity.AttrEntity;
import com.zsj.gulimall.product.service.AttrService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;



/**
 * 商品属性
 *
 * @author zhangshijie
 * @email 642011598@gmail.com
 * @date 2022-05-27 08:03:13
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    ///product/attr/base/list/{catelogId}
    ///product/attr/sale/list/{catelogId}
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId,
                          @PathVariable("attrType" ) String type){

        PageUtils page=attrService.queryBaseOrSaleAttrPage(params,catelogId,type);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息 2022/6/18 在http://localhost:8001/#/product-baseattr的修改中请求响应回显 查询属性详情
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		//AttrEntity attr = attrService.getById(attrId);
          AttrRespVo attrRespVo= attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		//attrService.save(attr);
        attrService.saveAttr(attr);
        return R.ok();
    }

    /**
     * 修改 2022/6/18 4:17
     * by Zhang shi jie @Dabao
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrRespVo attr){
		attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
