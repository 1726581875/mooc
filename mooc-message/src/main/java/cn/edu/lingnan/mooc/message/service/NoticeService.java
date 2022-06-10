package cn.edu.lingnan.mooc.message.service;

import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.common.model.LoginUser;
import cn.edu.lingnan.mooc.common.util.CopyUtil;
import cn.edu.lingnan.mooc.common.util.UserUtil;
import cn.edu.lingnan.mooc.message.mapper.NoticeMapper;
import cn.edu.lingnan.mooc.message.menus.NoticeStatusEnum;
import cn.edu.lingnan.mooc.message.model.entity.Notice;
import cn.edu.lingnan.mooc.message.model.vo.NoticeVO;
import cn.edu.lingnan.mooc.message.model.vo.ReplyNoticeVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/4/28
 */
@Slf4j
@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 获取未读消息数
     * @return
     */
    public Integer getUnReadNoticeNum(Integer status) {
        LambdaQueryWrapper<Notice> noticeQueryWrapper = getNoticeQueryWrapper(status);
        return noticeMapper.selectCount(noticeQueryWrapper);
    }

    /**
     * 获取消息列表
     * @param status
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageVO<NoticeVO> getMessageList(Integer status,Integer pageIndex, Integer pageSize){


        LambdaQueryWrapper<Notice> noticeQueryWrapper = getNoticeQueryWrapper(status);

        Page<Notice> noticePage = noticeMapper.selectPage(new Page<>(pageIndex , pageSize), noticeQueryWrapper);

        if(CollectionUtils.isEmpty(noticePage.getRecords())){
            return new PageVO<>(pageIndex, pageSize,0, 0L,  new ArrayList<>());
        }

        List<NoticeVO> noticeVOList = CopyUtil.copyList(noticePage.getRecords(), NoticeVO.class);
        int pageCount = Long.valueOf(noticePage.getPages()).intValue();
        return new PageVO<>(pageIndex, pageSize, pageCount, noticePage.getTotal(), noticeVOList);

    }

    private LambdaQueryWrapper<Notice> getNoticeQueryWrapper(Integer status) {
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        LoginUser loginUser = UserUtil.getLoginUser();

        queryWrapper = queryWrapper.eq(Notice::getStatus, status)
                .eq(Notice::getUserType, loginUser.getType());
        // todo 这段逻辑考虑是否可以去除
/*        if (UserUtil.isTeacher()) {
            return queryWrapper.and(
                    e -> e.eq(Notice::getAcceptId, loginUser.getUserId()).or(o -> o.isNotNull(Notice::getCourseId))
            );
        }*/
        return queryWrapper.eq(Notice::getAcceptId, loginUser.getUserId());
    }



    /**
     * 更新消息状态
     * @param noticeIdList
     * @param status
     */
    public void updateNoticeStatus(List<Long> noticeIdList,Integer status){
        int successNum = noticeMapper.updateNoticeStatus(noticeIdList, status);
        log.info("=====更新消息的状态 idList={},status={},成功数={} =====",noticeIdList.toString(),status,successNum);
    }

    /**
     * 批量更新消息状态
     * @param status
     */
    public void updateAllNoticeStatus(Integer status){
        Long userId = UserUtil.getUserId();
        Boolean isManager = UserTypeEnum.MANAGER.equals(UserUtil.getLoginUser().getType());
        int successNum = 0;
        if(NoticeStatusEnum.READ.getStatus().equals(status)){
            //批量更改：未读=>已读
            successNum = noticeMapper.updateAllNoticeStatus(userId, NoticeStatusEnum.UNREAD.getStatus(), status,isManager);
        }

        if(NoticeStatusEnum.DELETED.getStatus().equals(status)){
            //批量更改：已读=>已删除（在回收站可见）
             successNum = noticeMapper.updateAllNoticeStatus(userId, NoticeStatusEnum.READ.getStatus(), status,isManager);
        }

        if(NoticeStatusEnum.TRUE_DELETED.getStatus().equals(status)){
            //批量更改：已删除=>真正删除（在回收站看不到）
            successNum = noticeMapper.updateAllNoticeStatus(userId, NoticeStatusEnum.DELETED.getStatus(), status,isManager);
        }

        log.info("=====更新消息的状态status={},成功数={} =====",status,successNum);
    }

    /**
     * 获取回复消息详情
     * @param id 消息id
     * @return
     */
    public ReplyNoticeVO getReplyNoticeDetail(Long id){

        Notice notice = noticeMapper.findById(id);
        if(notice == null){
            log.error("====== 统计未读消息发生异常 用户还没有登录======");
            return null;
        }

        Long senderId = notice.getSendId();
        Long courseId = notice.getCourseId();
        String courseName = noticeMapper.getCourseName(courseId);
        String userName = noticeMapper.getUserName(senderId);

        //构造返回结果
        ReplyNoticeVO replyNoticeVO = new ReplyNoticeVO();
        replyNoticeVO.setCommentId(notice.getCommentId());
        replyNoticeVO.setReplyId(notice.getReplyId());
        replyNoticeVO.setFromUserId(senderId);
        replyNoticeVO.setCourseId(courseId);
        replyNoticeVO.setReplyContent(notice.getContent());
        replyNoticeVO.setCourseName(courseName);
        replyNoticeVO.setFromUserName(userName);

        return replyNoticeVO;
    }

    public int insert(Notice notice){
        return noticeMapper.insert(notice);
    }


}
