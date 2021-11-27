package cn.edu.lingnan.mooc.core.service;


import cn.edu.lingnan.mooc.core.entity.MonitorRecord;
import cn.edu.lingnan.mooc.core.entity.MoocUser;
import cn.edu.lingnan.mooc.core.param.MonitorRecordParam;
import cn.edu.lingnan.mooc.core.repository.MonitorRecordRepository;
import cn.edu.lingnan.mooc.core.repository.MoocUserRepository;
import cn.edu.lingnan.mooc.core.util.ConvertTimeUtil;
import cn.edu.lingnan.mooc.core.util.CopyUtil;
import cn.edu.lingnan.mooc.core.vo.MonitorRecordVO;
import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.common.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2021/01/02
 */
@Service
public class MonitorRecordService {

    @Resource
    private MonitorRecordRepository monitorRecordRepository;
    @Autowired
    private MoocUserRepository moocUserRepository;


    public PageVO<MonitorRecordVO> findPageByCondition(MonitorRecordParam monitorRecordParam, Integer pageIndex, Integer pageSize){
        // 如果不传账号或名字默使用null，null表示不使用名字匹配
        String nameOrAccount = monitorRecordParam.getNameOrAccount();
        if(nameOrAccount == null ||  nameOrAccount.equals("")){
            nameOrAccount = null;
        }
        // 如果不传类型默认使用null，null表示查所有类型
        String recordType = monitorRecordParam.getRecordType();
        if(recordType == null ||  recordType.equals("")){
            recordType = null;
        }
        //如果不传时间，默认查7天前
        String startTimeStr = monitorRecordParam.getStartTime();
        String endTimeStr = monitorRecordParam.getEndTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if(null == startTimeStr || startTimeStr.equals("")){
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE,-7);
            startTimeStr = formatter.format(calendar.getTime());
        }
        if(null == endTimeStr || endTimeStr.equals("")){
            endTimeStr = formatter.format(new Date());
        }

        //如果是教师登录，查询当前教师的监控记录信息
        Integer teacherId = null;
        if(UserUtil.isTeacher()){
            teacherId = Math.toIntExact(UserUtil.getUserId());
        }

        // 构造查询参数
        Date startTime = ConvertTimeUtil.getTime(startTimeStr);
        Date endTime = ConvertTimeUtil.getEndTime(endTimeStr);
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"create_time");
        // 调用分页条件查询方法
        Page<MonitorRecord> monitorRecordPage = monitorRecordRepository.findMonitorRecordByCondition(nameOrAccount, recordType ,teacherId, startTime, endTime, pageable);
        //获取page对象里的list
        List<MonitorRecord> monitorRecordList = monitorRecordPage.getContent();
        // 转化为VO对象
        List<MonitorRecordVO> monitorRecordVOList = CopyUtil.copyList(monitorRecordList, MonitorRecordVO.class);
        // 设置用户名 + 账号
        List<Long> teacherIdList = monitorRecordList.stream().map(MonitorRecord::getTeacherId)
                .collect(Collectors.toList());
        List<MoocUser> teacherList = moocUserRepository.findAllById(teacherIdList);
        Map<Long, MoocUser> userMap = teacherList.stream().collect(Collectors.toMap(MoocUser::getId, e -> e, (u1,u2) -> u1));
        monitorRecordVOList.forEach(e -> {
            MoocUser teacher = userMap.get(e.getTeacherId());
            String nameAccount = Objects.nonNull(teacher) ? teacher.getName() + "(" + teacher.getAccount() + ")" : "";
            e.setNameAccount(nameAccount);
        });
        // 封装返回结果
        PageVO<MonitorRecordVO> pageVO = new PageVO<>();
        pageVO.setContent(monitorRecordVOList);
        pageVO.setPageSize(pageSize);
        pageVO.setPageIndex(pageIndex);
        //页数
        pageVO.setPageCount(monitorRecordPage.getTotalPages());
        return pageVO;
    }

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public MonitorRecord findById(Long id){
        Optional<MonitorRecord> optional = monitorRecordRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<MonitorRecord> findAll(){
        return monitorRecordRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<MonitorRecord> findAllByCondition(MonitorRecord matchObject){
        return monitorRecordRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<MonitorRecordVO> findPage(MonitorRecord matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<MonitorRecord> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<MonitorRecord> monitorRecordPage = monitorRecordRepository.findAll(example, pageable);
        //获取page对象里的list
        List<MonitorRecord> monitorRecordList = monitorRecordPage.getContent();
        // 转化为VO对象
        List<MonitorRecordVO> monitorRecordVOList = CopyUtil.copyList(monitorRecordList, MonitorRecordVO.class);
        // 设置用户名 + 账号
/*        Map<Long, MoocUser> userMap = moocUserService.getUserMap(monitorRecordList.stream().map(MonitorRecord::getTeacherId)
                .collect(Collectors.toList()));
        monitorRecordVOList.forEach(e -> {
            MoocUser user = userMap.get(e.getTeacherId());
            String nameAccount = user.getName() + "(" + user.getAccount() + ")";
            e.setNameAccount(nameAccount);
        });*/
        /* 4. 封装到自定义分页结果 */
        PageVO<MonitorRecordVO> pageVO = new PageVO<>();
        pageVO.setContent(monitorRecordVOList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(monitorRecordPage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param monitorRecord
     * @return 返回成功数
     */
    public Integer insert(MonitorRecord monitorRecord){
        if (monitorRecord == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        MonitorRecord newMonitorRecord = monitorRecordRepository.save(monitorRecord);
        return newMonitorRecord == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param monitorRecord
     * @return 返回成功数
     */
    public Integer insertOrUpdate(MonitorRecord monitorRecord){
        if (monitorRecord == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(monitorRecord.getId() != null){
          return this.update(monitorRecord);
        }
        MonitorRecord newMonitorRecord = monitorRecordRepository.save(monitorRecord);
        return newMonitorRecord == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param monitorRecord
     * @return 返回成功条数
     */
    public Integer update(MonitorRecord monitorRecord){
        // 入参校验
        if(monitorRecord == null || monitorRecord.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<MonitorRecord> optional = monitorRecordRepository.findById(monitorRecord.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ monitorRecord.getId() +"的MonitorRecord");
        }
        MonitorRecord dbMonitorRecord = optional.get();
        //把不为null的属性拷贝到dbMonitorRecord
        CopyUtil.notNullCopy(monitorRecord, dbMonitorRecord);
        //执行保存操作
        MonitorRecord updateMonitorRecord = monitorRecordRepository.save(dbMonitorRecord);
        return updateMonitorRecord == null ? 0 : 1;
    }


    public Integer deleteById(Long id){
        monitorRecordRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param monitorRecordIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Long> monitorRecordIdList){
        List<MonitorRecord> delMonitorRecordList = monitorRecordRepository.findAllById(monitorRecordIdList);
        monitorRecordRepository.deleteInBatch(delMonitorRecordList);
        return delMonitorRecordList.size();
    }


}
