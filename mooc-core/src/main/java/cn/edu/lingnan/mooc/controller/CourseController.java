package cn.edu.lingnan.mooc.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.model.Course;
import cn.edu.lingnan.mooc.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/10/13
 */
@RestController
@RequestMapping("/admin/courses")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class CourseController {

    @Autowired
    private CourseService courseService;

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
     * url: /admin/courses/course
     * @param course
     * @return
     */
    @PostMapping("/course")
    public RespResult insertOrUpdate(@RequestBody Course course) {
        Integer flag = courseService.insertOrUpdate(course);
        if (flag == 0) {
            return RespResult.fail("新增Course失败");
        }
        return RespResult.success("新增Course成功");
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
        Integer flag = courseService.deleteById(id);
        if (flag == 0) {
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