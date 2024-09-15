package cn.comradexy.demo.service;

import cn.comradexy.demo.mapper.ServeAccessMapper;
import cn.comradexy.demo.model.domain.Serve;
import cn.comradexy.demo.model.domain.ServeAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @Author: ComradeXY
 * @CreateTime: 2024-09-12
 * @Description:
 */
@Service
public class ServeAccessService implements IServeAccessService {
    private static final long ACCESS_TIME_UPDATE_THRESHOLD = 3;

    private final Logger logger = LoggerFactory.getLogger(ServeAccessService.class);

    @Resource
    private ServeAccessMapper serveAccessMapper;

    /**
     * 访问一次服务
     *
     * @param serveAccess 服务访问记录
     */
    private void accessServeOnce(ServeAccess serveAccess) {
        // 访问次数+1，如果访问次数超过 threshold，则更新访问时间
        // LRU算法“缓存污染”的问题
        serveAccess.setAccessCount(serveAccess.getAccessCount() + 1);
        if (serveAccess.getAccessCount() >= ACCESS_TIME_UPDATE_THRESHOLD) {
            serveAccess.setLastAccessTime(LocalDateTime.now());
            serveAccess.setAccessCount(0L);
        }
    }

    @Override
    public void recordAccess(Serve serve) {
        // 查询记录是否存在
        ServeAccess serveAccess = serveAccessMapper.selectById(serve.getId());
        if (null != serveAccess) {
            // 如果存在，则更新访问记录
            accessServeOnce(serveAccess);
            serveAccessMapper.update(serveAccess);
            logger.info("更新服务访问记录: {}", serveAccess);
        } else {
            // 如果不存在，则插入新记录
            serveAccess = ServeAccess.builder()
                    .id(serve.getId())
                    .accessCount(1L)
                    .lastAccessTime(LocalDateTime.now())
                    .build();
            serveAccessMapper.insert(serveAccess);
        }
        logger.info("插入服务访问记录: {}", serveAccess);
    }

}
