package top.lrshuai.limit.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Slf4j
public class AopUtil {

    private static final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 解析SpEL表达式
     */
    public static String parseSpel(String expression, ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            StandardEvaluationContext context = new StandardEvaluationContext();

            // 设置方法参数
            String[] parameterNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }

            Expression expr = parser.parseExpression(expression);
            return expr.getValue(context, String.class);
        } catch (Exception e) {
            log.warn("解析SpEL表达式失败: {}", expression, e);
            return expression;
        }
    }
}
