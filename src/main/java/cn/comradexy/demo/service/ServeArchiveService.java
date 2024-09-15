package cn.comradexy.demo.service;

import cn.comradexy.demo.mapper.ServeArchiveMapper;
import cn.comradexy.demo.model.domain.ServeAccess;
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
        ServeAccess serveAccess = serveArchiveMapper.selectById(serveId);
        if (serveAccess == null) return false;

        // 访问次数+1，如果访问次数超过3次，则更新访问时间
        serveAccess.setAccessCount(serveAccess.getAccessCount() + 1);
        if (serveAccess.getAccessCount() >= 3) {
            serveAccess.setLastAccessTime(LocalDateTime.now());
            serveAccess.setAccessCount(0L);
        }

        serveArchiveMapper.update(serveAccess);

        return true;
    }
}
