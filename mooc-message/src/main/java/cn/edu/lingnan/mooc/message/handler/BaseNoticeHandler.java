package cn.edu.lingnan.mooc.message.handler;

import cn.edu.lingnan.mooc.common.model.NoticeDTO;

/**
 * @author xiaomingzhang
 * @date 2022/1/10
 */
public interface BaseNoticeHandler {

    void handle(NoticeDTO noticeDTO);

}
