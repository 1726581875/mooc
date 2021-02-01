package cn.edu.lingnan.core.service.reception;

import cn.edu.lingnan.core.entity.Course;
import cn.edu.lingnan.core.entity.MoocUser;
import cn.edu.lingnan.core.repository.CourseRepository;
import cn.edu.lingnan.core.repository.MoocUserRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.core.vo.ChapterVO;
import cn.edu.lingnan.core.vo.CourseVO;
import cn.edu.lingnan.core.vo.reception.CourseDetailVO;
import cn.edu.lingnan.core.vo.reception.ReceptionCourseVO;
import cn.edu.lingnan.mooc.common.model.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author xmz
 * @date: 2021/01/31
 * 前台 课程service类
 */
@Service
public class ReceptionCourseService {

    @Resource
    private CourseRepository courseRepository;
    @Resource
    private MoocUserRepository moocUserRepository;
    @Autowired
    private ReceptionChapterService receptionChapterService;

    /**
     * 根据可从Id，查询课程详情
     * 1、获取课程基本信息
     * 2、获取课程教师信息
     * 3、获取章节信息
     * @param courseId
     * @return
     */
    public CourseDetailVO findCourseDetailById(Integer courseId){

        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if(courseOptional.isPresent()){
            //获取课程基本信息
            Course course = courseOptional.get();
            CourseDetailVO courseDetailVO = CopyUtil.copy(course, CourseDetailVO.class);
            //获取教师基本信息,设置到VO对象
            Optional<MoocUser> teacherOptional = moocUserRepository.findById(course.getTeacherId());
            courseDetailVO.setTeacher(teacherOptional.get());
            //获取章节信息,设置到VO对象
            List<ChapterVO> chapterVOList = receptionChapterService.findAllChapterByCourseId(courseId);
            courseDetailVO.setChapterList(chapterVOList);

            return courseDetailVO;
        }


        return null;
    }

   public PageVO<ReceptionCourseVO> findCourseByTeachId(Integer teacherId,Integer pageIndex,Integer pageSize){
       //构造匹配条件Example对象
       Course matchObject = new Course();
       matchObject.setTeacherId(teacherId);
       Example<Course> example = Example.of(matchObject);
       Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"createTime");
       Page<Course> coursePage = courseRepository.findAll(example, pageable);
       List<Course> courseList = coursePage.getContent();
       //对象转换，属性值复制
       List<ReceptionCourseVO> courseVOList = CopyUtil.copyList(courseList,ReceptionCourseVO.class);

       /* 4. 封装到自定义分页结果 */
       PageVO<ReceptionCourseVO> pageVO = new PageVO<>();
       pageVO.setContent(courseVOList);
       pageVO.setPageIndex(pageIndex);
       pageVO.setPageSize(pageSize);
       pageVO.setPageCount(coursePage.getTotalPages());
       return pageVO;
   }




}
