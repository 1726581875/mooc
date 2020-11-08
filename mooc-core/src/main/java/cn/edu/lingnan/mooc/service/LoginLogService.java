package cn.edu.lingnan.mooc.service;

import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.entity.LoginLog;
import cn.edu.lingnan.mooc.repository.LoginLogRepository;
import cn.edu.lingnan.mooc.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author xmz
 * @date: 2020/10/23
 */
@Service
public class LoginLogService {

    @Resource
    private LoginLogRepository loginLogRepository;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public LoginLog findById(Integer id){
        Optional<LoginLog> optional = loginLogRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<LoginLog> findAll(){
        return loginLogRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<LoginLog> findAllByCondition(LoginLog matchObject){
        return loginLogRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<LoginLog> findPage(LoginLog matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<LoginLog> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<LoginLog> loginLogPage = loginLogRepository.findAll(example, pageable);
        //获取page对象里的list
        List<LoginLog> loginLogList = loginLogPage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<LoginLog> pageVO = new PageVO<>();
        pageVO.setContent(loginLogList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(loginLogPage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param loginLog
     * @return 返回成功数
     */
    public Integer insert(LoginLog loginLog){
        if (loginLog == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        LoginLog newLoginLog = loginLogRepository.save(loginLog);
        return newLoginLog == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param loginLog
     * @return 返回成功数
     */
    public Integer insertOrUpdate(LoginLog loginLog){
        if (loginLog == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(loginLog.getId() != null){
          return this.update(loginLog);
        }
        LoginLog newLoginLog = loginLogRepository.save(loginLog);
        return newLoginLog == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param loginLog
     * @return 返回成功条数
     */
    public Integer update(LoginLog loginLog){
        // 入参校验
        if(loginLog == null || loginLog.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<LoginLog> optional = loginLogRepository.findById(loginLog.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ loginLog.getId() +"的LoginLog");
        }
        LoginLog dbLoginLog = optional.get();
        //把不为null的属性拷贝到dbLoginLog
        CopyUtil.notNullCopy(loginLog, dbLoginLog);
        //执行保存操作
        LoginLog updateLoginLog = loginLogRepository.save(dbLoginLog);
        return updateLoginLog == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        loginLogRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param loginLogIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> loginLogIdList){
        List<LoginLog> delLoginLogList = loginLogRepository.findAllById(loginLogIdList);
        loginLogRepository.deleteInBatch(delLoginLogList);
        return delLoginLogList.size();
    }


}
