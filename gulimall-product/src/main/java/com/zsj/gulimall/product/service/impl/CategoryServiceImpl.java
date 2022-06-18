package com.zsj.gulimall.product.service.impl;

import com.zsj.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.transaction.annotation.Transactional;

import static java.lang.System.exit;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

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

    @Override
    public void removeMenuByIds(List<Long> asList) {

        //TODO 1.检查当前删除的菜单，是否被别的地方引用
        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

 /*   @Override
    public Long[] findCatelogPath(Long catelogId) {   //找当前结点的父节点组成的路径
        List<Long> paths=new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return (Long[])paths.toArray(new Long[parentPath.size()]);
    }

    private List<Long> findParentPath(Long catelogId,List<Long> paths)  //简单递归找父节点
    {
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid()!=0)
        {
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }*/
    @Override
    public Long[] findCatelogPath(Long catelogId) {//找当前结点的父节点组成的路径 一行搞定
        List<Long> paths=new ArrayList<>();
        if (this.getById(catelogId)==null)
        {
            System.out.println("你他妈这catelogId根本尼玛的不存在,不存在就给你放个0吧");      //优化程序
            paths.add(0L);
        }
        else {
            for(Long temp=catelogId;paths.add(temp)&&(temp=this.getById(temp).getParentCid())!=0;); //一行搞定，代码的优雅 ,日你妈的空指针异常,sorry只是没那个id,
            Collections.reverse(paths);
        }
        //然后发现单元测试不用重新启动主程序，因为单元测试就是直接获取java的数据，它不是前端
        return (Long[])paths.toArray(new Long[paths.size()]);
    }

    @Transactional
    @Override            //更新联合关系的表
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
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