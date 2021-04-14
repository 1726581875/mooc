package cn.edu.lingnan.mooc.statistics.controller;

import cn.edu.lingnan.mooc.statistics.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xmz
 * @date: 2021/01/02
 */
@RestController
@RequestMapping("/courses")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class CourseController {
    @Autowired
    private CourseService courseService;






}
