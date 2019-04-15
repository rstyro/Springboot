package top.lrshuai.limit.annotation;

import java.lang.annotation.*;

/**
 * 请求限制的自定义注解
 *
 * @Inherited 指示批注类型是自动继承的。如果在注释类型声明中存在继承的元注释，并且用户在类声明上查询注释类型，并且类声明对该类没有注释，那么该类的超类将自动被查询到注释类型。
 * 这个过程将被重复，直到找到这个类型的注释，或者到达类层次结构(对象)的顶端。如果没有超类具有此类的注释，那么查询将表明该类没有此类注释
 *
 * @Target 注解可修饰的对象范围，ElementType.METHOD 作用于方法，ElementType.TYPE 作用于类
 * (ElementType)取值有：
 * 　　　　1.CONSTRUCTOR:用于描述构造器
 * 　　　　2.FIELD:用于描述域
 * 　　　　3.LOCAL_VARIABLE:用于描述局部变量
 * 　　　　4.METHOD:用于描述方法
 * 　　　　5.PACKAGE:用于描述包
 * 　　　　6.PARAMETER:用于描述参数
 * 　　　　7.TYPE:用于描述类、接口(包括注解类型) 或enum声明
 * @Retention定义了该Annotation被保留的时间长短：某些Annotation仅出现在源代码中，而被编译器丢弃；
 * 而另一些却被编译在class文件中；编译在class文件中的Annotation可能会被虚拟机忽略，
 * 而另一些在class被装载时将被读取（请注意并不影响class的执行，因为Annotation与class在使用上是被分离的）。
 * 使用这个meta-Annotation可以对 Annotation的“生命周期”限制。
 * （RetentionPoicy）取值有：
 * 　　　　1.SOURCE:在源文件中有效（即源文件保留）
 * 　　　　2.CLASS:在class文件中有效（即class保留）
 * 　　　　3.RUNTIME:在运行时有效（即运行时保留）
 *
 * @Inherited
 * 元注解是一个标记注解，@Inherited阐述了某个被标注的类型是被继承的。
 * 如果一个使用了@Inherited修饰的annotation类型被用于一个class，则这个annotation将被用于该class的子类。
 */
@Documented
@Inherited
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {
    // 在 second 秒内，只能请求 count 次
    int second() default 1;
    int count() default 1;
}
