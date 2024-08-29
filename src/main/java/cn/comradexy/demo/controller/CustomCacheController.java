package cn.comradexy.demo.controller;

import cn.comradexy.demo.model.Result;
import cn.comradexy.demo.service.ICustomCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * controller demo
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: controller demo
 */
@RestController
@RequestMapping("/customCache")
public class CustomCacheController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ICustomCacheService customCacheService;

    @GetMapping("/getUserByUserName")
    public Result<?> getUserByUserName(@RequestParam("userName") String userName) {
        logger.info("getUserByUserName: {}", userName);
        return Result.success(customCacheService.getUserByUserName(userName));
    }

    @GetMapping("/getUserByName")
    public Result<?> getUserByName(@RequestParam("name") String name) {
        logger.info("getUserByName: {}", name);
        return Result.success(customCacheService.getUserByName(name));
    }

    @GetMapping("/listUser")
    public Result<?> listUser() {
        logger.info("listUser");
        return Result.success(customCacheService.listUser());
    }
}
