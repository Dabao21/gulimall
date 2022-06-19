package com.zsj.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.zsj.gulimall.product.entity.AttrEntity;
import com.zsj.gulimall.product.service.AttrAttrgroupRelationService;
import com.zsj.gulimall.product.service.AttrService;
import com.zsj.gulimall.product.service.CategoryService;
import com.zsj.gulimall.product.vo.AttrGroupRelationVo;
import com.zsj.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zsj.gulimall.product.entity.AttrGroupEntity;
import com.zsj.gulimall.product.service.AttrGroupService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;



/**
 * 属性分组
 *
 * @author zhangshijie
 * @email 642011598@gmail.com
 * @date 2022-05-27 08:03:13
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;          //引用了这个Service然后用的方法不是直属的方法，而是创建了一个只服务于这个Controller的方法，感觉不太好

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;


    /**
    *@date 2022/6/19 15:24
    *@Author Dabao
    * 1./product/attrgroup/{catelogId}/withattr
    * 2.
    **/
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttr(@PathVariable("catelogId") Long catelogId)
    {
        /**
        *@date 2022/6/19 15:27
        *@Author Dabao
        * 1.找出当前分类下的所有分组
        * 2.找出这些分组的所有属性
        **/
         List<AttrGroupWithAttrsVo> vosList=attrGroupService.getAttrGroupWithAttrByCatelogId(catelogId);
         return R.ok().put("data",vosList);

    }


    ///product/attrgroup/attr/relation  2022/6/19 3:54不按照雷锋杨来
    @PostMapping
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
            attrAttrgroupRelationService.saveRelation(vos);
            return R.ok();
    }

    ///product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping("/{attrgroupId}/attr/relation")    //获取分组实体关联的属性实体 1-n
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId)
    {
        List<AttrEntity> attrEntities=attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",attrEntities);
    }

    ///product/attrgroup/{attrgroupId}/noattr/relation 获取分组没有关联的本分类下的属性
    @GetMapping("/{attrgroupId}/noattr/relation")    //获取分组实体关联的属性实体 1-n
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String, Object> params)
    {
       PageUtils pageUtils=attrService.getNoRelationAttr(attrgroupId,params);
        return R.ok().put("data",pageUtils);  //这里干你妈的被坑了，前端写的data.page 第一个data不是这个data 前端应该data.data 2022/6/19 3:40
    }


    ///product/attrgroup/attr/relation/delete 移除关联
    @RequestMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos)  //Post中 @RequestBody将json数据封装成对象
    {
        attrGroupService.deleteRelation(vos);
        return R.ok();
    }


    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId){
        //PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path =categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
