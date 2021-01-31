package cn.edu.lingnan.core.service.reception;

import cn.edu.lingnan.core.entity.Course;
import cn.edu.lingnan.core.entity.MoocUser;
import cn.edu.lingnan.core.repository.CourseRepository;
import cn.edu.lingnan.core.repository.MoocUserRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.core.vo.ChapterVO;
import cn.edu.lingnan.core.vo.reception.CourseDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

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
            courseDetailVO.setChapterVOList(chapterVOList);

            return courseDetailVO;
        }


        return null;
    }


}
