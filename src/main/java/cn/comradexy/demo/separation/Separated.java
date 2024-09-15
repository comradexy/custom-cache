package cn.comradexy.demo.separation;

import java.lang.annotation.*;

/**
 * 冷热分离
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-09-02
 * @Description: 冷热分离，加在Mapper上
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Separated {
    OperateType operateType();
}
