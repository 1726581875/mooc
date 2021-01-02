package cn.edu.lingnan.mooc.statistics.repository;

import cn.edu.lingnan.mooc.statistics.entity.es.EsCourse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xmz
 * @date: 2020/12/30
 */
@SpringBootTest
public class EsCourseRepositoryTest {

    @Autowired
    private EsCourseRepository esCourseRepository;

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
    }

}
