package cn.edu.lingnan.mooc.message.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MessageFactory {

/*
     @Autowired
     private CourseUserServiceApi courseUserApi;


     public List<Notice> getHomeWorkMessage(Integer sendId , Integer courseId,String message){
          List<Notice> noticeList = new ArrayList<>();
          List<CourseUserInfo> courseUserList = courseUserApi.findAllCourseUserByCourseId(courseId);
          courseUserList.forEach(e -> {
               noticeList.add(new Notice()
                       .setSendId(sendId)
                       .setAcceptId(e.getUserId())
                       .setNoticeType(1)
                       .setNoticeContent(message)
                       .setNoticeFlag(0));
          });
        return noticeList;
     }

     */
/**
      * 构造一个条简单消息
      * @param sendId
      * @param acceptId
      * @param message
      * @return
      *//*

     public Notice getSimpleMessage(Integer sendId , Integer acceptId,String message){

          return  new Notice()
                  .setSendId(sendId)
                  .setAcceptId(acceptId)
                  .setNoticeType(2)//类型2，个人信息
                  .setNoticeContent(message)
                  .setNoticeFlag(0);//未读
     }
*/




}
