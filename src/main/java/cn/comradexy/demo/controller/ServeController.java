package cn.comradexy.demo.controller;

import cn.comradexy.demo.model.Result;
import cn.comradexy.demo.service.IServeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/jzo2o-demo")
public class ServeController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IServeService serveService;

    @GetMapping("/getServeById/{id}")
    public Result<?> getServeById(@PathVariable("id") Long id) {
        logger.info("getServeById: {}", id);
        return Result.success(serveService.getServeById(id));
    }

}
