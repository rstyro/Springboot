package top.lrshuai.SpringBootmultisource.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
public class DataSourceConfig implements EnvironmentAware{
	@Autowired
	private Environment env;
	
	@Override
	public void setEnvironment(Environment env) {
		 this.env = env;		
	}
	
	/**
     * 第一个数据源
     */
    @Bean
    public DataSource firstDataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", env.getProperty("datasource.first.driverClassName"));
        props.put("url", env.getProperty("datasource.first.url"));
        props.put("username", env.getProperty("datasource.first.username"));
        props.put("password", env.getProperty("datasource.first.password"));
        setProps(props);
        return DruidDataSourceFactory.createDataSource(props);
    }

    /**
     * 第二个数据源
     * @return
     * @throws Exception
     */
    @Bean
    public DataSource secondDataSource() throws Exception {
    	Properties props = new Properties();
        props.put("driverClassName", env.getProperty("datasource.second.driverClassName"));
        props.put("url", env.getProperty("datasource.second.url"));
        props.put("username", env.getProperty("datasource.second.username"));
        props.put("password", env.getProperty("datasource.second.password"));
        setProps(props);
        return DruidDataSourceFactory.createDataSource(props);
    }

    /**
     * 设置其他属性
     * @param props
     */
    public void setProps(Properties props){
		props.put("initialSize",env.getProperty("spring.datasource.initialSize"));  
	    props.put("minIdle",env.getProperty("spring.datasource.minIdle"));  
	    props.put("maxActive",env.getProperty("spring.datasource.maxActive"));  
	    props.put("maxWait",env.getProperty("spring.datasource.maxWait"));  
  		props.put("timeBetweenEvictionRunsMillis",env.getProperty("spring.datasource.timeBetweenEvictionRunsMillis"));  
  		props.put("minEvictableIdleTimeMillis",env.getProperty("spring.datasource.minEvictableIdleTimeMillis"));  
  		props.put("validationQuery",env.getProperty("spring.datasource.validationQuery"));  
  		props.put("testWhileIdle",env.getProperty("spring.datasource.testWhileIdle"));  
  		props.put("testOnBorrow",env.getProperty("spring.datasource.testOnBorrow"));  
  		props.put("testOnReturn",env.getProperty("spring.datasource.testOnReturn"));  
  		props.put("poolPreparedStatements",env.getProperty("spring.datasource.poolPreparedStatements"));  
  		props.put("maxPoolPreparedStatementPerConnectionSize",env.getProperty("spring.datasource.maxPoolPreparedStatementPerConnectionSize"));  
  		props.put("filters",env.getProperty("spring.datasource.filters"));  
  		props.put("connectionProperties",env.getProperty("spring.datasource.connectionProperties")); 
    }
    
    /**
     * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
     * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
     */
    @Bean
    @Primary
    public DynamicDataSource dataSource(@Qualifier("firstDataSource") DataSource firstDataSource,
            							@Qualifier("secondDataSource") DataSource secondDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DatabaseType.FIRSTDB, firstDataSource);
        targetDataSources.put(DatabaseType.SECONDDB, secondDataSource);

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
        dataSource.setDefaultTargetDataSource(firstDataSource);// 默认的datasource设置为firstDataSource

        return dataSource;
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource ds) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(ds);// 指定数据源(这个必须有，否则报错)
        fb.setConfigLocation(new DefaultResourceLoader().getResource("classpath:mybatis/mybatis-config.xml"));
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/**/*.xml"));
        return fb.getObject();
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("allow", "127.0.0.1,192.168.1.83"); //白名单
        reg.addInitParameter("deny",""); //黑名单
        reg.addInitParameter("loginUsername", "admin");//查看监控的用户名
        reg.addInitParameter("loginPassword", "nimda");//密码
        return reg;
    }

    @Bean public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
