package cn.edu.lingnan.mooc.dao;

import cn.edu.lingnan.mooc.model.MoocManager;
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

    public MoocManager findManagerByAccount(String account){

        String sql = "select * from mooc_manager where account = ?";
        MoocManager moocManager = null;
       try {
           moocManager = jdbcTemplate.queryForObject(sql, new Object[]{account}, new ManagerMapper());
        }catch (Exception e){
           log.warn("数据库查询该账户失败，可能该用户不存在，msg={}",e.getMessage());
       }
        return moocManager;
    }




}
