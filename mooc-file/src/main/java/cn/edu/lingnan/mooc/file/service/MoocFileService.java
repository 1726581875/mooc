package cn.edu.lingnan.mooc.file.service;

import cn.edu.lingnan.mooc.file.entity.MoocFile;
import cn.edu.lingnan.mooc.file.repository.MoocFileRepository;
import cn.edu.lingnan.mooc.file.util.CopyUtil;
import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.file.vo.FileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/10/17
 */
@Service
@Slf4j
public class MoocFileService {

    @Resource
    private MoocFileRepository moocFileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据用户id list 获取对应用户名
     * @param userIdList
     * @return
     */
    public Map<Integer,String> getUserNameMap(List<Integer> userIdList){
        // 构造sql
        StringBuilder sqlBuilder = new StringBuilder("select id,name from mooc_user where id in (");
        sqlBuilder.append(userIdList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        sqlBuilder.append(")");
        log.info("get user name sql is sql:{}",sqlBuilder.toString());
        // 执行语句
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        // 获取和构造返回结果
        List<Object[]> resultList = query.getResultList();
        Map<Integer,String> resultMap = new HashMap<>();
        resultList.forEach(o -> resultMap.put(Integer.valueOf(o[0].toString()),o[1].toString()));
        return resultMap;
    }

    /**
     * 根据课程id list 获取对应课程名
     * @param courseIdList
     * @return
     */
    public Map<Integer,String> getCourseNameMap(List<Integer> courseIdList){
        // 构造sql
        StringBuilder sqlBuilder = new StringBuilder("select id,name from course where id in (");
        sqlBuilder.append(courseIdList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        sqlBuilder.append(")");
        log.info("get course name sql is:{}",sqlBuilder.toString());
        // 执行语句
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        // 获取和构造返回结果
        List<Object[]> resultList = query.getResultList();
        Map<Integer,String> resultMap = new HashMap<>();
        resultList.forEach(o -> resultMap.put(Integer.valueOf(o[0].toString()),o[1].toString()));
        return resultMap;
    }

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

    public MoocFile findByCondition(MoocFile moocFile){
        Optional<MoocFile> optional = moocFileRepository.findOne(Example.of(moocFile));
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public MoocFile findByFileKey(String fileKey){
        MoocFile moocFile = new MoocFile();
        moocFile.setFileKey(fileKey);
        Optional<MoocFile> optional = moocFileRepository.findOne(Example.of(moocFile));
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
    public PageVO<FileVO> findPage(MoocFile matchObject, Integer pageIndex, Integer pageSize){
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
        // 获取课程id list 和 用户id list
        List<Integer> courseIdList = moocFileList.stream().map(MoocFile::getCourseId).collect(Collectors.toList());
        List<Integer> userIdList = moocFileList.stream().map(MoocFile::getUserId).collect(Collectors.toList());
        Map<Integer, String> userNameMap = this.getUserNameMap(userIdList);
        Map<Integer, String> courseNameMap = this.getCourseNameMap(courseIdList);
        // 复制基本属性到VO对象
        List<FileVO> fileVOList = CopyUtil.copyList(moocFileList, FileVO.class);
        fileVOList.forEach(file -> {
            file.setUserName(userNameMap.getOrDefault(file.getUserId(),"未知用户"));
            file.setCourseName(courseNameMap.getOrDefault(file.getCourseId(),"未知课程"));
        });
        /* 4. 封装到自定义分页结果 */
        PageVO<FileVO> pageVO = new PageVO<>();
        pageVO.setContent(fileVOList);
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
    public MoocFile insertOrUpdate(MoocFile moocFile){
        if (moocFile == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(moocFile.getId() != null){
          return this.update(moocFile);
        }
        return moocFileRepository.save(moocFile);
    }


    /**
     *  选择性更新
     * @param moocFile
     * @return 返回成功条数
     */
    public MoocFile update(MoocFile moocFile){
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
        return moocFileRepository.save(dbMoocFile);
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
