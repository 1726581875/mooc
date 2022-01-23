package cn.edu.lingnan;

import java.sql.SQLException;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2021/12/29
 */
public interface BaseDAO {

    /**
     * 获取查询数据结果列表
     * @param sql
     * @param type
     * @param <T>
     * @return
     */
    <T> List<T> getList(String sql, Class<T> type);

    /**
     * 获取查询结果
     * @param sql
     * @param type
     * @param <T>
     * @return
     */
    <T> T getOne(String sql, Class<T> type);

    /**
     * 插入单条数据
     * @param obj
     * @param <T>
     * @return
     */
    <T> int insert(T obj);

    /**
     *
     * @param objectList
     * @param <T>
     * @return
     */
    <T> int batchInsert(List<T> objectList) throws SQLException;

    /**
     * 批量删除
     * @param ids
     * @param <T>
     * @return
     */
    <T> int batchDeleteByIds(List<T> ids);

    /**
     *
     * @param type
     * @param <T>
     */
    <T> void createTable(Class<T> type);


}
