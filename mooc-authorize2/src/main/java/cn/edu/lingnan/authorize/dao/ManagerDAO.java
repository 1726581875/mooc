package cn.edu.lingnan.authorize.dao;

import cn.edu.lingnan.authorize.constant.SqlConstant;
import cn.edu.lingnan.authorize.model.entity.ManagerRoleRel;
import cn.edu.lingnan.authorize.model.entity.MoocManager;
import cn.edu.lingnan.mooc.common.model.PageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@Repository
public class ManagerDAO extends BaseDAO {

    private Logger log = LoggerFactory.getLogger(ManagerDAO.class);

    /**
     * 行映射
     */
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
    public int save(MoocManager manager){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO mooc_manager(name,account,password,status) ");
        sql.append("VALUES ");
        sql.append("(?,?,?,?) ");
        sql.append("ON DUPLICATE KEY ");
        sql.append("UPDATE password=?,status=?,update_time=?,name=? ");
        return jdbcTemplate.update(sql.toString(), manager.getName(), manager.getAccount(), manager.getPassword(), manager.getStatus()
                , manager.getPassword(), manager.getStatus(), manager.getUpdateTime(), manager.getName());
    }

    public long insert(MoocManager manager){
        final String sql = "INSERT INTO mooc_manager(name,account,password,status) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps  = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, manager.getName());
                ps.setObject(2, manager.getAccount());
                ps.setObject(3, manager.getPassword());
                ps.setObject(4, manager.getStatus());
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    /**
     * 疲劳插入管理员角色关系
     * @param managerRoleRels
     * @return
     */
    public void batchInsertManagerRoleRel(List<ManagerRoleRel> managerRoleRels){
        String sql=  "insert into manager_role_rel (manager_id,role_id) values (? , ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        managerRoleRels.forEach(e -> batchArgs.add(new Object[]{e.getManagerId(),e.getRoleId()}));
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public void batchDeleteManager(List<Long> managerIdList){
/*        String sql=  "delete from mooc_manager where id in ("
                + accountList.stream().map(e->"'" + e + "'").collect(Collectors.joining(",")) + ")";
        List<Object[]> batchArgs = new ArrayList<>();
        managerRoleRels.forEach(e -> batchArgs.add(new Object[]{e.getManagerId(),e.getRoleId()}));
        jdbcTemplate.batchUpdate(sql, batchArgs);*/
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

    /**
     * 根据账号查找id
     * @param accountList
     * @return
     */
    public List<Integer> findManagerIdByAccountList(List<String> accountList){

        List<Integer> idList = new ArrayList<>();
        String sql = "select id from mooc_manager where account in ("
                + accountList.stream().map(e->"'" + e + "'").collect(Collectors.joining(",")) + ")";
        try {
            idList = jdbcTemplate.queryForList(sql, Integer.class);
        }catch (Exception e){
            log.warn("==根据账号查询数据库id list失败，accountList{} ,errmsg={}",accountList,e.getMessage());
        }
        return idList;
    }

    public void updateLoginTime(Integer id, Date loginTime){
        String sql = "update mooc_user set login_time = ? where id = ?";
        jdbcTemplate.update(sql,loginTime,id);
    }

    /**
     * 分页查询管理员列表
     * @param queryStr
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageVO<MoocManager> findManagePage(String queryStr,Integer pageIndex,Integer pageSize){

        StringBuilder commonSql = new StringBuilder(" from mooc_manager ");
        if(!StringUtils.isEmpty(queryStr)){
            commonSql.append(" where name like concat('%',?, '%') or account like concat('%',?, '%') ");
        }
        //拼接sql, 统计总行数
        String countSql = SqlConstant.SELECT_COUNT + commonSql.toString();
        Integer totalCount = !StringUtils.isEmpty(queryStr) ?
                jdbcTemplate.queryForObject(countSql, new Object[]{queryStr,queryStr}, Integer.class)
                : jdbcTemplate.queryForObject(countSql, Integer.class);
        log.info("查询管理员信息 countSql={}",countSql);
        if(totalCount == null || totalCount == 0){
            return new PageVO<>(pageIndex, pageSize,0,0,new ArrayList<>());
        }
        //拼接查询sql，查询数据
        String querySql = SqlConstant.SELECT_ALL + commonSql.toString() + "limit " + (pageIndex - 1)*pageSize + ", " + pageSize;
        log.info("查询管理员信息 querySql={}",querySql);
        List<MoocManager> moocManagers = !StringUtils.isEmpty(queryStr) ?
                jdbcTemplate.query(querySql, new Object[]{queryStr,queryStr}, new ManagerMapper())
                : jdbcTemplate.query(querySql, new ManagerMapper()) ;

        //计算页数
        Integer pageCount = totalCount%pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        return new PageVO<>(pageIndex, pageSize, pageCount, totalCount, moocManagers);
    }

    /**
     * 删除管理员角色关系
     * @param managerId
     * @return
     */
    public int deleteManagerRoleRefByManagerId(Long managerId){
        String sql = "delete from manager_role_rel where manager_id = ?";
        return jdbcTemplate.update(sql, managerId);
    }


}
