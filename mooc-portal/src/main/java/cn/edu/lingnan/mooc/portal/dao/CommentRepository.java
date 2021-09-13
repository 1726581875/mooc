package cn.edu.lingnan.mooc.portal.dao;


import cn.edu.lingnan.mooc.portal.model.entity.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author xmz
* 2021-03-31
* 
*/
public interface CommentRepository extends JpaRepository<CourseComment, Integer>, JpaSpecificationExecutor<CourseComment> {
	
}
