package com.zsj.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.zsj.common.constant.ProductConstant;
import com.zsj.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.zsj.gulimall.product.dao.AttrGroupDao;
import com.zsj.gulimall.product.dao.CategoryDao;
import com.zsj.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.zsj.gulimall.product.entity.AttrGroupEntity;
import com.zsj.gulimall.product.entity.CategoryEntity;
import com.zsj.gulimall.product.service.CategoryService;
import com.zsj.gulimall.product.vo.AttrRespVo;
import com.zsj.gulimall.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.gulimall.product.dao.AttrDao;
import com.zsj.gulimall.product.entity.AttrEntity;
import com.zsj.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.Utilities;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {


    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional  //也是一个事务
    @Override
    public void saveAttr(AttrVo attr) {
        System.out.print("AttrVo attr:");
        System.out.println(attr);
        AttrEntity attrEntity = new AttrEntity();
        //attrEntity.setAttrName(attr.getAttrName()); //TODO 这个方法大量反射
        BeanUtils.copyProperties(attr,attrEntity);
        //1.保存属性的基本数据
        this.save(attrEntity);      //注意了注意了！！马勒戈壁，因为前端传来的AttrVo attr里并没有AttrId因为AttrId是表自增的，attrEntity存储到表里面之后attr表才有AttrId，我本来想用前端再发一次请求来获取，大错特错，后端直接处理不就行了
        //save之后attrEntity的attrId在内部处理之后自动加上了
        System.out.print("attrEntity:");
        System.out.println(attrEntity);
        //2.保存属性和属性小组的基本数据
        if (attr.getAttrType()== ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()&&attr.getAttrGroupId()!=null) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            System.out.println("AttrServiceImpl->public void saveAttr(AttrVo attr)->attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());之前打印了");
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            System.out.println("AttrServiceImpl->public void saveAttr(AttrVo attr)->attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());之后打印了");
            System.out.println("AttrServiceImpl->public void saveAttr(AttrVo attr)->attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());之前打印了");
            System.out.print("attr.getAttrId():");
            System.out.println(attr.getAttrId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            System.out.println("AttrServiceImpl->public void saveAttr(AttrVo attr)->attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());之后打印了");
            System.out.println("AttrServiceImpl->public void saveAttr(AttrVo attr)->relationDao.insert(attrAttrgroupRelationEntity);之前打印了");
            relationDao.insert(attrAttrgroupRelationEntity);
            System.out.println("AttrServiceImpl->public void saveAttr(AttrVo attr)->relationDao.insert(attrAttrgroupRelationEntity);之后打印了");
        }
        else if (attr.getAttrType()== ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()&&attr.getAttrGroupId()==null)
        {
            //TODO 要解决这个异常处理 差不多需要抛个异常来回滚事务 2022/6/19 5：19分 还不会处理
            System.out.println("fuck！！！！！！！！！！！！！！！！基本属性分组id不能为空");
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(null);
            attrAttrgroupRelationEntity.setAttrId(null);
            relationDao.insert(attrAttrgroupRelationEntity);
        }

    }

    @Override           //2022/6/17 23:25写完 查询规格参数的管理页面  //显示列表
    public PageUtils queryBaseOrSaleAttrPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> QueryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type","base".equalsIgnoreCase(type)?ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()); //1规格参数 2销售属性;//响应主体数据查询条件
        if (catelogId!=0) {
            QueryWrapper.eq("catelog_id",catelogId);
        }
        String key = (String)params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            QueryWrapper.and((wrapper)->{
               wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),   //把分页请求的数据塞进去get得到一个响应分页数据
                QueryWrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();  //为什么不能pageUtils.getList()>
        // 因为PageUtils pageUtils = new PageUtils(page); 还没运行，所以不知道pageUtils的泛型类型具体是什么类型，所以list属性的类型还不知道
        List<Object> respVos = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //1.给相应对象设置"groupName": "主体", //所属分组名
            if ("base".equalsIgnoreCase(type)) {        //如果是规格参数才给响应对象设置分组名
                AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attrAttrgroupRelationEntity != null&&attrAttrgroupRelationEntity.getAttrGroupId()!=null) {       //第二个与怕这个属性根本就他妈没关联什么分组，怕在关联表查不出来，很重要
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }

            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {  //怕查不出来，很重要
                attrRespVo.setCatelogName(categoryEntity.getName());  //给相应对象设置分类名
            }
            return attrRespVo;   //每一个映射搞到attrResoVo然后返回
        }).collect(Collectors.toList());
        pageUtils.setList(respVos);
        return pageUtils;

    }

    @Override        //回显接口调用        //获取信息好像不用事务?
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,attrRespVo);
        //获取设置分组信息  //先判断是不是基本属性（规格参数）
        if (attrEntity.getAttrType()== ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity attrgroupRelationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (attrgroupRelationEntity!=null) {

                attrRespVo.setAttrGroupId(attrgroupRelationEntity.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupRelationEntity.getAttrGroupId());
                if (attrGroupEntity!=null) {
                    //设置分组名字完毕
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
                else
                {
                    System.out.println("你他妈这个属性分组关联表里的"+attrgroupRelationEntity.getAttrGroupId()+"分组id在分组表没有");
                }
            }
            else
            {
                System.out.println("你他妈这个"+attrEntity.getAttrName()+"属性在属性关联表没有");
            }
        }

        //2获取设置分类路径信息

        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        if (catelogPath!=null) {
            attrRespVo.setCatelogPath(catelogPath);
            CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
            attrRespVo.setCatelogName(categoryEntity.getName());
        }
        else
        {
            System.out.println("Fuck!你的分类id是尼玛假的，这个id找不到对应的分类名字和三级分类路径");
        }

        return attrRespVo;
    }

    @Transactional
    @Override
    public void updateAttr(AttrRespVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        //attrEntity.setAttrName(attr.getAttrName());
        BeanUtils.copyProperties(attr,attrEntity);
        //1.修改属性的基本数据
        this.updateById(attrEntity);

        //2.更新修改属性分组关联表的基本数据 前提是要基础规格参数属性
        if (attrEntity.getAttrType()== ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());
            //AttrAttrgroupRelationEntity RelationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            Integer count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            if (count>0) { //attrId在属性分组关联表中有相应记录就更新
                relationDao.update(attrAttrgroupRelationEntity,new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));
            }
            else
            {
                //attrId在属性分组关联表中没有相应记录就新增
                relationDao.insert(attrAttrgroupRelationEntity);
            }
        }

    }

    @Override   //查找分组关联的属性，前端为关联键
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        if (attrAttrgroupRelationEntityList!=null) {
            List<Long> longList = attrAttrgroupRelationEntityList.stream().map((entity) -> {
                return entity.getAttrId();
            }).collect(Collectors.toList());
            List<AttrEntity> attrEntities = this.listByIds(longList);
            return attrEntities;
        }
        else {
            return null;
        }
    }

    @Override       //获取分组所属分类里没有关联别的分组的属性       //成就感满满 2022/6/19 3:40
    public PageUtils getNoRelationAttr(Long attrgroupId, Map<String, Object> params) {
                    //1 用分组Id从分组表里找到此分组对应的分类id
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectOne(new QueryWrapper<AttrGroupEntity>().eq("attr_group_id", attrgroupId));
        Long catelogId = attrGroupEntity.getCatelogId();
        if (catelogId!=null) {                   //此分组没关联分类Id那玩个屁，因为属性都是在分类里的
            //2.1 属性分组关联关系表所有的属性id
            List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().gt("id", 0));
            QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<AttrEntity>();
            if (attrAttrgroupRelationEntityList!=null) {
                List<Long> attrIdInRelationList = attrAttrgroupRelationEntityList.stream().map((item) -> {
                    Long attrId = item.getAttrId();
                    return attrId;
                }).collect(Collectors.toList());
                //!!!!!!!!!还要属性类别要为基本规格参数属性
                attrEntityQueryWrapper.eq("catelog_id", catelogId).eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()).notIn("attr_id", attrIdInRelationList);
            }
            else
            {
                attrEntityQueryWrapper.eq("catelog_id", catelogId).eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
            }


            //2.2 用分类Id和"不在属性分类关联表里的属性集合的属性"在属性表中找出此分类id下没有关联别的分组的属性

            String key = (String)params.get("key");
            if (StringUtils.isNotEmpty(key)) {
                attrEntityQueryWrapper.eq("attr_id",key).or().like("attr_name",key);
            }
            List<AttrEntity> attrEntityList = baseMapper.selectList(attrEntityQueryWrapper);
            //看了page IPage Query  PageUtils 的源码搞明白这些的关系才写出粗来的 2022/6/19 2：15
            /*IPage<AttrEntity> page = new Query<AttrEntity>().getPage(params);
            page.setRecords(attrEntityList);*/
            IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), attrEntityQueryWrapper); //这个是一步到位的方法
            PageUtils pageUtils = new PageUtils(page);
            return pageUtils;

        }




        return null;
    }

}