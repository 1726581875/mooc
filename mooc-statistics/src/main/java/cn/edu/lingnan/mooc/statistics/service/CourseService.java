package cn.edu.lingnan.mooc.statistics.service;

import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.statistics.entity.vo.CourseSearchVO;
import cn.edu.lingnan.mooc.statistics.repository.es.EsCourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
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
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    public PageVO<CourseSearchVO> searchCourseByKeyWord(String keyWord, String pageIndex, String pageSize) {
        //获取总数
        Integer totalCount = countCourseNum();

/*
        if (totalCount < 1) {
            return new PageUtils<>(0L, query.getPageSize(), 0L, 0);
        }
        // 总页数
        Integer pageSize = query.getPageSize();
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        // 起始行数
        Integer currPage = query.getCurrPage();
        if (currPage <= 0) {
            currPage = 1;
        } else if (currPage > totalPage) {
            currPage = totalPage;
        }
        int startRowNumber = (currPage - 1) * pageSize;

        log.info("===========文档搜索===========查询分页数据===========");
        // 5.查询分页id
        //根据文件id分组聚合查询

        TermsAggregationBuilder groupByDocIdAggBuilder = getGroupByDocIdAggBuilder(startRowNumber, pageSize);
        long aggBeginTime = System.currentTimeMillis();
        //聚合查询
        SearchResponse docIdResponse = elasticDocAnalyzeService.getResponseByQueryAndAgg(idBuilder, groupByDocIdAggBuilder);
        log.info("======aggTime:{}", System.currentTimeMillis() - aggBeginTime);
        Aggregation groupByDocId = docIdResponse.getAggregations().get("groupByDocId");
        ParsedLongTerms terms = ((ParsedLongTerms) groupByDocId);
        if (CollectionUtils.isEmpty(terms.getBuckets())) {
            return new PageUtils<>(0L, pageSize, 0L, 0);
        }
        List<? extends Terms.Bucket> list = terms.getBuckets();
        Map<Long, Long> docIdOrderMap = MapUtil.newHashMap(list.size());
        list.forEach(bucket -> {
            Long docId = (Long) bucket.getKey();
            ParsedMin minContentOrder = bucket.getAggregations().get("minContentOrder");
            if (minContentOrder != null) {
                Long order = Double.isInfinite(minContentOrder.getValue()) ? null : ((Double) minContentOrder.getValue()).longValue();
                docIdOrderMap.put(docId, order);
            }
        });
*/



        return null;
    }

    /**
     * 统计课程总数
     * @return
     */
    private Integer countCourseNum() {
        CardinalityAggregationBuilder cardinality = AggregationBuilders.cardinality("groupByAgg").field("id");
        SearchResponse searchResponse = statisticsListService.initSearchRequestWithAgg(null,cardinality,new String[]{"mooc_course"},"_doc",null,null);
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
            highlightBuilder.fields().add(new HighlightBuilder.Field("docContent").fragmentSize(20).numOfFragments(10));
            highlightBuilder.fields().add(new HighlightBuilder.Field("docName"));
        }
        return highlightBuilder;
    }



}
