package cn.edu.lingnan.mooc.authorize.dao;

import cn.edu.lingnan.mooc.authorize.model.entity.LoginLog;
import org.springframework.stereotype.Repository;

/**
 * @author xmz
 * @date: 2020/11/23
 */
@Repository
public class LoginLogDAO extends BaseDAO{


    /**
     * 登录日志表插入信息
     * @param loginLog
     */
    public void insertLoginLog(LoginLog loginLog){
        String sql = "insert into login_log(log_name,account,create_time,succeed,message,ip,system_type)"
                + "value(?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,loginLog.getLogName(), loginLog.getAccount()
                , loginLog.getCreateTime(),loginLog.getSucceed(),loginLog.getMessage()
                ,loginLog.getIp(),loginLog.getSystemType());
    }


}
