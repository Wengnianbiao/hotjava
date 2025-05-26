package com.biao.job.quartzjob.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * 数据库源配置
 */
@Configuration
@MapperScan(basePackages = "com.biao.job.mapper", sqlSessionFactoryRef = "dataSqlSessionFactory")
public class DataSourceConfig {

    @Bean(name = "datasource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 返回data数据库的会话工厂
     */
    @Bean(name = "dataSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("datasource") DataSource ds) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(ds);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath*:/mapper/**/*.xml"));
        return bean.getObject();
    }


    /**
     * 返回data数据库的事务
     */
    @Bean(name = "dataTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("datasource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }
}
