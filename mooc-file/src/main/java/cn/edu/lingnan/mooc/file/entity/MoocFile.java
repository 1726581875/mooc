package cn.edu.lingnan.mooc.file.entity;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity(name = "mooc_file")
public class MoocFile{
    // id   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 文件名   
    private String name;
    // 视频相对路径   
    private String filePath;
    // 大小|字节B   
    private Integer fileSize;
    // 文件后缀   
    private String fileSuffix;
    // 文件唯一标识   
    private String fileKey;
    /**
     * 文件类型|1视频、2图片、3未知类型、4txt、5markdown 5、ppt 6 word 7、excel 8、pdf
     */
    private Integer fileType;
    // 分片下标   
    private Integer shardIndex;
    // 总共分片数   
    private Integer shardCount;
    // 分片大小   
    private Integer shardSize;
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 所属课程Id
     */
    private Integer courseId;
    /**
     * 文件状态|1、正常 2、已删除
     */
    private Integer status;
    // 创建时间   
    private Date createTime;
    // 修改时间   
    private Date updateTime;
    
}