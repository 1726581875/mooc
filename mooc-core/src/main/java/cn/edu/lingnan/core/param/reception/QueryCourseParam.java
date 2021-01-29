package cn.edu.lingnan.core.param.reception;

import lombok.Data;

import java.util.List;

@Data
public class QueryCourseParam {

    public List<Integer> tagIdList;

    private Integer pageIndex;

    private Integer pageSize;


}
