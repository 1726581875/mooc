package cn.edu.lingnan.mooc.message.mapper;

import cn.edu.lingnan.mooc.message.model.entity.Notice;
import cn.edu.lingnan.mooc.message.model.vo.NoticeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xmz
 * @date: 2021/04/10
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    /**
     * 统计用户消息数
     * @param userId
     * @param status
     * @return
     */
     int countUnReadNoticeByUserId(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 统计管理员未读消息数
     * @param managerId
     * @param status
     * @return
     */
    int countUnReadNoticeByManagerId(@Param("managerId") Long managerId, @Param("status") Integer status);

    /**
     * 获取通知List
     * @param status
     * @param userId
     * @param isManager
     * @return
     */
    List<NoticeVO> getNoticeList(@Param("status") Integer status,@Param("userId")Long userId,@Param("isManager")Boolean isManager);

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
    int updateAllNoticeStatus(@Param("userId")Long userId,@Param("fromStatus")Integer fromStatus,
                              @Param("toStatus")Integer toStatus,@Param("isManager")Boolean isManager);


    /**
     * 根据id查询
     * @param id
     * @return
     */
    Notice findById(@Param("id")Long id);

    /**
     * 根据id查询课程名
     * @param id
     * @return
     */
    String getCourseName(@Param("id")Long id);

    /**
     * 根据id查询用户名
     * @param id
     * @return
     */
    String getUserName(@Param("id")Long id);
}
