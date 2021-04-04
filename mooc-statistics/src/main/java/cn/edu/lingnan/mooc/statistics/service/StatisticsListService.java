package cn.edu.lingnan.mooc.statistics.service;

import cn.edu.lingnan.mooc.statistics.authentication.util.UserUtil;
import cn.edu.lingnan.mooc.statistics.constant.Constant;
import cn.edu.lingnan.mooc.statistics.constant.EsConstant;
import cn.edu.lingnan.mooc.statistics.entity.CourseRecordStatisticsVO;
import cn.edu.lingnan.mooc.statistics.entity.StatisticsListViewQuery;
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
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.ParsedCardinality;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xiaomingzhang
 * @date 2021/04/01
 */
@Slf4j
@Service
public class StatisticsListService {

    @Resource
    private RestHighLevelClient restHighLevelClient;


    public Object getCourseStatisticsList(StatisticsListViewQuery query) {

        log.info("===========课程统计报表===========start===========");
        Map<Integer, CourseRecordStatisticsVO> CourseRecordVOMap = new HashMap<>();

        Integer dsfUserId = UserUtil.getUserId();

        List<String> accountList = null;
        // 查询关键字
/*        if (StringUtils.isNotBlank(query.getKeyword())){
            log.info("===========课程统计报表===========查询关键字===========");
            ResultMessage<List<String>> keywordListResult = logAuditFeign.listPermissionAccountByKeyword(null, query.getKeyword(), dsfUserId, null, false,false);
            accountList = keywordListResult.getData();
            if (CollectionUtils.isEmpty(accountList)){
                return new PageUtils(0L, query.getPageSize(), 0L, 0);
            }
        }
        // 查询有权限的人员
        if (!Constant.SUPER_DSF_USER_ID.equals(dsfUserId) && StringUtils.isBlank(query.getKeyword())) {
            log.info("===========课程统计报表===========查询有权限的人员===========");
            long accountBeginTime = System.currentTimeMillis();
            // 先查询是否有根部门的权限
            Boolean isAdminPermission = addressbookService.isOwnTopDeptIdByAdminId(dsfUserId);
            if (!isAdminPermission) {
                // 如果有根部门权限，走超管逻辑；没有根部门权限，再获取有权限的人员
                ResultMessage<List<String>> accountListResult = logAuditFeign.listPermissionAccountByKeyword(null, query.getKeyword(), dsfUserId, null, false,false);
                accountList = accountListResult.getData();
                if (CollectionUtils.isEmpty(accountList)) {
                    return new PageUtils(0L, query.getPageSize(), 0L, 0);
                }
            }
            log.info("==========人员总数耗时:{}===========", System.currentTimeMillis() - accountBeginTime);
        }*/
        // 判断时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long beginTime = null;
        Long endTime = null;
        try {
            beginTime = sdf.parse(query.getBeginTime() + Constant.ZERO_TIME).toInstant().toEpochMilli();
            endTime = sdf.parse(query.getEndTime() + Constant.END_TIME).toInstant().toEpochMilli();
        } catch (ParseException e) {
            log.error("=====时间格式转换出问题 error=====");
        }

        // 模糊查询匹配的索引
        String[] esIndexNameArray = new String[]{"elasticSerchPropertis.getUserDailyChatRecordIndex() + Constant.INDEX_NAME_POSTFIX"};

        log.info("===========课程统计报表===========查询总数===========");
        // 查询总数
        long countBeginTime = System.currentTimeMillis();
        //获取boolQuery条件
        BoolQueryBuilder boolQueryAgg = this.getCourseRecordBoolQueryAgg(new ArrayList<>(), beginTime, endTime);
        Integer totalCount = countCourseRecordGroupByCourseId(esIndexNameArray, boolQueryAgg);
        long countEndTime = System.currentTimeMillis();
        log.info("==========查询总数耗时:{}===========", countEndTime - countBeginTime);

        if (totalCount == null || totalCount < 1) {
            return null;
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
        // 最后一页需要特殊处理，可能小于pageSize
        int limit = (currPage == totalPage && totalCount % pageSize != 0) ? totalCount % pageSize : pageSize;
        log.info("===========课程统计报表===========查询分页数据，为优化es聚合仅查询排序字段===========");
        // 查询分页数据，为优化es聚合仅查询排序字段
        TermsAggregationBuilder aggBuilder = getCourseTermAggBuilder(startRowNumber, limit, query.getOrderField(), query.getIsAsc());
        long aggBeginTime = System.currentTimeMillis();

        //1、去es查询，根据查询排序字段结果
        SearchResponse searchResponse = this.initSearchRequestWithAgg(boolQueryAgg, aggBuilder,esIndexNameArray , "_doc", 0, 0);

        long aggEndTime = System.currentTimeMillis();
        log.info("==========agg总数耗时:{}===========", aggEndTime - aggBeginTime);

        Aggregation aggResponse = searchResponse.getAggregations().get(EsConstant.GROUP_BY_AGG);
        ParsedStringTerms terms = ((ParsedStringTerms) aggResponse);
        terms.getBuckets().stream().forEach(bucket -> {

            CourseRecordStatisticsVO courseRecordVO = new CourseRecordStatisticsVO();
            // 帐号
            Integer courseId = (Integer) bucket.getKey();
            courseRecordVO.setCourseId(courseId);
            // 点赞数
            ParsedSum viewAgg = bucket.getAggregations().get(EsConstant.VIEW_NUM_AGG);
            if (viewAgg != null){
                courseRecordVO.setViewNum(((Double)viewAgg.getValue()).intValue());
            }
            // 收藏数
            ParsedSum collectionAgg = bucket.getAggregations().get(EsConstant.COLLECTION_NUM_AGG);
            if (collectionAgg != null){
                courseRecordVO.setCollectionNum(((Double)collectionAgg.getValue()).intValue());
            }
            CourseRecordVOMap.put(courseId, courseRecordVO);
        });
        if (CourseRecordVOMap.size() == 0) {
            return null;
        }
        //获取课程idList
        List<Integer> courseIdList = new ArrayList<>(CourseRecordVOMap.keySet());

        //2、根据课程ID查询其他统计字段
        TermsAggregationBuilder detailAgg = getSendMessageAllAggBuilder(query.getOrderField(), query.getIsAsc());
        long agg2BeginTime = System.currentTimeMillis();

        //构造boolQuery,根据课程Id聚合
        BoolQueryBuilder boolQueryAgg2 = this.getCourseRecordBoolQueryAgg(courseIdList, beginTime, endTime);

        SearchResponse detailSearchResponse = this.initSearchRequestWithAgg(boolQueryAgg2, detailAgg, esIndexNameArray , "_doc", 0, 0);
        long agg2EndTime = System.currentTimeMillis();
        log.info("==========agg2总数耗时:{}===========", agg2EndTime - agg2BeginTime);

        Aggregation detailAggResponse = detailSearchResponse.getAggregations().get(EsConstant.GROUP_BY_AGG);
        ParsedStringTerms detailTerms = ((ParsedStringTerms) detailAggResponse);
        detailTerms.getBuckets().stream().forEach(bucket -> {
            // 帐号
            Integer courseId = (Integer) bucket.getKey();
            CourseRecordStatisticsVO courseRecordVO = CourseRecordVOMap.get(courseId);
            // 观看数
            if (courseRecordVO.getViewNum() == null){
                ParsedSum viewAgg = bucket.getAggregations().get(EsConstant.VIEW_NUM_AGG);
                courseRecordVO.setViewNum(((Double)viewAgg.getValue()).intValue());
            }
            // 收藏数
            if (courseRecordVO.getCollectionNum() == null){
                ParsedSum collectionAgg = bucket.getAggregations().get(EsConstant.COLLECTION_NUM_AGG);
                courseRecordVO.setCollectionNum(((Double)collectionAgg.getValue()).intValue());
            }
        });



        log.info("===========课程统计报表===========根据课程Id查询教师、课程名===========");
        // 根据账号查询人员姓名、部门
/*        Set<String> resultAccountSet = CourseRecordVOMap.keySet();
        ResultMessage<Map<String, AddressBookPersonPageVo>> userInfoResult = logAuditFeign.getNameAndImageByAccounts(TokenUtils.createRequestToken(), resultAccountSet.toArray(new String[0]));
        Map<String, AddressBookPersonPageVo> userInfoMap = userInfoResult.getData();
        ResultMessage<Map<String, AddressBookPersonPageVo>> departmentInfoResult = logAuditFeign.getDepartmentNameByAccounts(TokenUtils.createRequestToken(), resultAccountSet.toArray(new String[0]));
        Map<String, AddressBookPersonPageVo> departmentInfoMap = departmentInfoResult.getData();
        CourseRecordVOMap.forEach((account, sendMessageVO) -> {
            AddressBookPersonPageVo userInfo = userInfoMap.get(account);
            if (userInfo != null){
                sendMessageVO.setUsername(userInfo.getName());
            }
            AddressBookPersonPageVo departmentInfo = departmentInfoMap.get(account);
            if (departmentInfo != null){
                sendMessageVO.setDepartmentName(departmentInfo.getDepartmentName());
            }
        });*/
        log.info("===========课程统计报表===========根据课程Id查询教师、课程名===========");


        log.info("===========课程统计报表===========end===========");
        return null;
    }

    /**
     * 获取课程记录boolQuery条件
     * @param courseIdList
     * @param beginTime
     * @param endTime
     * @return
     */
    private BoolQueryBuilder getCourseRecordBoolQueryAgg(List<Integer> courseIdList, Long beginTime, Long endTime){
        //1、构建查询条件boolQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //课程Id
        if (!CollectionUtils.isEmpty(courseIdList)) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("courseId", courseIdList));
        }
        //设置时间范围
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createTime");
        if (beginTime != null) {
            rangeQueryBuilder.gte(beginTime);
        }
        if (endTime != null) {
            rangeQueryBuilder.lte(endTime);
        }
        boolQueryBuilder.must(rangeQueryBuilder);
        return boolQueryBuilder;
    }


    /**
     * cardinality聚合查询总数
     * @param
     * @return
     */
    private Integer countCourseRecordGroupByCourseId(String[] exIndexNames,BoolQueryBuilder boolQueryBuilder) {
        //构造Cardinality聚合
        CardinalityAggregationBuilder cardinality = AggregationBuilders.cardinality("groupByAgg").field("courseId");
        //查询
        SearchResponse searchResponse =this.initSearchRequestWithAgg(boolQueryBuilder, cardinality, exIndexNames, "_doc", 0, 0);
        Aggregation aggResponse = searchResponse.getAggregations().get("groupByAgg");
        ParsedCardinality parsedCardinality = (ParsedCardinality) aggResponse;
        Long docCount = parsedCardinality.getValue();
        return docCount.intValue();
    }

    /**
     * 查询课程统计报表的term聚合函数
     *
     * @return
     */
    public TermsAggregationBuilder getCourseTermAggBuilder(Integer offset, Integer size, String orderByField, Boolean isAsc) {
        int totalCount = offset + size;
        if (totalCount < 0){
            totalCount = Integer.MAX_VALUE;
        }
        // 按userId分组
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(EsConstant.GROUP_BY_AGG).field(EsConstant.COURSE_ID);
        termsAggregationBuilder.size(totalCount);
        String orderByAgg;
        switch (orderByField){
            //观看数
            case EsConstant.VIEW_NUM:
                termsAggregationBuilder.subAggregation(AggregationBuilders.sum(EsConstant.VIEW_NUM_AGG).field(EsConstant.VIEW_NUM));
                orderByAgg = EsConstant.VIEW_NUM_AGG;
                break;
            //收藏数
            default:
                termsAggregationBuilder.subAggregation(AggregationBuilders.sum(EsConstant.COLLECTION_NUM_AGG).field(EsConstant.COLLECTION_NUM));
                orderByAgg = EsConstant.COLLECTION_NUM_AGG;
                break;
        }
        // 按具体字段降序或升序
        List<BucketOrder> bucketOrderList = new ArrayList<>();
        bucketOrderList.add(BucketOrder.aggregation(orderByAgg, isAsc));
        bucketOrderList.add(BucketOrder.aggregation(EsConstant.KEY, true));
        termsAggregationBuilder.order(bucketOrderList);
        termsAggregationBuilder.subAggregation(PipelineAggregatorBuilders.bucketSort(EsConstant.ORDER_BY_AGG, null).from(offset).size(size));
        return termsAggregationBuilder;
    }

    /**
     * 查询消息发送报表的聚合函数(查询所有，用于导出及分页二次查询)
     * @param orderByField    排序字段
     * @param isAsc           是否排序
     *
     * @return
     */
    public TermsAggregationBuilder getSendMessageAllAggBuilder(String orderByField, Boolean isAsc) {
        // 按userId分组
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(EsConstant.GROUP_BY_AGG).field(EsConstant.COURSE_ID);
        termsAggregationBuilder.size(Integer.MAX_VALUE);
        if (isAsc != null){
            String orderByAgg;
            switch (orderByField){
                case EsConstant.VIEW_NUM:
                    orderByAgg = EsConstant.VIEW_NUM_AGG;
                    break;
                default:
                    orderByAgg = EsConstant.COLLECTION_NUM_AGG;
                    break;
            }
            termsAggregationBuilder.order(BucketOrder.aggregation(orderByAgg, isAsc));
        }
        // 观看人数、收藏人数
        termsAggregationBuilder.subAggregation(AggregationBuilders.sum(EsConstant.VIEW_NUM_AGG).field(EsConstant.VIEW_NUM));
        termsAggregationBuilder.subAggregation(AggregationBuilders.sum(EsConstant.COLLECTION_NUM_AGG).field(EsConstant.COLLECTION_NUM));
        return termsAggregationBuilder;
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
            log.error("==========es查询异常,index={}, type={}, es语句={},exception=",index.toString(),types, searchRequest.source().toString(),e);
            throw new RuntimeException("es查询异常");
        }
        return searchResponse;
    }
}