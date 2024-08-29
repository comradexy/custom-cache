package cn.comradexy.demo.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DO demo
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: DO demo
 */
@Data
@AllArgsConstructor
@Builder
public class UserDO {
    private Long id;
    private String username;
    private String name;
    private String password;
    private String email;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
