package cn.edu.lingnan.mooc.statistics.service;

import cn.edu.lingnan.mooc.statistics.repository.EsCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xmz
 * @date: 2021/01/02
 */
@Service
public class CourseService {

    @Resource
    private EsCourseRepository esCourseRepository;



}
