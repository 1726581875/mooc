package cn.edu.lingnan.mooc.common.model;

import lombok.Data;
import java.util.List;

/**
 * @Author xmz
 * @Date: 2020/10/06
 * 封装分页结果
 */
@Data
public class PageVO <T> {
    // 当前第几页
    private Integer pageIndex;
    // 每页大小
    private Integer pageSize;
    // 总页数
    private Integer pageCount;
    // 数据总条数
    private Integer pageTotal;
    // 页数据
    private List<T> content;

}

