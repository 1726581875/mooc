package cn.edu.lingnan.mooc.statistics.dao.repository;

import cn.edu.lingnan.mooc.statistics.model.entity.es.EsCourse;
import cn.edu.lingnan.mooc.statistics.model.entity.mysql.Course;
import cn.edu.lingnan.mooc.statistics.dao.repository.es.EsCourseRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.io.IOException;
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

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void testElasticsearchTemplate() throws IOException {

        String keyword = "spring";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("name",keyword));
        boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("summary",keyword));
        // 构件SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("mooc_course");
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();

        SearchHit[] hits1 = hits.getHits();
        String sourceAsString = hits1[0].getSourceAsString();
        System.out.println(sourceAsString);
    }

    @Test
    public void esFindAllTest(){
        esCourseRepository.findAll().forEach(System.out::println);
    }

    /**
     * 测试根据课程名和课程简介检索课程
     */
    @Test
    public void testFindByQuery(){
        String keyword = "spring";
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
        esCourse.setTeacherId(course.getTeacherId());
        esCourse.setCreateTime(course.getCreateTime());
        esCourse.setUpdateTime(course.getUpdateTime());
        return esCourse;
    }

    @Test
    private void testGetCourseAccount(){

    }


/*
    https://dt2008.cn/archives/416.html
    @Test
    public void test8() throws Exception {

        BoolQueryBuilder parentQueryBuilder = QueryBuilders.boolQuery();
        //3. 创建搜索请求构建对象(封装查询条件)(***)
        searchRequestBuilder.setQuery(QueryBuilders.queryStringQuery("分布式"));

        //一、先设计高亮的样式: <font color='red'>笔记本</font>
        //HighlightBuilder: 构建高亮的样式
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //1.1 需要高亮的Field名称（可以是1个或n个）
        highlightBuilder.field("title").field("content");
        //1.2 前缀
        highlightBuilder.preTags("<font color='red'>");
        //1.3 后缀
        highlightBuilder.postTags("</font>");
        searchRequestBuilder.highlighter(highlightBuilder);

        //二、执行关键词查询
        //4. 执行请求,得到搜索响应对象
        SearchResponse searchResponse = searchRequestBuilder.get();

        //5. 获取搜索结果
        SearchHits hits = searchResponse.getHits();

        //6. 迭代搜索结果
        System.out.println("总命中数："+hits.totalHits);
        for(SearchHit hit:hits){
            //三、获取高亮内容，进行展示
            */
/**
             * {
             *     "title":"<font color='red'>笔记本</font>",
             *     "content":"<font color='red'>笔记本</font>"
             * }
             *//*

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();

            //取出结果
            //System.out.println(hit.getSourceAsMap());

            System.out.println(hit.getSourceAsMap().get("id"));
            //展示高亮的内容:highlightFields.get("title").getFragments()[0].toString()

            //判断该内容有没有高亮
            if(highlightFields.get("title")!=null){
                System.out.println(highlightFields.get("title").getFragments()[0].toString());
            }else{
                System.out.println(hit.getSourceAsMap().get("title"));
            }

            if(highlightFields.get("content")!=null){
                System.out.println(highlightFields.get("content").getFragments()[0].toString());
            }else{
                System.out.println(hit.getSourceAsMap().get("content"));
            }

            System.out.println("===================");
        }


    }

*/


}
