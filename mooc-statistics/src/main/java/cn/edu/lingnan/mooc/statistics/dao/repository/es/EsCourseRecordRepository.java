package cn.edu.lingnan.mooc.statistics.dao.repository.es;

import cn.edu.lingnan.mooc.statistics.model.entity.es.EsCourse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author xmz
 * @date: 2021/04/14
 */
public interface EsCourseRecordRepository extends ElasticsearchRepository<EsCourse,Integer> {

}
