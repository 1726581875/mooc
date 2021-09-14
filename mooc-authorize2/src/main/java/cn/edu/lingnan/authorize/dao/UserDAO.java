package cn.edu.lingnan.authorize.dao;

import cn.edu.lingnan.authorize.model.entity.MoocUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
            return new MoocUser().setId(resultSet.getLong("id"))
                    .setName(resultSet.getString("name"))
                    .setAccount(resultSet.getString("account"))
                    .setPassword(resultSet.getString("password"))
                    .setUserType(resultSet.getString("user_type"))
                    .setStatus(resultSet.getInt("status"))
                    .setUserImage(resultSet.getString("user_image"));
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
            log.warn("数据库查询该账户失败，可能该用户不存在,account={}，msg={}",account,e.getMessage());
        }
        return moocUser;
    }


    /**
     * 根据账号查找id
     * @param accountList
     * @return
     */
    public List<Integer> findUserIdByAccountList(List<String> accountList){

        List<Integer> idList = new ArrayList<>();
        String sql = "select id from mooc_user where account in ("
                + accountList.stream().map(e->"'" + e + "'").collect(Collectors.joining(",")) + ")";
        try {
            idList = jdbcTemplate.queryForList(sql, Integer.class);
        }catch (Exception e){
            log.warn("==根据账号查询数据库id list失败，accountList{} ,errmsg={}",accountList,e.getMessage());
        }
        return idList;
    }


    /**
     * 根据id查找用户
     *  @param userId
     * @return
     */
    public MoocUser findUserById(Integer userId){

        String sql = "select * from mooc_user where id = ?";
        MoocUser moocUser = null;
        try {
            moocUser = jdbcTemplate.queryForObject(sql, new Object[]{userId}, new ManagerMapper());
        }catch (Exception e){
            log.warn("数据库查询该账户失败,userId={}，msg={}",userId,e.getMessage());
        }
        return moocUser;
    }

    /**
     * 插入或者更新用户
     * @param user
     */
    public void save(MoocUser user){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO mooc_user(name,account,password,status,user_type,user_image) ");
        sql.append("VALUES ");
        sql.append("(?,?,?,?,?,?) ");
        sql.append("ON DUPLICATE KEY ");
        sql.append("UPDATE password=?,status=? ");
        jdbcTemplate.update(sql.toString(),user.getName(),user.getAccount(),user.getPassword(),user.getStatus()
                ,user.getUserType(),user.getUserImage(),
                user.getPassword(),user.getStatus());
    }

    public void updateLoginTime(Integer id, Date loginTime){
        String sql = "update mooc_user set login_time = ? where id = ?";
        jdbcTemplate.update(sql,loginTime,id);
    }

}
