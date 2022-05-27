package com.zsj.gulimall.member;



import feign.Logger;
import feign.codec.Decoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class GulimallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallMemberApplication.class, args);
    }



        @Bean
        Logger.Level feignLoggerLevel() {
            return Logger.Level.FULL;
        }


        @Bean
        public Decoder feignDecoder() {
            return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
        }

        public ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
            final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(new PhpMappingJackson2HttpMessageConverter());
            return new ObjectFactory<HttpMessageConverters>() {
                @Override
                public HttpMessageConverters getObject() throws BeansException {
                    return httpMessageConverters;
                }
            };
        }

        public class PhpMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
            PhpMappingJackson2HttpMessageConverter(){
                List<MediaType> mediaTypes = new ArrayList<>();
                mediaTypes.add(MediaType.valueOf(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8")); //关键
                setSupportedMediaTypes(mediaTypes);
            }
        }

}
