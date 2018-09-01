package top.lrshuai.SpringBootmultisource.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import top.lrshuai.SpringBootmultisource.config.DatabaseContextHolder;
import top.lrshuai.SpringBootmultisource.config.DatabaseType;
import top.lrshuai.SpringBootmultisource.service.impl.UserServiceImpl2;

/**
 * 
 * @author rstyro
 * 对数据源进行切片
 */
@Aspect
@Component	
@Order(-1)	//保证该AOP在@transactional之前执行
public class DataSourceAspect {

	 /**
     * 使用空方法定义切点表达式
     */
    @Pointcut("execution(* top.lrshuai.SpringBootmultisource.service..*.*(..))")
    public void declareJointPointExpression() {
    }

    @Before("declareJointPointExpression()")
    public void setDataSourceKey(JoinPoint point){
        //根据连接点所属的类实例，动态切换数据源
    	System.out.println("point.getTarget()===="+point.getTarget());
        if (point.getTarget() instanceof UserServiceImpl2) {
            DatabaseContextHolder.setDatabaseType(DatabaseType.SECONDDB);
        } else {
            DatabaseContextHolder.setDatabaseType(DatabaseType.FIRSTDB);
        }
    }
}
