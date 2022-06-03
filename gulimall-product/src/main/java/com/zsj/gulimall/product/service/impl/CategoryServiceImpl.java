package com.zsj.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.gulimall.product.dao.CategoryDao;
import com.zsj.gulimall.product.entity.CategoryEntity;
import com.zsj.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listwithTree() {
        //1.查出所有分类
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        //2.组装成父子的树形结构
        List<CategoryEntity> collects = categoryEntities.stream().filter(categoryEntity ->
                        categoryEntity.getParentCid() == 0).collect(Collectors.toList()).stream().
                map(categoryEntity ->
                {
                    categoryEntity.setChildren(getChidrens(categoryEntity, categoryEntities));
                    return categoryEntity;
                }).sorted((categoryEntity01, categoryEntity02) -> {
                    //菜单的排序
                    return (categoryEntity01.getSort()==null?0:categoryEntity01.getSort()) - (categoryEntity02.getSort()==null?0:categoryEntity02.getSort());
                }).collect(Collectors.toList());

        return collects;
    }

    public List<CategoryEntity> getChidrens(CategoryEntity root,List<CategoryEntity> all){

        List<CategoryEntity> collect = all.stream().filter(categoryEntity -> categoryEntity.getParentCid() == root.getCatId()).
                map(categoryEntity -> {
                    categoryEntity.setChildren(getChidrens(categoryEntity, all));
                    return categoryEntity;

                }).sorted((categoryEntity0, categoryEntity1) -> {
                    return (categoryEntity0.getSort()==null?0:categoryEntity0.getSort()) - (categoryEntity1.getSort()==null?0:categoryEntity1.getSort());
                }).collect(Collectors.toList());
        return collect;

    }


}