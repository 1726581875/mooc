package cn.edu.lingnan.mooc.statistics.repository;

import cn.edu.lingnan.mooc.statistics.entity.mysql.LoginAmountCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date 2021/01/07
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface LoginAmountCountRepository extends JpaRepository<LoginAmountCount, Integer>, JpaSpecificationExecutor<LoginAmountCount> {
    /**
     * 根据时间范围查询登录人数
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value="select * from login_amount_count where count_time >= ?1 and count_time <= ?2",nativeQuery = true)
    List<LoginAmountCount> findLoginAmountCountByTime(Date startTime, Date endTime);

}
