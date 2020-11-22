package cn.edu.lingnan.mooc.dao;

import cn.edu.lingnan.mooc.model.MenuTree;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@Repository
public class MenuTreeDAO extends BaseDAO{

    public List<MenuTree> findMenuList(List<Long> roleIdList){
        StringBuilder sql = new StringBuilder("select mt.* from menu_tree mt,role_menu_rel rmr where mt.id = rmr.menu_id and rmr.role_id in (");
        sql.append(roleIdList.stream().map(e -> "?").collect(Collectors.joining(",")));
        sql.append(")");
        return jdbcTemplate.query(sql.toString(),roleIdList.toArray(new Object[0]), new MenuTreeMapper());
    }

    private class MenuTreeMapper implements RowMapper<MenuTree>{
        @Override
        public MenuTree mapRow(ResultSet resultSet, int i) throws SQLException {
            MenuTree menuTree = new MenuTree();
            menuTree.setId(resultSet.getLong("id"));
            menuTree.setLabel(resultSet.getString("label"));
            menuTree.setLeaf(resultSet.getInt("leaf"));
            menuTree.setParentId(resultSet.getLong("parent_id"));
            menuTree.setRouter(resultSet.getString("router"));
            menuTree.setPermission(resultSet.getString("permission"));
            return menuTree;
        }
    }
}
