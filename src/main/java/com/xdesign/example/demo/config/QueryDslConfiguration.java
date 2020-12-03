package com.xdesign.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.querydsl.binding.QuerydslPredicateBuilder;

@Configuration
public class QueryDslConfiguration {
    @Bean
    public QuerydslPredicateBuilder querydslPredicateBuilder(QuerydslBindingsFactory querydslBindingsFactory) {
        return new QuerydslPredicateBuilder(new DefaultConversionService(), querydslBindingsFactory.getEntityPathResolver());
    }
}
