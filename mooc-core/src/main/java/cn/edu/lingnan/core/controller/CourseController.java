package cn.edu.lingnan.core.controller;

import cn.edu.lingnan.core.authentication.annotation.Check;
import cn.edu.lingnan.core.enums.CourseEnum;
import cn.edu.lingnan.core.param.CourseParam;
import cn.edu.lingnan.core.param.reception.QueryCourseParam;
import cn.edu.lingnan.core.repository.CourseRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.core.vo.CourseVO;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.core.entity.Course;
import cn.edu.lingnan.core.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author xmz
 * @date: 2020/10/13
 */
@RestController
@RequestMapping("/admin/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Resource
    private CourseRepository courseRepository;


    @PostMapping("/changeStatus")
    public RespResult updateCourseStatus(@RequestBody CourseParam courseParam) {
        //设置参数
        Course course = new Course();
        course.setId(courseParam.getId());
        course.setStatus(CourseEnum.getStatusByText(courseParam.getStatus()));
        //更新课程状态
        Integer updateStatus = courseService.update(course);
        return updateStatus == 1 ? RespResult.success("更改课程状态成功") : RespResult.fail("更改课程状态失败");
    }

    /**
     * 前台方法
     * 根据标签查询课程
     * @param queryCourseParam
     * @return
     */
    @PostMapping("/getByTag")
    public RespResult findCourseByTagIdList2(@RequestBody QueryCourseParam queryCourseParam){
        if(queryCourseParam.getPageIndex() == null && queryCourseParam.getPageIndex() < 1){
            queryCourseParam.setPageIndex(1);
        }
        if(queryCourseParam.getPageSize() == null){
            queryCourseParam.setPageSize(10);
        }
        return RespResult.success(courseService.getCourseByTagList(queryCourseParam.getTagIdList(),
                queryCourseParam.getPageIndex(),queryCourseParam.getPageSize()));
    }
    /**
     * 分页查询course接口
     * get请求
     * url: /admin/courses/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(Course matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(courseService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 根据id查询课程
     * @param courseId
     * @return
     */
    @GetMapping("/{courseId}")
    public RespResult findById(@PathVariable("courseId") Integer courseId) {
        return RespResult.success(courseService.findById(courseId));
    }

    /**
     * 更新course接口
     * 请求方法: put
     * url: /admin/courses/{id}
     * @param course
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody Course course) {
        Integer flag = courseService.update(course);
        if (flag == 0) {
            return RespResult.fail("更新Course失败");
        }
        return RespResult.success("更新Course成功");
    }

    /**
     * 插入或更新course
     * 请求方法: post
     * url: /admin/courses/save
     * @param courseParam
     * @return
     */
    @PostMapping("/save")
    public RespResult insertOrUpdate(@RequestBody CourseParam courseParam) {
        Integer flag = courseService.insertOrUpdate(courseParam);
        if (flag == 0) {
            return RespResult.fail();
        }
        return RespResult.success();
    }

    /**
     * 删除course接口
     * 请求方法: delete
     * url: /admin/courses/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if(!courseOptional.isPresent()){
            return RespResult.fail("课程不存在");
        }
        Course course = courseOptional.get();
        course.setStatus(3);
        Course c = courseRepository.save(course);
        if (c == null) {
            return RespResult.fail("删除Course失败");
        }
        return RespResult.success("删除Course成功");
    }

    /**
     * 批量删除course接口
     * 请求方法: post
     * url: /admin/courses/bacth/delete
     * @param courseIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> courseIdList) {
        courseService.deleteAllByIds(courseIdList);
        return RespResult.success("批量删除Course成功");
    }

}