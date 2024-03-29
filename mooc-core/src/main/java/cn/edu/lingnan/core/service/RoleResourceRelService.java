package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.entity.RoleResourceRel;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.core.repository.RoleResourceRelRepository;
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
public class RoleResourceRelService {

    @Resource
    private RoleResourceRelRepository roleResourceRelRepository;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public RoleResourceRel findById(Integer id){
        Optional<RoleResourceRel> optional = roleResourceRelRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<RoleResourceRel> findAll(){
        return roleResourceRelRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<RoleResourceRel> findAllByCondition(RoleResourceRel matchObject){
        return roleResourceRelRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<RoleResourceRel> findPage(RoleResourceRel matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<RoleResourceRel> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<RoleResourceRel> roleResourceRelPage = roleResourceRelRepository.findAll(example, pageable);
        //获取page对象里的list
        List<RoleResourceRel> roleResourceRelList = roleResourceRelPage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<RoleResourceRel> pageVO = new PageVO<>();
        pageVO.setContent(roleResourceRelList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(roleResourceRelPage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param roleResourceRel
     * @return 返回成功数
     */
    public Integer insert(RoleResourceRel roleResourceRel){
        if (roleResourceRel == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        RoleResourceRel newRoleResourceRel = roleResourceRelRepository.save(roleResourceRel);
        return newRoleResourceRel == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param roleResourceRel
     * @return 返回成功数
     */
    public Integer insertOrUpdate(RoleResourceRel roleResourceRel){
        if (roleResourceRel == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(roleResourceRel.getId() != null){
          return this.update(roleResourceRel);
        }
        RoleResourceRel newRoleResourceRel = roleResourceRelRepository.save(roleResourceRel);
        return newRoleResourceRel == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param roleResourceRel
     * @return 返回成功条数
     */
    public Integer update(RoleResourceRel roleResourceRel){
        // 入参校验
        if(roleResourceRel == null || roleResourceRel.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<RoleResourceRel> optional = roleResourceRelRepository.findById(roleResourceRel.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ roleResourceRel.getId() +"的RoleResourceRel");
        }
        RoleResourceRel dbRoleResourceRel = optional.get();
        //把不为null的属性拷贝到dbRoleResourceRel
        CopyUtil.notNullCopy(roleResourceRel, dbRoleResourceRel);
        //执行保存操作
        RoleResourceRel updateRoleResourceRel = roleResourceRelRepository.save(dbRoleResourceRel);
        return updateRoleResourceRel == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        roleResourceRelRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param roleResourceRelIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> roleResourceRelIdList){
        List<RoleResourceRel> delRoleResourceRelList = roleResourceRelRepository.findAllById(roleResourceRelIdList);
        roleResourceRelRepository.deleteInBatch(delRoleResourceRelList);
        return delRoleResourceRelList.size();
    }


}
