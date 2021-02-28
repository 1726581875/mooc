package cn.edu.lingnan.mooc.statistics.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Carlson
 * @Date: 2020/3/5 15:10
 * 统计概览-每天数量展示模型
 */
@Getter
@Setter
@ToString
public class StatisticsVO {
    /**
     * 日期
     */
    private String date;
    /**
     * 统计值
     */
    private Long count;
}
