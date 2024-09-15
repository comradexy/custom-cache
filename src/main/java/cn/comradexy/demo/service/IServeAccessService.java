package cn.comradexy.demo.service;

import cn.comradexy.demo.model.domain.Serve;

/**
 * @Author: ComradeXY
 * @CreateTime: 2024-09-12
 * @Description:
 */
public interface IServeAccessService {
    /**
     * 记录访问
     *
     * @param serve 服务
     */
    void recordAccess(Serve serve);
}
