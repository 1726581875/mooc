package cn.edu.lingnan.mooc.statistics.repository.es;

import cn.edu.lingnan.mooc.statistics.entity.es.EsCourse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/12/30
 */
public interface EsCourseRepository extends ElasticsearchRepository<EsCourse,Integer> {

}
