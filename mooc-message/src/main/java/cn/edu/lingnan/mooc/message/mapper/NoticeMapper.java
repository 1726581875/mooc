package cn.edu.lingnan.mooc.message.mapper;

/**
 * @author xmz
 * @date: 2021/04/10
 */
public interface NoticeMapper {

    /**
     *
     * @return
     */
     int countUnReadNoticeNum();

    /**
     * @param userId
     * @return
     */
     int countUnReadNoticeByUserId(Integer userId);

    /**
     * @param managerId
     * @return
     */
    int countUnReadNoticeByManagerId(Integer managerId);

}
