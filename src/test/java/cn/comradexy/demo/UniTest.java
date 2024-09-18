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
    private Object result = null;

    @Test
    public void test() {
        test2();
    }

    private void test2() {
        long id = 1L;
        result = id;
        test1();
        System.out.println("test2: " + result);
    }

    private Object test1() {
        try {
            result = 2L;
            return result;
        } finally {
            result = 3L;
            System.out.println("test1: " + result);
        }
    }
}
