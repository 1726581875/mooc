package cn.edu.lingnan.mooc.core.entity.reception;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author xiaomingzhang
 * @data 2021/02/02
 * 收藏表实体类
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Accessors(chain=true)
public class Collection {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer courseId;

    private Integer userId;

    private Date createTime;

    private Date updateTime;


}
