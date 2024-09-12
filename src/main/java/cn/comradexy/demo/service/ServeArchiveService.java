package cn.comradexy.demo.service;

import cn.comradexy.demo.mapper.ServeArchiveMapper;
import cn.comradexy.demo.model.domain.ServeArchive;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @Author: ComradeXY
 * @CreateTime: 2024-09-12
 * @Description:
 */
@Service
public class ServeArchiveService implements IServeArchiveService {
    @Resource
    ServeArchiveMapper serveArchiveMapper;

    @Override
    public boolean recordServeAccess(Long serveId) {
        ServeArchive serveArchive = serveArchiveMapper.selectById(serveId);
        if (serveArchive == null) return false;

        // 访问次数+1，如果访问次数超过3次，则更新访问时间
        serveArchive.setAccessCount(serveArchive.getAccessCount() + 1);
        if (serveArchive.getAccessCount() >= 3) {
            serveArchive.setLastAccessTime(LocalDateTime.now());
            serveArchive.setAccessCount(0L);
        }

        serveArchiveMapper.update(serveArchive);

        return true;
    }
}
