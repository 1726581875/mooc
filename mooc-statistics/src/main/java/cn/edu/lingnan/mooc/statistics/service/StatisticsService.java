package cn.edu.lingnan.mooc.statistics.service;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.statistics.authentication.util.UserUtil;
import cn.edu.lingnan.mooc.statistics.entity.StatisticsOverviewDailyCountVO;
import cn.edu.lingnan.mooc.statistics.mapper.CourseMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.metrics.ParsedSingleValueNumericMetricsAggregation;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTimeZone;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

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
    @Resource(name = "asyncPromiseExecutor")
    private Executor executor;
    /**
     * 获取统计数据
     * @return
     */
    public RespResult getCourseStatistics(Date startTime,Date endTime){




       return RespResult.success();
    }



    /**
     * 获取收藏数统计
     * @return
     */
    public Map<String,Integer> getCourseCollectionStatistics(){




        return null;
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
     * 统计时间段内客户联系趋势数据
     * 1.每天的发起申请数
     * 2.每天的新增客户数
     * 3.每天的删除/拉黑客户数
     * 流程：
     * 1.参数校验并获取开始和结束时间
     * 2.获取权限人员数据
     * 3.获取白名单成员
     * 4.按时间分组，封装ES语句
     * 5.多线程请求ES获取每天的发起申请数/每天的新增客户数/每天的删除/拉黑客户数
     * @param timeType
     * @return
     */
    public Map<String, Object> getCollectionAndViewTrend(String timeType) {
        // 判断时间参数。开始时间根据参数取1天前、1周前或1个月前的最早毫秒时间戳，结束时间取昨天最晚的毫秒时间戳
        if (StringUtils.isBlank(timeType)) {
            throw new RuntimeException("时间为空");
        }
        int mapSize = 3;
        Map<String, Object> trendData = new HashMap<>(mapSize);

        long beginTime = getBeginTimeByTimeType(timeType);
        long endTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        // 判断是否有缓存
  /*      Long dsfUserId = ShiroUtils.getDsfUserId();
        boolean getCache = getCacheCustomerContactTrend(trendData, dsfUserId, timeType);
        if (getCache){
            return trendData;
        }
        // 判断有权限的人员
        List<String> accountList = new ArrayList<>();
        if (!Constant.SUPER_DSF_USER_ID.equals(dsfUserId)) {
            // 先查询是否有根部门的权限
            Boolean isAdminPermission = addressbookService.isOwnTopDeptIdByAdminId(dsfUserId);
            if (!isAdminPermission) {
                // 如果有根部门权限，走超管逻辑；没有根部门权限，再获取有权限的人员
                ResultMessage<List<String>> accountListResult = logAuditFeign.listPermissionAccount(TokenUtils.createRequestToken(), dsfUserId);
                accountList = accountListResult.getData();
                if (CollectionUtils.isEmpty(accountList)) {
                    return getEmptyCustomerContactTrend(beginTime, endTime);
                }
            }
        }
        // 白名单
        ResultMessage<List<String>> whiteAccountResult = logAuditFeign.listWhiteAccount(TokenUtils.createRequestToken());
        List<String> whiteAccount = whiteAccountResult.getData();*/
        // 按时间分组

        CountDownLatch countDownLatch = new CountDownLatch(mapSize);
        executor.execute(() -> {
            try{
                // 1.每天的发起申请数
                String key = "logaudit:statistics:countUserDailyChatRecordByField:";
                List<StatisticsOverviewDailyCountVO> statisticsList = countUserDailyChatRecordByField(beginTime,endTime,"collection", "sum");
                //cacheStatisticsList(key, statisticsList, STATISTICS_CACHE_EXPIRE_MINUTES);
                trendData.put("dailyNewApplyCnt", statisticsList);
            }catch (Exception e){
                log.error("====================每天的发起申请数，多线程查询异常====================", e);
            }
            countDownLatch.countDown();
        });
        executor.execute(() -> {
            try{
                // 2.每天的新增客户数
                String key = "logaudit:statistics:countUserDailyChatRecordByField:";
                List<StatisticsOverviewDailyCountVO> statisticsList = countUserDailyChatRecordByField(beginTime,endTime,"view", "sum");
               // cacheStatisticsList(key, statisticsList, STATISTICS_CACHE_EXPIRE_MINUTES);
                trendData.put("dailyNewContactCnt", statisticsList);
            }catch (Exception e){
                log.error("====================每天的新增客户数，多线程查询异常====================", e);
            }
            countDownLatch.countDown();
        });
        executor.execute(() -> {
            try{
                // 3.每天的删除/拉黑客户数
                String key = "logaudit:statistics:countUserDailyChatRecordByField:";
                List<StatisticsOverviewDailyCountVO> statisticsList = countUserDailyChatRecordByField(beginTime,endTime,"view", "sum");
                //cacheStatisticsList(key, statisticsList, STATISTICS_CACHE_EXPIRE_MINUTES);
                trendData.put("dailyNegativeFeedbackCnt", statisticsList);
            }catch (Exception e){
                log.error("====================每天的删除/拉黑客户数，多线程查询异常====================", e);
            }
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (Exception e) {
            log.error("====================统计时间段内客户联系趋势数据，多线程查询异常====================", e);
        }
        return trendData;
    }
    private Long getBeginTimeByTimeType(String timeType) {
        LocalDate now = LocalDate.now();
        switch (timeType) {
            case "lastDay":
                now = now.minusDays(1);
                break;
            case "lastWeek":
                now = now.minusDays(7);
                break;
            case "lastMonth":
                now = now.minusDays(30);
                break;
            default:
                now = now.minusDays(7);
                break;
        }
        return now.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }


    public List<StatisticsOverviewDailyCountVO> countUserDailyChatRecordByField(Long beginTime,Long endTime, String countField, String countType) {
        log.info("===========统计时间段内每天的发起申请数 或 新增客户数 或 删除/拉黑客户数===========begin===========");
        Map<String, Long> dailyCount = initDailyCount(beginTime, endTime);
        // 构造agg
        DateHistogramAggregationBuilder dateAgg = AggregationBuilders.dateHistogram("dateAgg").field("statTime").timeZone(DateTimeZone.forOffsetHours(8));
        dateAgg.dateHistogramInterval(DateHistogramInterval.days(1));
        dateAgg.subAggregation(AggregationBuilders.sum("countAgg").field(countField));

        //构造boolQuery条件
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("userId", "xiaomingzhang"));
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("statTime");
        if (beginTime != null) {
            rangeQueryBuilder.gte(beginTime);
        }
        if (endTime != null) {
            rangeQueryBuilder.lte(endTime);
        }
        queryBuilder.must(rangeQueryBuilder);

        // 模糊查询匹配的索引
        String[] esIndexNameArray = new String[]{"index"};

        SearchResponse searchResponse = initSearchRequestWithAgg(queryBuilder, dateAgg, esIndexNameArray, "_doc", 0, 0);
        SearchResponse response = searchResponse;
        Histogram histogram = response.getAggregations().get("dateAgg");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
        for (Histogram.Bucket entry : histogram.getBuckets()) {
            // 转换时区，如2020-03-23T00:00:00.000+08:00
            String utcTimeStr = entry.getKeyAsString();
            Date date;
            try {
                date = sdf.parse(utcTimeStr);
            } catch (ParseException e) {
                log.error("=====parse error=====");
                throw new RuntimeException("=====parse error=====");
            }
            String dateStr = sdf2.format(date);
            Aggregation aggResponse = entry.getAggregations().get("countAgg");
            ParsedSingleValueNumericMetricsAggregation countAggValue = ((ParsedSingleValueNumericMetricsAggregation) aggResponse);
            double count = countAggValue.value();
            dailyCount.put(dateStr, Double.isInfinite(count) ? 0 : ((Double)count).longValue());
        }
        log.info("===========统计时间段内每天的发起申请数 或 新增客户数 或 删除/拉黑客户数===========end===========");
        return buildDailyCountVOList(dailyCount);
    }
    /**
     * 构造每日数量集合
     *
     * @param dailyCount
     * @return
     */
    private List<StatisticsOverviewDailyCountVO> buildDailyCountVOList(Map<String, Long> dailyCount) {
        List<StatisticsOverviewDailyCountVO> dailyCountVOList = dailyCount.keySet().stream().map(key -> {
            StatisticsOverviewDailyCountVO dailyCountVO = new StatisticsOverviewDailyCountVO();
            dailyCountVO.setDate(key);
            dailyCountVO.setCount(dailyCount.get(key));
            return dailyCountVO;
        }).collect(Collectors.toList());
        return dailyCountVOList;
    }


    /**
     * 初始化查询请求
     *
     * @param boolQueryBuilder 请求的查询条件
     * @return
     */
    public SearchResponse initSearchRequestWithAgg(BoolQueryBuilder boolQueryBuilder, AggregationBuilder aggregationBuilder,
                                                   String[] index, String types, Integer offset, Integer size) {
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
            //排序
            searchSourceBuilder.sort("id", SortOrder.ASC);
        }
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("=======================查询异常：{}", e);
            throw new RuntimeException("es查询异常");
        }
        return searchResponse;
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
     * 获取用户分类统计数据
     *  1、查询缓存
     * @return map <分类名，课程数量>
     */
    public List<Map<String,Object>> getClassificationStatistics() {
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
