package cn.edu.lingnan.mooc.statistics.service;

import cn.edu.lingnan.mooc.statistics.repository.es.EsCourseRepository;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xmz
 * @date: 2021/01/02
 */
@Service
public class CourseService {

    @Resource
    private EsCourseRepository esCourseRepository;



    // 根据关键字高亮文档内容、名称的部分字段，高亮前后显示20个字符，高亮个数设置为10
    private HighlightBuilder getHighlightBuilder() {
        HighlightBuilder highlightBuilder = SearchSourceBuilder.highlight();
        if (StringUtils.isNotBlank("esQueryBuilder.getKeyword()")) {
            highlightBuilder.fields().add(new HighlightBuilder.Field("docContent").fragmentSize(20).numOfFragments(10));
            highlightBuilder.fields().add(new HighlightBuilder.Field("docName"));
        }
        return highlightBuilder;
    }



}
