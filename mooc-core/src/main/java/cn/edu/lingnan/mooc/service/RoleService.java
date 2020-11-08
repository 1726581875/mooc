package cn.edu.lingnan.mooc.service;

import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.entity.Role;
import cn.edu.lingnan.mooc.repository.RoleRepository;
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
public class RoleService {

    @Resource
    private RoleRepository roleRepository;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public Role findById(Integer id){
        Optional<Role> optional = roleRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<Role> findAllByCondition(Role matchObject){
        return roleRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<Role> findPage(Role matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<Role> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<Role> rolePage = roleRepository.findAll(example, pageable);
        //获取page对象里的list
        List<Role> roleList = rolePage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<Role> pageVO = new PageVO<>();
        pageVO.setContent(roleList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(rolePage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param role
     * @return 返回成功数
     */
    public Integer insert(Role role){
        if (role == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        Role newRole = roleRepository.save(role);
        return newRole == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param role
     * @return 返回成功数
     */
    public Integer insertOrUpdate(Role role){
        if (role == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(role.getId() != null){
          return this.update(role);
        }
        Role newRole = roleRepository.save(role);
        return newRole == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param role
     * @return 返回成功条数
     */
    public Integer update(Role role){
        // 入参校验
        if(role == null || role.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<Role> optional = roleRepository.findById(role.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ role.getId() +"的Role");
        }
        Role dbRole = optional.get();
        //把不为null的属性拷贝到dbRole
        CopyUtil.notNullCopy(role, dbRole);
        //执行保存操作
        Role updateRole = roleRepository.save(dbRole);
        return updateRole == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        roleRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param roleIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> roleIdList){
        List<Role> delRoleList = roleRepository.findAllById(roleIdList);
        roleRepository.deleteInBatch(delRoleList);
        return delRoleList.size();
    }


}
