package cn.edu.lingnan.mooc.statistics.service;

import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.statistics.entity.mysql.Course;
import cn.edu.lingnan.mooc.statistics.entity.vo.CourseSearchVO;
import cn.edu.lingnan.mooc.statistics.mapper.CourseMapper;
import cn.edu.lingnan.mooc.statistics.repository.es.EsCourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.ParsedCardinality;
import org.elasticsearch.search.aggregations.metrics.min.ParsedMin;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2021/01/02
 */
@Slf4j
@Service
public class CourseService {

    @Resource
    private EsCourseRepository esCourseRepository;
    @Resource
    private StatisticsListService statisticsListService;
    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private CourseMapper courseMapper;

    public PageVO<CourseSearchVO> searchCourseByKeyWord(String keyWord, Integer pageIndex, Integer pageSize){

        log.info("===========课程搜索======开始===============");
        List<CourseSearchVO> courseSearchVOList = new ArrayList<>();

        BoolQueryBuilder boolQuery = this.getBoolQuery(keyWord);

        //1、获取分页总数
        Integer totalCount = countCourseNum(boolQuery);

        if (totalCount < 1) {
            return new PageVO<>(1,10,0,0,new ArrayList<>());
        }

        // 总页数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        // 起始行数
        if (pageIndex <= 0) {
            pageIndex = 1;
        } else if (pageIndex > totalPage) {
            pageIndex = totalPage;
        }
        int startRowNumber = (pageIndex - 1) * pageSize;

        //2、es高亮查询
        HighlightBuilder highlightBuilder = getHighlightBuilder(keyWord);
        SearchResponse searchResponse = this.initSearchRequestWithHighlight(boolQuery,highlightBuilder, startRowNumber,pageSize);
        List<Integer> courseIdList = new ArrayList<>();
        //遍历构造命中数据
        if(searchResponse != null){
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            for (SearchHit hit :searchHits) {

                CourseSearchVO courseSearchVO = new CourseSearchVO();
                //设置课程Id
                Integer courseId = Integer.valueOf(hit.getId());
                courseSearchVO.setCourseId(courseId);

                //课程名
                if (hit.getHighlightFields() != null && hit.getHighlightFields().get("name") != null) {
                    List<String> docContentHighLight = Arrays.stream(hit.getHighlightFields().get("name").fragments()).map(Text::string).collect(Collectors.toList());
                    StringBuilder sb = new StringBuilder();
                    docContentHighLight.forEach(sb::append);
                    courseSearchVO.setCourseName(sb.toString());
                }
                //课程描述
                if (hit.getHighlightFields() != null && hit.getHighlightFields().get("summary") != null) {
                    List<String> docContentHighLight = Arrays.stream(hit.getHighlightFields().get("summary").fragments()).map(Text::string).collect(Collectors.toList());
                    StringBuilder sb = new StringBuilder();
                    docContentHighLight.forEach(sb::append);
                    courseSearchVO.setSummary(sb.toString());
                }
                courseIdList.add(courseId);
                courseSearchVOList.add(courseSearchVO);
            }

        }
        //查询数据库获取课程信息
        List<Course> courseList = courseMapper.getCourseByIdList(courseIdList);
        //map<courseId,course>
        Map<Integer,Course> courseMap = new HashMap<>(courseList.size());
        courseList.forEach(course -> courseMap.put(course.getId(),course));
        //获取教师id
        List<Integer> teacherIdList = courseList.stream().map(Course::getTeacherId).collect(Collectors.toList());
        //查询数据库获取教师名字
        List<Map<String, Object>> teacherNameAndIdMapList = courseMapper.getTeacherNameByIdList(teacherIdList);
        //map<teacherId,name>
        Map<Integer,String> teacherNameMap = new HashMap<>(teacherNameAndIdMapList.size());
        teacherNameAndIdMapList.forEach(map -> teacherNameMap.put(((Long) map.get("id")).intValue(),(String) map.get("name")));

        //3、设置其他字段
        courseSearchVOList.forEach(courseSearchVO -> setCourseSearchVO(courseSearchVO,courseMap,teacherNameMap));


        //构造PageVO返回对象
        PageVO<CourseSearchVO> pageVO = new PageVO<>();
        pageVO.setContent(courseSearchVOList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(totalPage);
        pageVO.setPageTotal(totalCount);

        log.info("===========课程搜索===========结束===========");
        return pageVO;
    }

    /**
     * 设置查询返回对象的其他信息
     * @param courseSearchVO
     * @param courseMap
     * @param teacherNameMap
     * @return
     */
    private CourseSearchVO setCourseSearchVO(CourseSearchVO courseSearchVO,Map<Integer,Course> courseMap, Map<Integer,String> teacherNameMap){
        Course course = courseMap.get(courseSearchVO.getCourseId());
        //课程图片
        courseSearchVO.setCourseImage(course.getImage());
        //设置课程创建时间
        courseSearchVO.setCreateTime(course.getCreateTime());
        //教师Id
        Integer teacherId = course.getTeacherId();
        courseSearchVO.setTeacherId(teacherId);
        //教师名
        courseSearchVO.setTeacherName(teacherNameMap.get(teacherId));
        return courseSearchVO;
    }


    /**
     * 统计课程总数
     * @return
     */
    private Integer countCourseNum(BoolQueryBuilder boolQueryBuilder)  {
        CardinalityAggregationBuilder cardinality = AggregationBuilders.cardinality("groupByAgg").field("id");
        SearchResponse searchResponse = statisticsListService.initSearchRequestWithAgg(boolQueryBuilder, cardinality,new String[]{"mooc_course"},"_doc",null,null);
        Aggregation aggResponse = searchResponse.getAggregations().get("groupByAgg");
        ParsedCardinality parsedCardinality = (ParsedCardinality) aggResponse;
        long docCount = parsedCardinality.getValue();
        return (int) docCount;
    }

    /**
     * 根据关键字高亮文档内容、名称的部分字段，高亮前后显示20个字符，高亮个数设置为10
     */
    private HighlightBuilder getHighlightBuilder(String keyWord) {
        HighlightBuilder highlightBuilder = SearchSourceBuilder.highlight();
        if (StringUtils.isNotBlank(keyWord)) {
            highlightBuilder.fields().add(new HighlightBuilder.Field("summary").fragmentSize(20).numOfFragments(10));
            highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        }
        return highlightBuilder;
    }


    private BoolQueryBuilder getBoolQuery(String keyWord) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("name",keyWord));
        boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("summary",keyWord));
        return boolQueryBuilder;
    }

    private SearchResponse initSearchRequestWithHighlight(BoolQueryBuilder queryBuilder, HighlightBuilder highlightBuilder, Integer offset, Integer size) {
        //设置Index 和 type
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("mooc_course");
        searchRequest.types("_doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(offset);
        searchSourceBuilder.size(size);
        //设置回调参数
        if (highlightBuilder != null) {
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("======查询es异常：{}", e);
            throw new RuntimeException(e);
        }
        return searchResponse;
    }

}
