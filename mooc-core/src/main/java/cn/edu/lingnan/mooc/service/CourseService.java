package cn.edu.lingnan.mooc.service;

import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.entity.Course;
import cn.edu.lingnan.mooc.repository.CourseRepository;
import cn.edu.lingnan.mooc.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author xmz
 * @date: 2020/10/13
 */
@Service
public class CourseService {

    @Resource
    private CourseRepository courseRepository;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public Course findById(Integer id){
        Optional<Course> optional = courseRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<Course> findAll(){
        return courseRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<Course> findAllByCondition(Course matchObject){
        return courseRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<Course> findPage(Course matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<Course> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<Course> coursePage = courseRepository.findAll(example, pageable);
        //获取page对象里的list
        List<Course> courseList = coursePage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<Course> pageVO = new PageVO<>();
        pageVO.setContent(courseList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(coursePage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param course
     * @return 返回成功数
     */
    public Integer insert(Course course){
        if (course == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        Course newCourse = courseRepository.save(course);
        return newCourse == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param course
     * @return 返回成功数
     */
    public Integer insertOrUpdate(Course course){
        if (course == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(course.getId() != null){
          return this.update(course);
        }
        Course newCourse = courseRepository.save(course);
        return newCourse == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param course
     * @return 返回成功条数
     */
    public Integer update(Course course){
        // 入参校验
        if(course == null || course.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<Course> optional = courseRepository.findById(course.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ course.getId() +"的Course");
        }
        Course dbCourse = optional.get();
        //把不为null的属性拷贝到dbCourse
        CopyUtil.notNullCopy(course, dbCourse);
        //执行保存操作
        Course updateCourse = courseRepository.save(dbCourse);
        return updateCourse == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        courseRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param courseIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> courseIdList){
        List<Course> delCourseList = courseRepository.findAllById(courseIdList);
        courseRepository.deleteInBatch(delCourseList);
        return delCourseList.size();
    }


}
