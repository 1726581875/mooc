package cn.edu.lingnan.mooc.statistics.repository;

import cn.edu.lingnan.mooc.statistics.entity.es.EsCourse;
import cn.edu.lingnan.mooc.statistics.entity.mysql.Course;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/12/30
 */
@SpringBootTest
public class EsCourseRepositoryTest {

    @Autowired
    private EsCourseRepository esCourseRepository;
    @Autowired
    private CourseRepository mysqlCourseRepository;

    @Test
    public void esFindAllTest(){
        esCourseRepository.findAll().forEach(System.out::println);
    }

    @Test
    public void testFindByQuery(){
        String keyword = "测试";
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.should(QueryBuilders.matchPhraseQuery("name",keyword));
        queryBuilder.should(QueryBuilders.matchPhraseQuery("summary",keyword));
        Iterable<EsCourse> search = esCourseRepository.search(queryBuilder);
        search.forEach(System.out::println);
    }

    /**
     * 数据迁移，把mysql里的所有数据同步到es
     */
    @Test
    public void dataMigration(){
        // 获取mysql全部课程信息
        List<Course> courseList = mysqlCourseRepository.findAll();
        // 转换为es存储对象
        List<EsCourse> esCourseList = courseList.stream().map(this::createEsCourse).collect(Collectors.toList());
        // 批量插入
        esCourseRepository.saveAll(esCourseList);
    }

    private EsCourse createEsCourse(Course course){
        EsCourse esCourse = new EsCourse();
        esCourse.setId(course.getId());
        esCourse.setName(course.getName());
        esCourse.setSummary(course.getSummary());
        esCourse.setTeachId(course.getTeacherId());
        esCourse.setCreateTime(course.getCreateTime());
        esCourse.setUpdateTime(course.getUpdateTime());
        return esCourse;
    }



}
