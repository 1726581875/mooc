package cn.edu.lingnan.mooc.common.model;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * @Author xmz
 * @Date: 2020/10/06
 * 封装分页结果
 */
@Data
public class PageVO <T> implements Serializable {

    private static final long serialVersionUID = -5534780421701875924L;
    /**
     * 当前第几页
     */
    private Integer pageIndex;
    /**
     * 每页大小
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer pageCount;
    /**
     * 数据总条数
     */
    private Long totalRow;
    /**
     * 页数据
     */
    private List<T> content;


    public PageVO() {}

    public PageVO(Integer pageIndex, Integer pageSize, Integer pageCount,  List<T> content) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.content = content;
    }

    public PageVO(Integer pageIndex, Integer pageSize, Integer pageCount, Long totalRow, List<T> content) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.totalRow = totalRow;
        this.content = content;
    }

    public PageVO(Page<T> page) {
        this.pageIndex = page.getNumber();
        this.pageSize = page.getSize();
        this.pageCount = page.getTotalPages();
        this.totalRow = page.getTotalElements();
        this.content = page.getContent();
    }

}

