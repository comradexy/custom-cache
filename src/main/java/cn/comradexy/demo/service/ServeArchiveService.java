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


        return true;
    }
}
