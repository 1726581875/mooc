package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.entity.CommentReply;
import cn.edu.lingnan.core.entity.Course;
import cn.edu.lingnan.core.entity.CourseComment;
import cn.edu.lingnan.core.entity.MoocUser;
import cn.edu.lingnan.core.repository.CommentRepository;
import cn.edu.lingnan.core.repository.ReplyRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.core.vo.CommentAndReplyVO;
import cn.edu.lingnan.core.vo.CourseVO;
import cn.edu.lingnan.core.vo.ReplyerDTO;
import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.common.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2021/03/31
 */
@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private MoocUserService moocUserService;

    public PageVO<CommentAndReplyVO> findAllCommentByCourseId(Integer courseId,Integer pageIndex, Integer pageSize){

        CourseComment courseComment = new CourseComment();
        courseComment.setCourseId(courseId);
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"createTime");
        Page<CourseComment> courseCommentPage = commentRepository.findAll(Example.of(courseComment), pageable);
        List<CourseComment> courseCommentList = courseCommentPage.getContent();

        List<CommentAndReplyVO> courseVOList = new ArrayList<>();
        List<Integer> commentIdList = courseCommentList.stream().map(CourseComment::getId).collect(Collectors.toList());
        List<Integer> userIdList = courseCommentList.stream().map(CourseComment::getUserId).collect(Collectors.toList());
        //获取用户信息map
        Map<Integer, MoocUser> userMap = moocUserService.getUserMap(userIdList);
        //获取评论回复Map
        Map<Integer, List<ReplyerDTO>> replyMap = getReplyListMapByCommentIdList(commentIdList);

        //courseCommentList -> CommentAndReplyVO
        courseCommentList.forEach(comment -> courseVOList.add(createCommentAndReplyVO(comment, userMap, replyMap)));

        /* 4. 封装到自定义分页结果 */
        PageVO<CommentAndReplyVO> pageVO = new PageVO<>();
        pageVO.setContent(courseVOList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(courseCommentPage.getTotalPages());
        return pageVO;
    }

    /**
     * 构造一个CommentAndReplyVO
     * @param comment
     * @param userMap
     * @param replyMap
     * @return
     */
    private CommentAndReplyVO createCommentAndReplyVO(CourseComment comment,
                                                      Map<Integer, MoocUser> userMap, Map<Integer, List<ReplyerDTO>> replyMap){
        CommentAndReplyVO commentAndReplyVO = CopyUtil.copy(comment,CommentAndReplyVO.class);
        commentAndReplyVO.setCommentId(comment.getId());
        commentAndReplyVO.setUserName(userMap.get(comment.getUserId()).getName());
        commentAndReplyVO.setUserImage(userMap.get(comment.getUserId()).getUserImage());
        commentAndReplyVO.setReplyList(replyMap.getOrDefault(comment.getId(),new ArrayList<>()));
        //commentAndReplyVO.setStar(true);
        return commentAndReplyVO;
    }

    /**
     * 根据评论Id获取该评论的回复信息
     * @param commentIdList
     * @return
     */
    public Map<Integer, List<ReplyerDTO>> getReplyListMapByCommentIdList(List<Integer> commentIdList){
        List<CommentReply> commentReplyList = replyRepository.findAllByCommentIdIn(commentIdList);
        List<Integer> userIdList = commentReplyList.stream().map(CommentReply::getUserId).collect(Collectors.toList());
        List<Integer> toUserIdList = commentReplyList.stream().map(CommentReply::getToUserId).collect(Collectors.toList());
        Set<Integer> allUserIdSet = new HashSet<>();
        allUserIdSet.addAll(userIdList);
        allUserIdSet.addAll(toUserIdList);
        //获取用户信息map
        Map<Integer, MoocUser> userMap = moocUserService.getUserMap(new ArrayList<>(allUserIdSet));

        List<ReplyerDTO> replyerDTOList = commentReplyList.stream()
                .map(reply->createReplyerDTO(reply,userMap)).collect(Collectors.toList());

        //根据commentId分组
        Map<Integer, List<ReplyerDTO>> replyListMap = replyerDTOList.stream().collect(Collectors.groupingBy(ReplyerDTO::getCommentId));

        //按照时间排序
        replyListMap.forEach((k,v) -> {
            v.sort(Comparator.comparing(ReplyerDTO::getCreateTime));
        });
        return replyListMap;
    }


    private ReplyerDTO createReplyerDTO(CommentReply reply, Map<Integer,MoocUser> userMap){
        ReplyerDTO replyerDTO = CopyUtil.copy(reply, ReplyerDTO.class);
        replyerDTO.setReplyerName(userMap.get(reply.getUserId()).getName());
        replyerDTO.setReplyerImage(userMap.get(reply.getUserId()).getUserImage());
        replyerDTO.setToUserName(userMap.get(reply.getToUserId()).getName());
        replyerDTO.setToUserImage(userMap.get(reply.getToUserId()).getUserImage());
        replyerDTO.setReplyId(reply.getId());
        return replyerDTO;
    }


    public boolean insertCommentOrReply(Integer courseId, Integer commentId, Integer userId, Integer toUserId, String content) {

        //判断是不是要插入回复，toUserId不为null就说明是回复
        if(!StringUtils.isEmpty(commentId) && !StringUtils.isEmpty(toUserId)){
            CommentReply reply = new CommentReply();
            reply.setCommentId(commentId);
            reply.setUserId(userId);
            reply.setToUserId(toUserId);
            reply.setReplyContent(content);
            CommentReply commentReply = replyRepository.save(reply);
            if(commentReply == null){
                return false;
            }
            return true;
        }

        //插入课程评论
        CourseComment comment = new CourseComment();
        comment.setCourseId(courseId);
        comment.setUserId(userId);
        comment.setCommentContent(content);
        CourseComment articleComment = commentRepository.save(comment);
        if(articleComment == null){
            return false;
        }
        return true;
    }
}
