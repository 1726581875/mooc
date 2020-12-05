package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.entity.LoginLog;
import cn.edu.lingnan.core.param.LoginLogParam;
import cn.edu.lingnan.core.repository.LoginLogRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.core.util.ConvertTimeUtil;
import cn.edu.lingnan.core.vo.LoginLogVO;
import cn.edu.lingnan.mooc.common.model.PageVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * @author xmz
 * @date: 2020/11/29
 */
@Service
public class LoginLogService {

    @Resource
    private LoginLogRepository loginLogRepository;


    public PageVO<LoginLogVO> findLoginLogByCondition(LoginLogParam logParam,Integer pageIndex,Integer pageSize){

        String matchStr = logParam.getMatchStr();
        if(matchStr == null ||  matchStr.equals("")){
            matchStr = null;
        }
        //如果不传时间，默认查30天前
        String startTimeStr = logParam.getStartTime();
        String endTimeStr = logParam.getEndTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if(null == startTimeStr || startTimeStr.equals("")){
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE,-30);
            startTimeStr = formatter.format(calendar.getTime());
        }
        if(null == endTimeStr || endTimeStr.equals("")){
            endTimeStr = formatter.format(new Date());
        }
        // 构造查询参数
        Date startTime = ConvertTimeUtil.getTime(startTimeStr);
        Date endTime = ConvertTimeUtil.getEndTime(endTimeStr);
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"create_time");
        // 调用分页条件查询方法
        Page<LoginLog> loginLogPage = loginLogRepository.findLoginLogByCondition(matchStr, startTime, endTime, pageable);
        // 封装返回结果
        PageVO<LoginLogVO> pageVO = new PageVO<>();
        pageVO.setContent(CopyUtil.copyList(loginLogPage.getContent(),LoginLogVO.class));
        pageVO.setPageTotal((int) loginLogPage.getTotalElements()); //总条数
        pageVO.setPageSize(pageSize);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageCount(loginLogPage.getTotalPages()); //页数
        return pageVO;
    }

    /**
     * 条件分页查询
     * @param logParam 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<LoginLogVO> findPage(LoginLogParam logParam, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询

      //  predicates.add(cb.greaterThanOrEqualTo(root.get("commitTime").as(Date.class), stime（Date类型

        Date startTime = ConvertTimeUtil.getTime(logParam.getStartTime());
        Date endTime = ConvertTimeUtil.getEndTime(logParam.getEndTime());
        Specification<LoginLog> querySpecifi = new Specification<LoginLog>() {
            @Override
            public Predicate toPredicate(Root<LoginLog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (startTime != null) {
                    //大于或等于传入时间
                    predicates.add(cb.greaterThanOrEqualTo(root.get("commitTime").as(Date.class), startTime));
                }
                if (endTime != null) {
                    //小于或等于传入时间
                    predicates.add(cb.lessThanOrEqualTo(root.get("commitTime").as(Date.class), endTime));
                }
                if (StringUtils.isNotBlank(logParam.getMatchStr())) {
                    //模糊查找
                    predicates.add(cb.like(root.get("name").as(String.class), "%" + logParam.getMatchStr() + "%"));
                }
                // and到一起的话所有条件就是且关系，or就是或关系
                Predicate[] predicates1 = predicates.toArray(new Predicate[predicates.size()]);
                return cb.and(predicates1);
            }
        };

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"createTime");
        // 3、 传入条件、分页参数，调用方法
        Page<LoginLog> loginLogPage = loginLogRepository.findAll(querySpecifi, pageable);
        //获取page对象里的list
        List<LoginLog> loginLogList = loginLogPage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<LoginLogVO> pageVO = new PageVO<>();
        pageVO.setContent(CopyUtil.copyList(loginLogList,LoginLogVO.class));
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(loginLogPage.getTotalPages());
        return pageVO;
    }




}
