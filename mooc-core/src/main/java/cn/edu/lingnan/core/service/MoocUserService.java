package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.entity.MoocUser;
import cn.edu.lingnan.core.repository.MoocUserRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.mooc.common.model.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author xmz
 * @date: 2020/12/07
 */
@Service
public class MoocUserService {

    @Resource
    private MoocUserRepository moocUserRepository;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public MoocUser findById(Integer id){
        Optional<MoocUser> optional = moocUserRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<MoocUser> findAll(){
        return moocUserRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<MoocUser> findAllByCondition(MoocUser matchObject){
        return moocUserRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<MoocUser> findPage(MoocUser matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<MoocUser> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<MoocUser> moocUserPage = moocUserRepository.findAll(example, pageable);
        //获取page对象里的list
        List<MoocUser> moocUserList = moocUserPage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<MoocUser> pageVO = new PageVO<>();
        pageVO.setContent(moocUserList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(moocUserPage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param moocUser
     * @return 返回成功数
     */
    public Integer insert(MoocUser moocUser){
        if (moocUser == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        MoocUser newMoocUser = moocUserRepository.save(moocUser);
        return newMoocUser == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param moocUser
     * @return 返回成功数
     */
    public Integer insertOrUpdate(MoocUser moocUser){
        if (moocUser == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(moocUser.getId() != null){
          return this.update(moocUser);
        }
        MoocUser newMoocUser = moocUserRepository.save(moocUser);
        return newMoocUser == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param moocUser
     * @return 返回成功条数
     */
    public Integer update(MoocUser moocUser){
        // 入参校验
        if(moocUser == null || moocUser.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<MoocUser> optional = moocUserRepository.findById(moocUser.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ moocUser.getId() +"的MoocUser");
        }
        MoocUser dbMoocUser = optional.get();
        //把不为null的属性拷贝到dbMoocUser
        CopyUtil.notNullCopy(moocUser, dbMoocUser);
        //执行保存操作
        MoocUser updateMoocUser = moocUserRepository.save(dbMoocUser);
        return updateMoocUser == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        moocUserRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param moocUserIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> moocUserIdList){
        List<MoocUser> delMoocUserList = moocUserRepository.findAllById(moocUserIdList);
        moocUserRepository.deleteInBatch(delMoocUserList);
        return delMoocUserList.size();
    }


}
