package cn.edu.lingnan.mooc.statistics.constant;

/**
 * @author xmz
 * @date: 2021/02/28
 */
public interface EsConstant {


    /**
     * 统计类型
     */
    String SUM = "sum";

    /**
     * es字段收藏数
     */
    String COLLECTION_NUM = "collectionNum";
    /**
     * es字段观看数
     */
    String VIEW_NUM = "viewNum";

    /**
     * es字段创建时间
     */
    String CREATE_TIME = "createTime";

    /**
     * es字段教师id
     */
    String TEACHER_ID = "teacherId";

    /**
     * 聚合字段dataAgg
     */
    String DATA_AGG = "dataAgg";

    /**
     * 聚合字段countAgg
     */
    String COUNT_AGG = "countAgg";

    /**
     * 聚合字段groupAgg
     */
    String GROUP_BY_AGG = "groupByAgg";
    /**
     * 聚合字段order by
     */
    String ORDER_BY_AGG = "orderByAgg";

    /**
     * es字段courseId
     */
    String COURSE_ID = "courseId";

    /**
     * 读取key
     */
    String KEY = "_key";

    /**
     * 收藏数agg
     */
    String COLLECTION_NUM_AGG = "collectionNumCountAgg";
    /**
     * 观看人数agg
     */
    String VIEW_NUM_AGG = "collectionNumCountAgg";

}
