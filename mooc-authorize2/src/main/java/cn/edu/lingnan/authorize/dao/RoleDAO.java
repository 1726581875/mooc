package cn.edu.lingnan.authorize.dao;

import cn.edu.lingnan.authorize.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@Repository
public class RoleDAO extends BaseDAO{


    /**
     * 根据管理员账户查询角色id
     * @param managerId
     * @return
     */
    public List<Long> findAllRoleIdByManagerId(Long managerId){
        String querySql = "select role_id from manager_role_rel where manager_id = ?";
        return jdbcTemplate.query(querySql,new Object[]{managerId},(resultSet,i)->resultSet.getLong("role_id"));
    }

    /**
     * 根据管理员账户查询角色列表
     * @param managerId
     * @return
     */
    public List<Role> findAllRoleByManagerId(Long managerId){
        String querySql = "select r.* from manager_role_rel mrr,role r where r.id = mrr.role_id and mrr.manager_id = ?";
        return jdbcTemplate.query(querySql,new Object[]{managerId}
        ,(resultSet,i)-> new Role(resultSet.getInt("id"),resultSet.getString("name")));
    }

    public void saveAll(List<Role> roleList){


    }


}
