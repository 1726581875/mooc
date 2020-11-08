package cn.edu.lingnan.mooc.service;

import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.entity.MoocManager;
import cn.edu.lingnan.mooc.repository.MoocManagerRepository;
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
public class MoocManagerService {

    @Resource
    private MoocManagerRepository moocManagerRepository;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public MoocManager findById(Integer id){
        Optional<MoocManager> optional = moocManagerRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<MoocManager> findAll(){
        return moocManagerRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<MoocManager> findAllByCondition(MoocManager matchObject){
        return moocManagerRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<MoocManager> findPage(MoocManager matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<MoocManager> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<MoocManager> moocManagerPage = moocManagerRepository.findAll(example, pageable);
        //获取page对象里的list
        List<MoocManager> moocManagerList = moocManagerPage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<MoocManager> pageVO = new PageVO<>();
        pageVO.setContent(moocManagerList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(moocManagerPage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param moocManager
     * @return 返回成功数
     */
    public Integer insert(MoocManager moocManager){
        if (moocManager == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        MoocManager newMoocManager = moocManagerRepository.save(moocManager);
        return newMoocManager == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param moocManager
     * @return 返回成功数
     */
    public Integer insertOrUpdate(MoocManager moocManager){
        if (moocManager == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(moocManager.getId() != null){
          return this.update(moocManager);
        }
        MoocManager newMoocManager = moocManagerRepository.save(moocManager);
        return newMoocManager == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param moocManager
     * @return 返回成功条数
     */
    public Integer update(MoocManager moocManager){
        // 入参校验
        if(moocManager == null || moocManager.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<MoocManager> optional = moocManagerRepository.findById(moocManager.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ moocManager.getId() +"的MoocManager");
        }
        MoocManager dbMoocManager = optional.get();
        //把不为null的属性拷贝到dbMoocManager
        CopyUtil.notNullCopy(moocManager, dbMoocManager);
        //执行保存操作
        MoocManager updateMoocManager = moocManagerRepository.save(dbMoocManager);
        return updateMoocManager == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        moocManagerRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param moocManagerIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> moocManagerIdList){
        List<MoocManager> delMoocManagerList = moocManagerRepository.findAllById(moocManagerIdList);
        moocManagerRepository.deleteInBatch(delMoocManagerList);
        return delMoocManagerList.size();
    }


}
