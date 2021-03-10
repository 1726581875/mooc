package cn.edu.lingnan.mooc.file.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FileExport {

    @ExcelProperty(value = "ID",index = 0)
    @ColumnWidth(value = 20)
    private Integer id;

    @ExcelProperty(value = "文件名",index = 1)
    @ColumnWidth(value = 20)
    private String name;

    @ExcelProperty(value = "相对路径",index = 2)
    @ColumnWidth(value = 20)
    private String filePath;

    @ExcelProperty(value = "大小",index = 3)
    @ColumnWidth(value = 20)
    private Integer fileSize;

    @ExcelProperty(value = "文件后缀",index = 4)
    @ColumnWidth(value = 20)
    private String fileSuffix;

    @ExcelProperty(value = "fileKey",index = 5)
    @ColumnWidth(value = 20)
    private String fileKey;
    /**
     * 文件类型|1视频、2图片、3未知类型、4txt、5markdown 5、ppt 6 word 7、excel 8、pdf
     */
    @ExcelProperty(value = "文件名",index = 6)
    @ColumnWidth(value = 20)
    private Integer fileType;

    @ExcelProperty(value = "分片下标",index = 7)
    @ColumnWidth(value = 20)
    private Integer shardIndex;

    @ExcelProperty(value = "总共分片数",index = 8)
    @ColumnWidth(value = 20)
    private Integer shardCount;

    @ExcelProperty(value = "分片大小",index = 9)
    @ColumnWidth(value = 20)
    private Integer shardSize;

    @ExcelProperty(value = "用户Id",index = 10)
    @ColumnWidth(value = 20)
    private Integer userId;

    @ExcelProperty(value = "用户名",index = 11)
    @ColumnWidth(value = 20)
    private String userName;

    @ExcelProperty(value = "课程Id",index = 12)
    @ColumnWidth(value = 20)
    private Integer courseId;

    @ExcelProperty(value = "课程名",index = 13)
    @ColumnWidth(value = 20)
    private String courseName;

    @ExcelProperty(value = "状态",index = 14)
    @ColumnWidth(value = 20)
    private Integer status;

    @ExcelProperty(value = "创建时间",index = 15)
    @ColumnWidth(value = 20)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;


}
