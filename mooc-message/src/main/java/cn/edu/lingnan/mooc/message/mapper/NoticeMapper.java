package cn.edu.lingnan.mooc.message.mapper;

import cn.edu.lingnan.mooc.message.model.entity.Notice;
import cn.edu.lingnan.mooc.message.model.vo.NoticeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xmz
 * @date: 2021/04/10
 */
public interface NoticeMapper {

    /**
     * @param userId
     * @return
     */
     int countUnReadNoticeByUserId(@Param("userId") Integer userId,@Param("status") Integer status);

    /**
     * @param managerId
     * @return
     */
    int countUnReadNoticeByManagerId(@Param("managerId") Integer managerId,@Param("status") Integer status);

    /**
     * 获取通知List
     * @param status
     * @param userId
     * @param isManager
     * @return
     */
    List<NoticeVO> getNoticeList(@Param("status") Integer status,@Param("userId")Integer userId,@Param("isManager")Boolean isManager);

    /**
     * 更新通知状态
     * @param noticeIdList
     * @param status
     * @return
     */
    int updateNoticeStatus(@Param("noticeIdList")List<Integer> noticeIdList,@Param("status")Integer status);

    /**
     * 批量更新通知状态
     * @return
     */
    int updateAllNoticeStatus(@Param("userId")Integer userId,@Param("fromStatus")Integer fromStatus,
                              @Param("toStatus")Integer toStatus,@Param("isManager")Boolean isManager);

}