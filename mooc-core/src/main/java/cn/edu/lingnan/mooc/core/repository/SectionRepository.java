package cn.edu.lingnan.mooc.core.repository;
import cn.edu.lingnan.mooc.core.model.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author xmz
 * @date 2020/10/31
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface SectionRepository extends JpaRepository<Section, Integer>,JpaSpecificationExecutor<Section> {

    /**
     * 根据chapterIdList 大章idList查询对应小节List
     * @param chapterIdList
     * @return
     */
    List<Section> findAllByChapterIdIn(List<Integer> chapterIdList);
}
