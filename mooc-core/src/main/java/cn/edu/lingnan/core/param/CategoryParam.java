package cn.edu.lingnan.core.param;

import cn.edu.lingnan.core.entity.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/12/06
 */
@Data
@ToString
public class CategoryParam {
    // ID
    private Integer id;
    // 分类名称
    private String name;
    // 分类描述
    private String description;
    /**
     * 标签list
     */
    private List<Tag> tagList;

}
