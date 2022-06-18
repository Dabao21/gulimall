package com.zsj.gulimall.product;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.zsj.gulimall.product.entity.BrandEntity;
import com.zsj.gulimall.product.service.BrandService;
import com.zsj.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

@Slf4j
@SpringBootTest
class GulimallProductApplicationTests {


    @Autowired
    CategoryService categoryService;

    @Autowired
    BrandService brandService;
    @Test
    void contextLoads() {

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setDescript("hello");
        brandEntity.setName("华为");
        brandEntity.setLogo("fafsafas");
        brandService.save(brandEntity);
        System.out.println("保存成功");



    }

    @Test
    public void testFindPath()
    {
        Long[] catelogPath = categoryService.findCatelogPath(249L);
        log.info("完整路径：{}", Arrays.asList(catelogPath) );

    }


}
