package cn.edu.lingnan.core.repository;

import cn.edu.lingnan.core.entity.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author xmz
* 2021-03-31
* 
*/
public interface ReplyRepository extends JpaRepository<CommentReply, Integer>, JpaSpecificationExecutor<CommentReply> {

    List<CommentReply> findAllByCommentIdIn(List<Integer> commentIdList);

}
