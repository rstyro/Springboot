package top.lrshuai.validator.annotation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义验证器
 */
public class MatchValidator implements ConstraintValidator<MatchValue, String> {

    /**
     * 接收注解传过来的值
     */
    private Set<String> enumName=new HashSet<>();

    /**
     * 初始化方法，可以获取注解的参数信息
     */
    @Override
    public void initialize(MatchValue matchValue) {
        try {
            String[] values = matchValue.values();
            if(values.length>0){
                for (String item:values){
                    enumName.add(item);
                }
            }
            Class<?extends Enum>[] enums = matchValue.enums();
            if(enums.length>0){
                Enum[] enumConstants = Arrays.stream(enums).findFirst().get().getEnumConstants();
                for (Enum e:enumConstants){
                    enumName.add(e.name());
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
        }

    }

    /**
     * 校验值
     * @param value 参数的值信息
     * @param context 上下文对象，可以禁用默认提示模板，然后更改提示模板
     * @return boolean
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!StringUtils.isEmpty(value)){
            for(String name:enumName){
                if(name.equals(value)){
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
