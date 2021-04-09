package cn.edu.lingnan.mooc.message.service;

import cn.edu.lingnan.mooc.message.authentication.entity.UserToken;
import cn.edu.lingnan.mooc.message.authentication.util.UserUtil;
import cn.edu.lingnan.mooc.message.mapper.NoticeMapper;
import cn.edu.lingnan.mooc.message.repository.NoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    public Integer getUnReadNoticeNum() {
        UserToken userToken = UserUtil.getUserToken();
        if(userToken == null){
            log.error("====== 统计未读消息发生异常 用户还没有登录======");
            return null;
        }
        //获取用户Id
        Integer userId = userToken.getUserId().intValue();
        if(UserUtil.isTeacher()){
            return noticeMapper.countUnReadNoticeByUserId(userId);
        }

        return noticeMapper.countUnReadNoticeByManagerId(userId);
    }




}
