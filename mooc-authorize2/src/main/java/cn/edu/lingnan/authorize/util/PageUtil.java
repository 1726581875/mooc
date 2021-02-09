package cn.edu.lingnan.authorize.util;

import cn.edu.lingnan.mooc.common.model.PageVO;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/26
 */
public class PageUtil{

    public static PageVO getPageVO(List items, Integer pageIndex, Integer pageSize){
        // 数据条数
        Integer totalAmount = items.size();
        // 总页数
        Integer pageCount = totalAmount % pageSize == 0 ? totalAmount / pageSize : totalAmount / pageSize + 1;
        // 开始下标
        Integer startIndex = (pageIndex - 1) * pageSize;

        Integer limit = pageIndex == pageCount ? totalAmount % pageSize : pageSize;
        PageVO pageVO = new PageVO<>();
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageTotal(items.size());
        pageVO.setPageCount(pageCount);
        pageVO.setContent(items.subList(startIndex,startIndex + limit));
        return pageVO;
    }




}
