package cn.edu.lingnan.mooc.core.constant;

/**
 * @author xmz
 * @date: 2021/02/07
 * redis前缀的常类类
 */
public class RedisPrefixConstant {

    /**
     * 缓存的时间
     */
    public final static Integer CACHE_DAY_NUM = 2;

    /**
     * 课程收藏数缓存前缀
     */
    public final static String COLLECTION_NUM_PRE = "course:collection:";

    /**
     * 课程浏览数缓存前缀
     */
    public final static String VIEW_NUM_PRE = "course:view:";

    /**
     * 课程评论数缓存前缀
     */
    public final static String COMMENT_NUM_PRE = "course:comment:";
    /**
     * 课程问答数缓存前缀
     */
    public final static String QUESTION_NUM_PRE = "course:question:";

}
