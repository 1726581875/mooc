package cn.edu.lingnan.core.service;

import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.core.entity.MoocFile;
import cn.edu.lingnan.core.repository.MoocFileRepository;
import cn.edu.lingnan.core.util.CopyUtil;
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
public class MoocFileService {

    @Resource
    private MoocFileRepository moocFileRepository;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public MoocFile findById(Integer id){
        Optional<MoocFile> optional = moocFileRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<MoocFile> findAll(){
        return moocFileRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<MoocFile> findAllByCondition(MoocFile matchObject){
        return moocFileRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<MoocFile> findPage(MoocFile matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<MoocFile> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<MoocFile> moocFilePage = moocFileRepository.findAll(example, pageable);
        //获取page对象里的list
        List<MoocFile> moocFileList = moocFilePage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<MoocFile> pageVO = new PageVO<>();
        pageVO.setContent(moocFileList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(moocFilePage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param moocFile
     * @return 返回成功数
     */
    public Integer insert(MoocFile moocFile){
        if (moocFile == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        MoocFile newMoocFile = moocFileRepository.save(moocFile);
        return newMoocFile == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param moocFile
     * @return 返回成功数
     */
    public Integer insertOrUpdate(MoocFile moocFile){
        if (moocFile == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(moocFile.getId() != null){
          return this.update(moocFile);
        }
        MoocFile newMoocFile = moocFileRepository.save(moocFile);
        return newMoocFile == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param moocFile
     * @return 返回成功条数
     */
    public Integer update(MoocFile moocFile){
        // 入参校验
        if(moocFile == null || moocFile.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<MoocFile> optional = moocFileRepository.findById(moocFile.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ moocFile.getId() +"的MoocFile");
        }
        MoocFile dbMoocFile = optional.get();
        //把不为null的属性拷贝到dbMoocFile
        CopyUtil.notNullCopy(moocFile, dbMoocFile);
        //执行保存操作
        MoocFile updateMoocFile = moocFileRepository.save(dbMoocFile);
        return updateMoocFile == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        moocFileRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param moocFileIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> moocFileIdList){
        List<MoocFile> delMoocFileList = moocFileRepository.findAllById(moocFileIdList);
        moocFileRepository.deleteInBatch(delMoocFileList);
        return delMoocFileList.size();
    }


}
