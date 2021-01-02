package cn.edu.lingnan.mooc.statistics.repository;

import cn.edu.lingnan.mooc.statistics.entity.es.EsCourse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author xmz
 * @date: 2020/12/30
 */
public interface EsCourseRepository extends ElasticsearchRepository<EsCourse,Integer> {

}
