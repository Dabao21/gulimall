package com.zsj.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*
*@date 2022/7/11 18:33
*@Author Dabao
* 1.导入依赖
* 2.编写配置,参照官方文档,给容器中注入一个RestHighLevelClient
**/
@Configuration
public class GulimallElasticSearchConfig {

    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
/*        builder.addHeader("Authorization", "Bearer " + TOKEN);
        builder.setHttpAsyncResponseConsumerFactory(
                new Ht tpAsyncResponseConsumerFactory
                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));*/
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("121.5.141.206", 9200, "http")));
        return client;
    }
}
