package com.sumslack.test.conf;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;

@Configuration
@MapperScan("com.sumslack.test.mapper*")
public class MybatisPlusConfig {
	
	@Bean
	@Profile({ "dev", "test" })
	public PerformanceInterceptor performanceInterceptor() {
		PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
		performanceInterceptor.setFormat(false);
		performanceInterceptor.setMaxTime(500);
		return performanceInterceptor;
	}

	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}
	
	
    @Bean(name="dataSource")
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource(){
        return new DruidDataSource();
    } 
    
//    @Bean(name="dataSource2")
//    @ConfigurationProperties(prefix="spring.datasource2")
//    public DataSource dataSource2(){
//        return new DruidDataSource();
//    }
 
    
    @Bean(name="transactionManager") 
    public DataSourceTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }
}
