package cn.edu.lingnan.mooc.portal.dao;


import cn.edu.lingnan.mooc.portal.model.entity.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
* @author xmz
* 2021-03-31
* 
*/
public interface ReplyRepository extends JpaRepository<CommentReply, Integer>, JpaSpecificationExecutor<CommentReply> {

    List<CommentReply> findAllByCommentIdIn(List<Integer> commentIdList);

}
