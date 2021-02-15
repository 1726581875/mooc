package cn.edu.lingnan.authorize.dao;

import cn.edu.lingnan.authorize.model.MoocManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiFunction;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@Repository
public class ManagerDAO extends BaseDAO {

    private Logger log = LoggerFactory.getLogger(ManagerDAO.class);

    private class  ManagerMapper implements RowMapper<MoocManager>{

        @Override
        public MoocManager mapRow(ResultSet resultSet, int i) throws SQLException {
            return MoocManager.build().setId(resultSet.getLong("id"))
                    .setName(resultSet.getString("name"))
                    .setAccount(resultSet.getString("account"))
                    .setPassword(resultSet.getString("password"))
                    .setStatus(resultSet.getInt("status"));
        }
    }

    // 函数式版
    BiFunction<ResultSet, Integer, MoocManager> managerMapRow = (resultSet, i) -> {
        try {
          return  MoocManager.build().setId(resultSet.getLong("id"))
                    .setName(resultSet.getString("name"))
                    .setAccount(resultSet.getString("account"))
                    .setPassword(resultSet.getString("password"))
                    .setStatus(resultSet.getInt("status"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    };

    /**
     * 根据账号查找管理员
     * @param account
     * @return
     */
    public MoocManager findManagerByAccount(String account){

        String sql = "select * from mooc_manager where account = ?";
        MoocManager moocManager = null;
       try {
           moocManager = jdbcTemplate.queryForObject(sql, new Object[]{account}, new ManagerMapper());
        }catch (Exception e){
           log.warn("根据账号户查询失败，account={}，msg={}",account, e.getMessage());
       }
        return moocManager;
    }

    /**
     * 插入或者更新管理员
     * @param manager
     */
    public void save(MoocManager manager){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO mooc_manager(name,account,password,status) ");
        sql.append("VALUES ");
        sql.append("(?,?,?,?) ");
        sql.append("ON DUPLICATE KEY ");
        sql.append("UPDATE password=?,status=? ");
        jdbcTemplate.update(sql.toString(),manager.getName(),manager.getAccount(),manager.getPassword(),manager.getStatus()
                ,manager.getPassword(),manager.getStatus());
    }

    /**
     * 根据id查找
     * @param managerId
     * @return
     */
    public MoocManager findById(Integer managerId){
        String sql = "select * from mooc_manager where id = ?";
        MoocManager moocManager = null;
        try {
            moocManager = jdbcTemplate.queryForObject(sql, new Object[]{managerId}, new ManagerMapper());
        }catch (Exception e){
            log.warn("数据库查询该账户失败，id={}，msg={}",managerId,e.getMessage());
        }
        return moocManager;
    }


}
