package com.zsj.gulimall.product.service.impl;

import com.zsj.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.zsj.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.zsj.gulimall.product.entity.AttrEntity;
import com.zsj.gulimall.product.service.AttrService;
import com.zsj.gulimall.product.vo.AttrGroupRelationVo;
import com.zsj.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.gulimall.product.dao.AttrGroupDao;
import com.zsj.gulimall.product.entity.AttrGroupEntity;
import com.zsj.gulimall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    //属性分组页面的右边的页面显示的响应数据方法
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key =(String) params.get("key");
        //select * from pms_attr_group where catelog id=? and(attr_group_id=key or attr_group_name like key;
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if(StringUtils.isNotEmpty(key)){
            wrapper.and((obj)->{
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });

        }
        if(catelogId==0)
        {
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);

        }else
        {
            wrapper.eq("catelog_id",catelogId);

            IPage<AttrGroupEntity> page = this.page(                   //第一次把这段放上面那段括号了，导致key(关键字)为空的时候(非模糊查询)都没有返回page
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }

    }


    @Override   //删除属性分组页面分组关联的属性（就是删除属性分组关联表的数据）
    public void deleteRelation(AttrGroupRelationVo[] vos) {

        //this.remove(new QueryWrapper<>().eq("attr_id",).eq("attr_group_id",))
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {  //雷丰杨蜜汁操作，参数传的被拆解的实体类的VO，然后又转成没被拆解的实体类
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, attrAttrgroupRelationEntity);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        attrAttrgroupRelationDao.deleteBatchRelation(entities);
    }

    /**
    *@date 2022/6/19 15:35
    *@Author Dabao
    * 1.根据分类Id查找所有此分类下的分组和分组对应的属性
    * 2.
    **/
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrByCatelogId(Long catelogId) {

        // 2022/6/19 15:42 1.查询分组信息
        List<AttrGroupEntity> attrGroupEntityList = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        // 2022/6/19 15:44  2.从关系表中查出分组关联的所有属性
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVoList = attrGroupEntityList.stream().map((group) -> {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group, attrGroupWithAttrsVo);
            // 2022/6/19 15:59  查找这个分组下的属性，如果这个分组下没有关联属性则返回空？不用判断吧？要的！
            List<AttrEntity> relationAttrList = attrService.getRelationAttr(attrGroupWithAttrsVo.getAttrGroupId());
            if (relationAttrList!=null) {
                attrGroupWithAttrsVo.setAttrs(relationAttrList);

            }
            // 2022/6/19 16:05 不过很奇怪，如果没设也是null吧？
            return attrGroupWithAttrsVo;

        }).collect(Collectors.toList());

        return attrGroupWithAttrsVoList;
    }

}