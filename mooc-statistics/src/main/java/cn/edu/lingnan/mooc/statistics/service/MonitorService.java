package cn.edu.lingnan.mooc.statistics.service;

import cn.edu.lingnan.mooc.statistics.entity.mysql.LoginAmountCount;
import cn.edu.lingnan.mooc.statistics.mapper.CourseMapper;
import cn.edu.lingnan.mooc.statistics.repository.LoginAmountCountRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xiaomingzhang
 * @date 2021/1/07
 */
@Slf4j
@Service
public class MonitorService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private LoginAmountCountRepository loginAmountCountRepository;
    @Resource
    private CourseMapper courseMapper;

    /**
     * 获取一周前的新建课程数
     * @return
     */
    public Map<String,Long> countWeekCourse() {

        // 设置开始时间为6天前
        Calendar begin = Calendar.getInstance();
        begin.setTime(new Date());
        begin.set(Calendar.DATE, begin.get(Calendar.DATE) - 6);
        Long beginTime = begin.getTimeInMillis();
        // 结束时间为明天
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        end.add(Calendar.DAY_OF_WEEK,1);
        Long endTime = end.getTimeInMillis();

        // 调用统计方法，统计 前六天 + 今天的新建课程数
        Map<String, Long> countCourseMap = this.countCourseCreateByTime(beginTime, endTime);

        return countCourseMap;
    }
    /**
     * 获取一个月前的新建课程数
     * @return
     */
    public Map<String,Long> countMonthCourse() {

        // 设置开始时间为一个月前
        Calendar begin = Calendar.getInstance();
        begin.setTime(new Date());
        begin.add(Calendar.MONTH,-1);
        Long beginTime = begin.getTimeInMillis();
        // 结束时间为明天
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        end.add(Calendar.DAY_OF_WEEK,1);
        Long endTime = end.getTimeInMillis();

        // 调用统计方法，统计 前六天 + 今天的新建课程数
        Map<String, Long> countCourseMap = this.countCourseCreateByTime(beginTime, endTime);

        return countCourseMap;
    }


    /**
     * 统计对应时间内的每天创建课程数
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public Map<String,Long> countCourseCreateByTime(Long beginTime,Long endTime){


        // 初始化map
        Map<String, Long> dailyCountMap = initDailyCount(beginTime, endTime);
        // 构造时间聚合
        DateHistogramAggregationBuilder dateAgg = AggregationBuilders.dateHistogram("dateAgg").field("createTime").timeZone(DateTimeZone.forOffsetHours(8));
        dateAgg.dateHistogramInterval(DateHistogramInterval.days(1));
        // 构造条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createTime");
        rangeQueryBuilder.gte(beginTime);
        rangeQueryBuilder.lte(endTime);
        boolQueryBuilder.must(rangeQueryBuilder);

        // 执行查询
        SearchResponse response = initSearchRequestWithAgg(boolQueryBuilder, dateAgg,
                "mooc_course", "_doc", 0, 0);
        if(response != null) {
            Histogram histogram = response.getAggregations().get("dateAgg");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM-dd");
            for (Histogram.Bucket entry : histogram.getBuckets()) {
                // 获取key
                String utcTimeStr = entry.getKeyAsString();
                Date date = null;
                try {
                    // 转换时间格式
                    date = simpleDateFormat1.parse(utcTimeStr);
                } catch (ParseException e) {
                    log.error("=====parse error=====", e);
                }
                String dateStr = simpleDateFormat2.format(date);
                dailyCountMap.put(dateStr, entry.getDocCount());
            }
        }else {
            //去mysql查
            List<Map<String, Object>> mapList = courseMapper.countNewAddCourseNum(new Date(beginTime),new Date(endTime));
            //遍历查询结果
            mapList.forEach(map -> {
                    //遍历返回结果，赋值
                    dailyCountMap.forEach((data,num) -> {
                        if(map.get("data").toString().contains(data)){
                            dailyCountMap.put(data,(Long) map.get("count"));
                        }
                    });

            });

        }
        return dailyCountMap;
    }
    /**
     * 初始化每天对应数量集合
     *
     * @param beginTime
     * @param endTime
     */
    private Map<String, Long> initDailyCount(Long beginTime, Long endTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        Map<String, Long> dailyCount = new LinkedHashMap<>();
        // 起始时间
        Date beginDate = new Date(beginTime);
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        // 结束时间
        Date endDate = new Date(endTime);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        while (beginCalendar.before(endCalendar)) {
            String dateStr = simpleDateFormat.format(beginCalendar.getTime());
            dailyCount.put(dateStr, 0L);
            beginCalendar.add(Calendar.DATE, 1);
        }
        return dailyCount;
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
     * 获取七天前的系统登录人数
     * @return
     */
    public Map<String,Long> countWeekPerson(){
        // 设置开始时间为7天前
        Calendar begin = Calendar.getInstance();
        begin.setTime(new Date());
        begin.set(Calendar.DATE, begin.get(Calendar.DATE) - 6);
        Long beginTime = begin.getTimeInMillis();
        // 结束时间为今天
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        Long endTime = end.getTimeInMillis();

        // 初始化统计的日期对应登录人数map
        Map<String, Long> dailyCountMap = initDailyCount(beginTime, endTime);
        // 查询出对应时间的登录人数
        List<LoginAmountCount> loginAmountCountList = loginAmountCountRepository
                .findLoginAmountCountByTime(begin.getTime(), end.getTime());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        // 构造需要返回的map
        Map<String,Long> countMap = new HashMap<>(loginAmountCountList.size());
        loginAmountCountList.forEach(e -> {
            String formatDate = simpleDateFormat.format(e.getCountTime());
            dailyCountMap.computeIfPresent(formatDate,(k,v)-> v + Long.valueOf(e.getAmount()));
        });
        return dailyCountMap;
    }

    /**
     * 获取近一个月的系统登录人数
     * @return
     */
    public Map<String,Long> countMonthPerson(){
        // 设置开始时间为一个月前
        Calendar begin = Calendar.getInstance();
        begin.setTime(new Date());
        begin.add(Calendar.MONTH,-1);
        Long beginTime = begin.getTimeInMillis();
        // 结束时间为明天
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        Long endTime = end.getTimeInMillis();

        // 初始化统计的日期对应登录人数map
        Map<String, Long> dailyCountMap = initDailyCount(beginTime, endTime);
        // 查询出对应时间的登录人数
        List<LoginAmountCount> loginAmountCountList = loginAmountCountRepository
                .findLoginAmountCountByTime(begin.getTime(), end.getTime());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        // 构造需要返回的map
        Map<String,Long> countMap = new HashMap<>(loginAmountCountList.size());
        loginAmountCountList.forEach(e -> {
            String formatDate = simpleDateFormat.format(e.getCountTime());
            dailyCountMap.computeIfPresent(formatDate,(k,v)-> v + Long.valueOf(e.getAmount()));
        });
        return dailyCountMap;
    }




}
