package cn.edu.lingnan.mooc.authorize.shiro.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@Repository
public class BaseDAO {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

}
