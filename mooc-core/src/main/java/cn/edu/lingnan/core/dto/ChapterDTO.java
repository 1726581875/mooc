package cn.edu.lingnan.core.dto;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class ChapterDTO implements Serializable{

    @ExcelProperty(value = "ID",index = 0)
    @ColumnWidth(value = 20)
    private Integer id;

    @ExcelProperty(value = "课程ID",index = 1)
    @ColumnWidth(value = 20)
    private Integer courseId;

    @ExcelProperty(value = "名称",index = 2)
    @ColumnWidth(value = 20)
    private String name;

    @ExcelProperty(value = "创建时间",index = 3)
    @ColumnWidth(value = 20)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ExcelProperty(value = "更新时间",index = 4)
    @ColumnWidth(value = 20)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    
}