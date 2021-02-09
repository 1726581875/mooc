package cn.edu.lingnan.authorize.dao;

import cn.edu.lingnan.authorize.model.MoocManager;
import cn.edu.lingnan.authorize.model.MoocUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiFunction;

/**
 * @author xmz
 * @date: 2021/02/09
 */
@Repository
public class UserDAO extends BaseDAO {

    private Logger log = LoggerFactory.getLogger(ManagerDAO.class);

    private class  ManagerMapper implements RowMapper<MoocUser> {

        @Override
        public MoocUser mapRow(ResultSet resultSet, int i) throws SQLException {
            return new MoocUser().setId(resultSet.getInt("id"))
                    .setName(resultSet.getString("name"))
                    .setAccount(resultSet.getString("account"))
                    .setPassword(resultSet.getString("password"))
                    .setUserType(resultSet.getString("user_type"))
                    .setStatus(resultSet.getInt("status"));
        }
    }

    /**
     * 根据账号查找管理员
     * @param account
     * @return
     */
    public MoocUser findUserByAccount(String account){

        String sql = "select * from mooc_user where account = ?";
        MoocUser moocUser = null;
        try {
            moocUser = jdbcTemplate.queryForObject(sql, new Object[]{account}, new ManagerMapper());
        }catch (Exception e){
            log.warn("数据库查询该账户失败，可能该用户不存在，msg={}",e.getMessage());
        }
        return moocUser;
    }

    /**
     * 插入或者更新管理员
     * @param user
     */
    public void save(MoocUser user){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO mooc_user(name,account,password,status,user_type) ");
        sql.append("VALUES ");
        sql.append("(?,?,?,?,?) ");
        sql.append("ON DUPLICATE KEY ");
        sql.append("UPDATE password=?,status=? ");
        jdbcTemplate.update(sql.toString(),user.getName(),user.getAccount(),user.getPassword(),user.getStatus()
                ,user.getPassword(),user.getStatus(),user.getUserType());
    }

}
