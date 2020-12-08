package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.enums.CourseEnum;
import cn.edu.lingnan.core.repository.CourseRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.core.vo.CourseVO;
import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.core.entity.Course;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import javax.annotation.Resource;
import java.util.*;

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
    public PageVO<CourseVO> findPage(Course matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<Course> example = Example.of(matchObject,matcher);
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"createTime");
        Page<Course> coursePage = courseRepository.findAll(example, pageable);
        List<Course> courseList = coursePage.getContent();

        List<CourseVO> courseVOList = new ArrayList<>();
        // 获取教师名字map
        Map<Integer,String> teacherNameMap = new HashMap<>();
        teacherNameMap.put(1,"肖明章");

        // courseList -> courseVOList
        courseList.forEach(course -> courseVOList.add(createCourseVO(course,teacherNameMap)));

        /* 4. 封装到自定义分页结果 */
        PageVO<CourseVO> pageVO = new PageVO<>();
        pageVO.setContent(courseVOList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(coursePage.getTotalPages());
        return pageVO;
    }

    private CourseVO createCourseVO(Course course,Map<Integer,String> teacherNameMap){
        // 基本信息
        CourseVO courseVO = CopyUtil.copy(course, CourseVO.class);
        // 设置教师名
        courseVO.setTeacherName(teacherNameMap.getOrDefault(1,"未知老师"));
        // 转换状态为文本
        courseVO.setStatus(CourseEnum.getText(course.getStatus()));
        return courseVO;
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