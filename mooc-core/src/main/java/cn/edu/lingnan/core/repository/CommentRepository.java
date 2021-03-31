package cn.edu.lingnan.core.repository;

import cn.edu.lingnan.core.entity.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
* @author xmz
* 2021-03-31
* 
*/
public interface CommentRepository extends JpaRepository<CourseComment, Integer>, JpaSpecificationExecutor<CourseComment> {
	
}
