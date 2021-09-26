package cn.edu.lingnan.mooc.portal.service;

import cn.edu.lingnan.mooc.common.exception.MoocException;
import cn.edu.lingnan.mooc.common.model.NoticeDTO;
import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.common.util.JsonUtil;
import cn.edu.lingnan.mooc.common.util.RedisUtil;
import cn.edu.lingnan.mooc.portal.authorize.model.entity.MoocUser;
import cn.edu.lingnan.mooc.portal.authorize.util.CopyUtil;;
import cn.edu.lingnan.mooc.portal.constant.RabbitMqConstant;
import cn.edu.lingnan.mooc.portal.constant.RedisPrefixConstant;
import cn.edu.lingnan.mooc.portal.dao.CommentRepository;
import cn.edu.lingnan.mooc.portal.dao.CourseRepository;
import cn.edu.lingnan.mooc.portal.dao.ReplyRepository;
import cn.edu.lingnan.mooc.portal.model.dto.ReplyerDTO;
import cn.edu.lingnan.mooc.portal.model.entity.CommentReply;
import cn.edu.lingnan.mooc.portal.model.entity.Course;
import cn.edu.lingnan.mooc.portal.model.entity.CourseComment;
import cn.edu.lingnan.mooc.portal.model.enums.MoocExceptionEnum;
import cn.edu.lingnan.mooc.portal.model.param.CommentParam;
import cn.edu.lingnan.mooc.portal.model.param.ReplyParam;
import cn.edu.lingnan.mooc.portal.model.vo.CommentAndReplyVO;
import cn.edu.lingnan.mooc.portal.model.vo.CommentDetailVO;
import cn.edu.lingnan.mooc.portal.model.vo.CommentListVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
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
    private UserService userService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 根据课程courseId查询评论
     * @param courseId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageVO<CommentAndReplyVO> findAllCommentByCourseId(Long courseId, Integer type, Integer pageIndex, Integer pageSize){

        CourseComment courseComment = new CourseComment();
        courseComment.setCourseId(courseId);
        courseComment.setType(type);
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"createTime");
        Page<CourseComment> courseCommentPage = commentRepository.findAll(Example.of(courseComment), pageable);
        List<CourseComment> courseCommentList = courseCommentPage.getContent();

        List<CommentAndReplyVO> courseCommentVOList = new ArrayList<>();
        List<Long> commentIdList = courseCommentList.stream().map(CourseComment::getId).collect(Collectors.toList());
        List<Long> userIdList = courseCommentList.stream().map(CourseComment::getUserId).collect(Collectors.toList());
        //获取用户信息map
        Map<Long, MoocUser> userMap = userService.getUserMap(userIdList);
        //获取评论回复Map
        Map<Long, List<ReplyerDTO>> replyMap = getReplyListMapByCommentIdList(commentIdList);

        //courseCommentList -> CommentAndReplyVO
        courseCommentList.forEach(comment -> courseCommentVOList.add(createCommentAndReplyVO(comment, userMap, replyMap)));

        /* 4. 封装到自定义分页结果 */
        PageVO<CommentAndReplyVO> pageVO = new PageVO<>();
        pageVO.setContent(courseCommentVOList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(courseCommentPage.getTotalPages());
        //总数
        pageVO.setTotalRow(courseCommentPage.getTotalElements());
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
                                                      Map<Long, MoocUser> userMap, Map<Long, List<ReplyerDTO>> replyMap){
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
    public Map<Long, List<ReplyerDTO>> getReplyListMapByCommentIdList(List<Long> commentIdList){
        List<CommentReply> commentReplyList = replyRepository.findAllByCommentIdIn(commentIdList);
        List<Long> userIdList = commentReplyList.stream().map(CommentReply::getUserId).collect(Collectors.toList());
        List<Long> toUserIdList = commentReplyList.stream().map(CommentReply::getToUserId).collect(Collectors.toList());
        Set<Long> allUserIdSet = new HashSet<>();
        allUserIdSet.addAll(userIdList);
        allUserIdSet.addAll(toUserIdList);
        //获取用户信息map
        Map<Long, MoocUser> userMap = userService.getUserMap(new ArrayList<>(allUserIdSet));

        List<ReplyerDTO> replyerDTOList = commentReplyList.stream()
                .map(reply->createReplyerDTO(reply,userMap)).collect(Collectors.toList());

        //根据commentId分组
        Map<Long, List<ReplyerDTO>> replyListMap = replyerDTOList.stream().collect(Collectors.groupingBy(ReplyerDTO::getCommentId));

        //按照时间排序
        replyListMap.forEach((k,v) -> {
            v.sort(Comparator.comparing(ReplyerDTO::getCreateTime));
        });
        return replyListMap;
    }


    private ReplyerDTO createReplyerDTO(CommentReply reply, Map<Long,MoocUser> userMap){
        ReplyerDTO replyerDTO = CopyUtil.copy(reply, ReplyerDTO.class);
        replyerDTO.setReplyerName(userMap.get(reply.getUserId()).getName());
        replyerDTO.setReplyerImage(userMap.get(reply.getUserId()).getUserImage());
        replyerDTO.setToUserName(userMap.get(reply.getToUserId()).getName());
        replyerDTO.setToUserImage(userMap.get(reply.getToUserId()).getUserImage());
        replyerDTO.setReplyId(reply.getId());
        return replyerDTO;
    }


    public void insertReply(ReplyParam replyParam) {

        CommentReply reply = new CommentReply().build(replyParam);
        //插入回复信息
        CommentReply commentReply = replyRepository.save(reply);

        //评论的回复数+1,加锁避免多线程下回复数混乱
        synchronized (CommentService.class) {
            Optional<CourseComment> commentOptional = commentRepository.findById(replyParam.getCommentId());
            if (!commentOptional.isPresent()) {
                log.error("====== 获取课程信息评论信息失败，评论不存在courseId={}, commentId={} ====", replyParam.getCourseId(), replyParam.getCommentId());
            }

            CourseComment comment = commentOptional.get();
            //评论的回复数数加一，并保存到数据库
            comment.setReplyNum(comment.getReplyNum() + 1);
            commentRepository.save(comment);

            NoticeDTO noticeDTO = buildReplyNoticeDTO(replyParam,commentReply);
            amqpTemplate.convertAndSend(RabbitMqConstant.messageQueueName, JsonUtil.toJson(noticeDTO));
        }

    }


    private NoticeDTO buildReplyNoticeDTO(ReplyParam replyParam,  CommentReply commentReply){
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setSendId(replyParam.getUserId());
        noticeDTO.setAcceptId(replyParam.getToUserId());
        noticeDTO.setCourseId(replyParam.getCourseId());
        noticeDTO.setCommentId(commentReply.getCommentId());
        noticeDTO.setReplyId(commentReply.getId());
        noticeDTO.setContent(replyParam.getContent());
        noticeDTO.setType(3);
        return noticeDTO;
    }


    public void insertComment(CommentParam commentParam){

        Optional<Course> courseOptional = courseRepository.findById(commentParam.getCourseId());

        if(!courseOptional.isPresent()) {
            log.error("====== 获取课程信息失败，课程不存在 courseId={} ====", commentParam.getCourseId());
            throw new MoocException(MoocExceptionEnum.COURSE_NOT_EXIST);
        }

        // 插入课程评论
        CourseComment comment = new CourseComment().build(commentParam);
        CourseComment courseComment = commentRepository.save(comment);

        // 判断是课程评论还是课程问答,对应redis前缀不一样
        String redisKey = commentParam.getType().equals(0) ? RedisPrefixConstant.COMMENT_NUM_PRE + commentParam.getCourseId()
                : RedisPrefixConstant.QUESTION_NUM_PRE + commentParam.getCourseId();

        if(RedisUtil.isNotExist(redisKey)) {
            Course course = courseOptional.get();
            RedisUtil.setIfAbsent(redisKey, course.getCommentNum().toString());
        }
        RedisUtil.getRedisTemplate().opsForValue().increment(redisKey,1L);

        //发送消息
        NoticeDTO noticeDTO = buildCommentNotion(commentParam, courseOptional.get(), courseComment.getId());
        amqpTemplate.convertAndSend(RabbitMqConstant.messageQueueName, JsonUtil.toJson(noticeDTO));

    }

    private NoticeDTO buildCommentNotion(CommentParam commentParam, Course course, Long commentId){
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setSendId(commentParam.getUserId());
        noticeDTO.setAcceptId(course.getTeacherId());
        noticeDTO.setCourseId(course.getId());
        noticeDTO.setCommentId(commentId);
        noticeDTO.setContent(commentParam.getContent());
        noticeDTO.setType(2);
        return noticeDTO;
    }

    /**
     * 根据课程courseId查询评论
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageVO<CommentListVO> findAllCommentList(Integer type, Integer pageIndex, Integer pageSize){

        CourseComment courseComment = new CourseComment();
        courseComment.setType(type);
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"createTime");
        Page<CourseComment> courseCommentPage = commentRepository.findAll(Example.of(courseComment), pageable);
        List<CourseComment> courseCommentList = courseCommentPage.getContent();

        List<CommentListVO> courseCommentVOList = new ArrayList<>();

        //获取用户信息map
        List<Long> userIdList = courseCommentList.stream().map(CourseComment::getUserId).collect(Collectors.toList());
        Map<Long, MoocUser> userMap = userService.getUserMap(userIdList);
        //获取课程名
        List<Long> courseIdList = courseCommentList.stream().map(CourseComment::getCourseId).collect(Collectors.toList());
        Map<Long, String> courseNameMap = courseService.getCourseNameMap(courseIdList);


        //courseComment -> CommentListVO
        courseCommentList.forEach(comment -> courseCommentVOList.add(createCommentListVO(comment,userMap, courseNameMap)));

        /* 4. 封装到自定义分页结果 */
        PageVO<CommentListVO> pageVO = new PageVO<>();
        pageVO.setContent(courseCommentVOList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(courseCommentPage.getTotalPages());
        return pageVO;
    }

    private CommentListVO createCommentListVO(CourseComment comment,Map<Long, MoocUser> userMap,Map<Long, String> courseNameMap){
        CommentListVO commentListVO = CopyUtil.copy(comment,CommentListVO.class);
        commentListVO.setCommentId(comment.getId());
        commentListVO.setStarNum(comment.getCommentStar());
        //设置用户信息
        commentListVO.setUserName(userMap.get(comment.getUserId()).getName());
        commentListVO.setUserImage(userMap.get(comment.getUserId()).getUserImage());
        //设置课程信息
        commentListVO.setCourseName(courseNameMap.getOrDefault(comment.getCourseId(),"未知课程"));
        commentListVO.setCourseId(comment.getCourseId());
        return commentListVO;
    }

    public CommentDetailVO getCommentDetail(Long commentId){

        Optional<CourseComment> commentOptional = commentRepository.findById(commentId);
        if(!commentOptional.isPresent()){
            log.error("======该评论不存在commentId={} =====",commentId);
            throw new RuntimeException("评论不存在");
        }
        CourseComment courseComment = commentOptional.get();
        //获取用户信息map
        Map<Long, MoocUser> userMap = userService.getUserMap(Lists.newArrayList(courseComment.getUserId()));
        //获取课程名
        Map<Long, String> courseNameMap = courseService.getCourseNameMap(Lists.newArrayList(courseComment.getCourseId()));
        //获取评论的回复List
        Map<Long, List<ReplyerDTO>> replyListMap = this.getReplyListMapByCommentIdList(Lists.newArrayList(commentId));

        //构造返回对象
        CommentDetailVO commentDetailVO = CopyUtil.copy(courseComment,CommentDetailVO.class);
        commentDetailVO.setCommentId(courseComment.getId());
        commentDetailVO.setStarNum(courseComment.getCommentStar());
        //设置用户信息
        commentDetailVO.setUserName(userMap.get(courseComment.getUserId()).getName());
        commentDetailVO.setUserImage(userMap.get(courseComment.getUserId()).getUserImage());
        //设置课程信息
        commentDetailVO.setCourseName(courseNameMap.getOrDefault(courseComment.getCourseId(),"未知课程"));
        commentDetailVO.setCourseId(courseComment.getCourseId());
        //设置回复List
        commentDetailVO.setReplyList(replyListMap.getOrDefault(courseComment.getId(),Lists.newArrayList()));

        return commentDetailVO;
    }

}
