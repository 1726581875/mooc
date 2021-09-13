package cn.edu.lingnan.mooc.portal.model.param;

import lombok.Data;

import java.util.List;

@Data
public class QueryCourseParam {

    public List<Integer> tagIdList;

    private Integer pageIndex;

    private Integer pageSize;


}
