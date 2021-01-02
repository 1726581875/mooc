package cn.edu.lingnan.core.repository;
import cn.edu.lingnan.core.entity.LoginLog;
import cn.edu.lingnan.core.entity.MonitorRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * @author xmz
 * @date 2021/01/02
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface MonitorRecordRepository extends JpaRepository<MonitorRecord, Integer>,JpaSpecificationExecutor<MonitorRecord> {
    /**
     * 分页条件查询
     * @param nameOrAccount 账号或者昵称匹配,传null默认匹配所有
     * @param recordType 按类型查询,传null默认匹配所有
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageable 分页条件
     * @return
     */
    @Query(value="select * from course_monitor_record r,mooc_user u where r.teacher_id = u.id "
            + " and (r.create_time >= ?3 and r.create_time <= ?4) "
            + " and if(?1 is null , 1=1 ,(u.account = ?1 or u.name like CONCAT('%',?1,'%')))"
            + " and if(?2 is null , 1=1 ,r.record_type = ?2) "
            ,countQuery = "select count(1) from login_log",nativeQuery=true)
    Page<MonitorRecord> findMonitorRecordByCondition(String nameOrAccount,String recordType,Date startTime, Date endTime, Pageable pageable);
}
