package cn.edu.lingnan.mooc.message.mapper;

import cn.edu.lingnan.mooc.message.model.entity.Notice;
import cn.edu.lingnan.mooc.message.model.vo.NoticeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xmz
 * @date: 2021/04/10
 */
public interface SendNoticeMapper {

  /**
   * 查询具有课程权限和消息通知权限的管理员ID
   * @return
   */
  List<Long> getCourseManagerIdList();

}
