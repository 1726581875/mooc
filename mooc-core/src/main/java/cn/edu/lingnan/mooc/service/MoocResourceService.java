package cn.edu.lingnan.mooc.service;

import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.entity.MoocResource;
import cn.edu.lingnan.mooc.repository.MoocResourceRepository;
import cn.edu.lingnan.mooc.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author xmz
 * @date: 2020/10/31
 */
@Service
public class MoocResourceService {

    @Resource
    private MoocResourceRepository moocResourceRepository;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public MoocResource findById(Integer id){
        Optional<MoocResource> optional = moocResourceRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<MoocResource> findAll(){
        return moocResourceRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<MoocResource> findAllByCondition(MoocResource matchObject){
        return moocResourceRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<MoocResource> findPage(MoocResource matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<MoocResource> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<MoocResource> moocResourcePage = moocResourceRepository.findAll(example, pageable);
        //获取page对象里的list
        List<MoocResource> moocResourceList = moocResourcePage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<MoocResource> pageVO = new PageVO<>();
        pageVO.setContent(moocResourceList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(moocResourcePage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param moocResource
     * @return 返回成功数
     */
    public Integer insert(MoocResource moocResource){
        if (moocResource == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        MoocResource newMoocResource = moocResourceRepository.save(moocResource);
        return newMoocResource == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param moocResource
     * @return 返回成功数
     */
    public Integer insertOrUpdate(MoocResource moocResource){
        if (moocResource == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(moocResource.getId() != null){
          return this.update(moocResource);
        }
        MoocResource newMoocResource = moocResourceRepository.save(moocResource);
        return newMoocResource == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param moocResource
     * @return 返回成功条数
     */
    public Integer update(MoocResource moocResource){
        // 入参校验
        if(moocResource == null || moocResource.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<MoocResource> optional = moocResourceRepository.findById(moocResource.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ moocResource.getId() +"的MoocResource");
        }
        MoocResource dbMoocResource = optional.get();
        //把不为null的属性拷贝到dbMoocResource
        CopyUtil.notNullCopy(moocResource, dbMoocResource);
        //执行保存操作
        MoocResource updateMoocResource = moocResourceRepository.save(dbMoocResource);
        return updateMoocResource == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        moocResourceRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param moocResourceIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> moocResourceIdList){
        List<MoocResource> delMoocResourceList = moocResourceRepository.findAllById(moocResourceIdList);
        moocResourceRepository.deleteInBatch(delMoocResourceList);
        return delMoocResourceList.size();
    }


}
