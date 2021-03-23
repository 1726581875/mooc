package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.entity.IpBlacklist;
import cn.edu.lingnan.core.repository.IpBlacklistRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.mooc.common.model.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author xmz
 * @date: 2021/03/23
 */
@Service
public class IpBlacklistService {

    @Resource
    private IpBlacklistRepository ipBlacklistRepository;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public IpBlacklist findById(Integer id){
        Optional<IpBlacklist> optional = ipBlacklistRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<IpBlacklist> findAll(){
        return ipBlacklistRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<IpBlacklist> findAllByCondition(IpBlacklist matchObject){
        return ipBlacklistRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<IpBlacklist> findPage(IpBlacklist matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<IpBlacklist> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<IpBlacklist> ipBlacklistPage = ipBlacklistRepository.findAll(example, pageable);
        //获取page对象里的list
        List<IpBlacklist> ipBlacklistList = ipBlacklistPage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<IpBlacklist> pageVO = new PageVO<>();
        pageVO.setContent(ipBlacklistList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(ipBlacklistPage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param ipBlacklist
     * @return 返回成功数
     */
    public Integer insert(IpBlacklist ipBlacklist){
        if (ipBlacklist == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        IpBlacklist newIpBlacklist = ipBlacklistRepository.save(ipBlacklist);
        return newIpBlacklist == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param ipBlacklist
     * @return 返回成功数
     */
    public Integer insertOrUpdate(IpBlacklist ipBlacklist){
        if (ipBlacklist == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(ipBlacklist.getId() != null){
          return this.update(ipBlacklist);
        }
        IpBlacklist newIpBlacklist = ipBlacklistRepository.save(ipBlacklist);
        return newIpBlacklist == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param ipBlacklist
     * @return 返回成功条数
     */
    public Integer update(IpBlacklist ipBlacklist){
        // 入参校验
        if(ipBlacklist == null || ipBlacklist.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<IpBlacklist> optional = ipBlacklistRepository.findById(ipBlacklist.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ ipBlacklist.getId() +"的IpBlacklist");
        }
        IpBlacklist dbIpBlacklist = optional.get();
        //把不为null的属性拷贝到dbIpBlacklist
        CopyUtil.notNullCopy(ipBlacklist, dbIpBlacklist);
        //执行保存操作
        IpBlacklist updateIpBlacklist = ipBlacklistRepository.save(dbIpBlacklist);
        return updateIpBlacklist == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        ipBlacklistRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param ipBlacklistIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> ipBlacklistIdList){
        List<IpBlacklist> delIpBlacklistList = ipBlacklistRepository.findAllById(ipBlacklistIdList);
        ipBlacklistRepository.deleteInBatch(delIpBlacklistList);
        return delIpBlacklistList.size();
    }


}
