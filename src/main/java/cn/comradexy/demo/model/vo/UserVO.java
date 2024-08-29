package cn.comradexy.demo.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * VO demo
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: VO demo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVO {
    private String username;
    private String name;
    private String password;
    private String email;
}
