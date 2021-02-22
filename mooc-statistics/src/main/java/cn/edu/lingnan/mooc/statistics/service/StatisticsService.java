package cn.edu.lingnan.mooc.statistics.service;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.statistics.authentication.util.UserUtil;
import cn.edu.lingnan.mooc.statistics.mapper.CourseMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author xmz
 * @date: 2021/02/22
 */
@Slf4j
@Service
public class StatisticsService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private CourseMapper courseMapper;


    /**
     * 获取统计数据
     * @return
     */
    public RespResult getCourseStatistics(Date startTime,Date endTime){




       return RespResult.success();
    }




    /**
     * 初始化查询条件
     * @param boolQueryBuilder
     * @param aggregationBuilder
     * @param index
     * @param types
     * @param offset
     * @param size
     * @return
     */
    private SearchResponse initSearchRequestWithAgg(BoolQueryBuilder boolQueryBuilder, AggregationBuilder aggregationBuilder,
                                                    String index, String types, Integer offset, Integer size) {
        //设置Index 和 type
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        searchRequest.types(types);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        if (aggregationBuilder != null){
            searchSourceBuilder.aggregation(aggregationBuilder);
        }
        if (null != offset && null != size) {
            // 分页
            searchSourceBuilder.from(offset);
            searchSourceBuilder.size(size);

        }
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("=======================查询异常：{}", e);
        }
        return searchResponse;
    }

    /**
     * 获取用户分类统计数据
     *  1、查询缓存
     * @return map <分类名，课程数量>
     */
    public List<Map<String,Object>> getClassificationStatistics() {
        List<Map<String,Object>> classificationMap = new ArrayList<>();
        String account = UserUtil.getUserToken().getAccount();
        //如果是教师，只统计当前教师
        if( account.startsWith("teacher-")){
            Integer userId = UserUtil.getUserId();
            return courseMapper.countCourseCategoryByUserId(userId);
        }
        //否则就是管理员，统计全部
        return courseMapper.countAllCourseCategory();

    }
}
