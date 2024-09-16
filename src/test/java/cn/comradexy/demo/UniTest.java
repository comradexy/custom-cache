package cn.comradexy.demo;

import org.junit.jupiter.api.Test;

/**
 * 单元测试
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-09-17
 * @Description: 单元测试
 */
public class UniTest {

    @Test
    public void test() {
        long id = 1L;
        Object result = id;

        System.out.println(result instanceof Long);
    }
}
