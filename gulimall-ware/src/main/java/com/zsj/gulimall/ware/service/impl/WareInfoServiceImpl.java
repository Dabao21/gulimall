package com.zsj.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.gulimall.ware.dao.WareInfoDao;
import com.zsj.gulimall.ware.entity.WareInfoEntity;
import com.zsj.gulimall.ware.service.WareInfoService;
import org.springframework.util.StringUtils;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    /**
    *@date 2022/6/21 16:55
    *@Author Dabao
    * 1.
    * 2.
    **/
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareInfoEntity> wareInfoEntityQueryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");

        if (!StringUtils.isEmpty(key)) {
            wareInfoEntityQueryWrapper.eq("id",key).or().like("name",key).or().like("address",key).or().like("areacode",key);

        }

        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                wareInfoEntityQueryWrapper
        );

        return new PageUtils(page);
    }

}