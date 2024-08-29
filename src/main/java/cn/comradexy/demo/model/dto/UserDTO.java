package cn.comradexy.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO demo
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: DTO demo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String password;
    private String email;
}
