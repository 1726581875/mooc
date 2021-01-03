package cn.edu.lingnan.mooc.statistics.entity.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.util.Date;

/**
 * @author xmz
 * @date: 2020/12/30
 */
@Data
@Document(indexName = "mooc_course",type = "_doc",
        useServerConfiguration = true,createIndex = false)
public class EsCourse {

    /**
     * 课程id
     */
    @Id
    private Integer id;
    /**
     * 课程名
     */
    @Field(type = FieldType.Text)
    private String name;
    /**
     * 课程简介
     */
    @Field(type = FieldType.Text)
    private String summary;
    /**
     * 讲师id
     */
    @Field(type = FieldType.Integer)
    private Integer teachId;
    /**
     * 修改时间
     */
    @Field(type = FieldType.Date,format = DateFormat.custom,
            pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || epoch_millis")
    private Date updateTime;
    /**
     * 创建时间
     */
    @Field(type = FieldType.Date,format = DateFormat.custom,
            pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || epoch_millis")
    private Date createTime;


}
