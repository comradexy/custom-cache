package cn.comradexy.demo.service;

import cn.comradexy.demo.model.domain.Serve;

/**
 * service demo
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: service demo
 */
public interface IServeService {
    /**
     * 根据id查询服务
     *
     * @param id 服务id
     * @return 服务
     */
    Serve getServeById(Long id);

    /**
     * 插入服务
     *
     * @param serve 服务
     */
    void insertServe(Serve serve);
}
