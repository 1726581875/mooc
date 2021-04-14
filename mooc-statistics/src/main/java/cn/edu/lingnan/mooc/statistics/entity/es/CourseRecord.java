package cn.edu.lingnan.mooc.statistics.entity.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2021/02/23
 */
@Data
@Document(indexName = "course_record",type = "_doc", useServerConfiguration = true,createIndex = false)
public class CourseRecord {

    @Id
    private Long id;

    @Field(type = FieldType.Integer)
    private Integer courseId;

    @Field(type = FieldType.Integer)
    private Integer teacherId;

    @Field(type = FieldType.Integer)
    private Integer collectionNum;

    @Field(type = FieldType.Integer)
    private Integer viewNum;

    @Field(type = FieldType.Date,format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || epoch_millis")
    private Date countTime;

    @Field(type = FieldType.Date,format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || epoch_millis")
    private Date createTime;





}
