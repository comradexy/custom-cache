package cn.comradexy.demo.separation;

/**
 * 操作类型
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-09-12
 * @Description: 操作类型
 */
public enum OperateType {
    /**
     * 单条查询
     */
    SELECT_ONE,

    /**
     * 批量查询
     */
    SELECT_BATCH,

    /**
     * 单条查询，并锁定
     */
    SELECT_ONE_FOR_UPDATE,

    /**
     * 批量查询，并锁定
     */
    SELECT_BATCH_FOR_UPDATE,

    /**
     * 统计
     */
    COUNT,

    /**
     * 插入
     */
    INSERT,

    /**
     * 更新
     */
    UPDATE,

    /**
     * 删除
     */
    DELETE
}
