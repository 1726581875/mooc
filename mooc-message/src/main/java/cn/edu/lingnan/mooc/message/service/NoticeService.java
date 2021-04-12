package cn.edu.lingnan.mooc.message.service;

import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.message.authentication.entity.UserToken;
import cn.edu.lingnan.mooc.message.authentication.util.UserUtil;
import cn.edu.lingnan.mooc.message.mapper.NoticeMapper;
import cn.edu.lingnan.mooc.message.menus.NoticeStatusEnum;
import cn.edu.lingnan.mooc.message.model.entity.Notice;
import cn.edu.lingnan.mooc.message.model.vo.NoticeVO;
import cn.edu.lingnan.mooc.message.model.vo.ReplyNoticeVO;
import cn.edu.lingnan.mooc.message.repository.NoticeRepository;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;
    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 获取未读消息数
     * @return
     */
    public Integer getUnReadNoticeNum(Integer status) {
        UserToken userToken = UserUtil.getUserToken();
        if(userToken == null){
            log.error("====== 统计未读消息发生异常 用户还没有登录======");
            return null;
        }
        //获取用户Id
        Integer userId = userToken.getUserId().intValue();
        if(UserUtil.isTeacher()){
            return noticeMapper.countUnReadNoticeByUserId(userId,status);
        }

        return noticeMapper.countUnReadNoticeByManagerId(userId,status);
    }

    /**
     * 获取消息列表
     * @param status
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageVO<NoticeVO> getMessageList(Integer status,Integer pageIndex, Integer pageSize){

        //获取用户Id
        Integer userId = UserUtil.getUserId().intValue();
        PageHelper.startPage(pageIndex,pageSize,"create_time DESC");
        List<NoticeVO> noticeVOList = null;
        if(UserUtil.isTeacher()){
           noticeVOList = noticeMapper.getNoticeList(status,userId,false);
        }else {
           noticeVOList = noticeMapper.getNoticeList(status,userId,true);
        }
        //使用分页插件分页，设置页面大小和第几页
        PageInfo<NoticeVO> pageInfo = new PageInfo<NoticeVO>(noticeVOList);

        PageVO<NoticeVO> pageVO = new PageVO<>();
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(pageInfo.getPages());
        pageVO.setContent(pageInfo.getList());
        pageVO.setPageTotal((int)pageInfo.getTotal());

        return pageVO;
    }

    /**
     * 更新消息状态
     * @param noticeIdList
     * @param status
     */
    public void updateNoticeStatus(List<Integer> noticeIdList,Integer status){
        int successNum = noticeMapper.updateNoticeStatus(noticeIdList, status);
        log.info("=====更新消息的状态 idList={},status={},成功数={} =====",noticeIdList.toString(),status,successNum);
    }

    /**
     * 批量更新消息状态
     * @param status
     */
    public void updateAllNoticeStatus(Integer status){
        Integer userId = UserUtil.getUserId();
        Boolean isManager = !UserUtil.isTeacher();
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
    public ReplyNoticeVO getReplyNoticeDetail(Integer id){

        Notice notice = noticeMapper.findById(id);
        if(notice == null){
            log.error("====== 统计未读消息发生异常 用户还没有登录======");
            return null;
        }

        Integer senderId = notice.getSendId();
        Integer courseId = notice.getCourseId();
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
