package cn.edu.lingnan.mooc.portal.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.portal.model.param.QueryCourseParam;
import cn.edu.lingnan.mooc.portal.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xmz
 * @date: 2021/01/31
 */
@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService receptionCourseService;

    /**
     * 根据标签查询课程
     *
     * @param queryCourseParam
     * @return
     */
    @PostMapping("/getByTag")
    public RespResult findCourseByTagIdList2(@RequestBody QueryCourseParam queryCourseParam) {
        if (queryCourseParam.getPageIndex() == null && queryCourseParam.getPageIndex() < 1) {
            queryCourseParam.setPageIndex(1);
        }
        if (queryCourseParam.getPageSize() == null) {
            queryCourseParam.setPageSize(15);
        }
        return RespResult.success(receptionCourseService.getCourseByTagList(queryCourseParam.getTagIdList(),
                queryCourseParam.getPageIndex(), queryCourseParam.getPageSize()));
    }

    /**
     * 根据课程Id查询课程基本信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public RespResult findCourseById(@PathVariable Long id) {
        return RespResult.success(receptionCourseService.findCourseDetailById(id));
    }

    /**
     * 根据教师id查询开设的课程
     *
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/listByTeachId")
    public RespResult findCourseByTeachId(@RequestParam(value = "userId") Long userId,
                                          @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                          @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {

        return RespResult.success(receptionCourseService.findCourseByTeachId(userId, pageIndex, pageSize));
    }


    /**
     * 根据用户id查询收藏的课程
     *
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/collection/list")
    public RespResult findCollectionCourseByUserId(@RequestParam(value = "userId") Long userId,
                                                   @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                   @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {

        return RespResult.success(receptionCourseService.findCollectionCourseByUserId(userId, pageIndex, pageSize));
    }

    /**
     * 点击收藏或者取消收藏
     *
     * @return
     */
    @PutMapping("/collectionOrCancel/{courseId}")
    public RespResult collectionOrCancel(@PathVariable Long courseId) {
        receptionCourseService.collectionOrCancel(courseId);
        return RespResult.success();
    }


}
