package cn.comradexy.demo.service;

/**
 * @Author: ComradeXY
 * @CreateTime: 2024-09-12
 * @Description:
 */
public interface IServeAccessService {
    /**
     * 记录访问
     *
     * @param serveId 服务ID
     */
    void recordAccess(long serveId);
}
