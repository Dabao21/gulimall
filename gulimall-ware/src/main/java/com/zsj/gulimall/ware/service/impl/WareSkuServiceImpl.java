package com.zsj.gulimall.ware.service.impl;

import com.zsj.common.utils.R;
import com.zsj.gulimall.ware.feig.ProductFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.gulimall.ware.dao.WareSkuDao;
import com.zsj.gulimall.ware.entity.WareSkuEntity;
import com.zsj.gulimall.ware.service.WareSkuService;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    WareSkuDao wareSkuDao;

    @Autowired
    ProductFeignService productFeignService;
    /**
    *@date 2022/6/21 17:01
    *@Author Dabao
    * 1.
    * 2.
    **/
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /**
         *@date 2022/6/21 17:01
         *@Author Dabao
         * 1.skuId
         * 2.wareId
         * 3.ware_sku表
         **/
        QueryWrapper<WareSkuEntity> wareSkuEntityQueryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)) {
            wareSkuEntityQueryWrapper.eq("sku_id",skuId);
        }

        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(skuId)) {
            wareSkuEntityQueryWrapper.eq("ware_id",wareId);
        }

        IPage<WareSkuEntity> page = this.page(


                new Query<WareSkuEntity>().getPage(params),
                wareSkuEntityQueryWrapper

        );

        return new PageUtils(page);
    }


    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        QueryWrapper<WareSkuEntity> QueryWrapper = new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId);
        WareSkuEntity wareSkuEntity = wareSkuDao.selectOne(QueryWrapper);
        //  2022/6/22 6:30         判断库存是新增还是更新
        if (wareSkuEntity!=null) {
            wareSkuDao.addStock(skuId,wareId,skuNum);
        }else {
            WareSkuEntity wareSkuEntity1 = new WareSkuEntity();
            wareSkuEntity1.setSkuId(skuId);
            wareSkuEntity1.setWareId(wareId);
            wareSkuEntity1.setStock(skuNum);
            wareSkuEntity1.setStockLocked(0);
            //远程查询sku的名字  //  2022/6/22 7:09     有异常则异常处理不影响事务,无需回滚
            //TODO  2022/6/22 7:11         还有什么方法可以让异常出现后不回滚呢？高级部分跟大家说
            try{
                R info = productFeignService.info(skuId);
                if (info.getcode()==0) {
                    Map<String,Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
                    wareSkuEntity1.setSkuName((String) skuInfo.get("skuName"));
                }
            }catch (Exception e){

            }
            wareSkuDao.insert(wareSkuEntity1);
        }

    }

}