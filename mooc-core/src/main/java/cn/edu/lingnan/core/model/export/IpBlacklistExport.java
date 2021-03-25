package cn.edu.lingnan.core.model.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author xmz
 * @date: 2021/03/25
 * IP黑名单导出
 */
@Data
public class IpBlacklistExport {

    @ExcelProperty(value = "名字",index = 0)
    @ColumnWidth(value = 20)
    private String name;

    @ExcelProperty(value = "IP",index = 1)
    @ColumnWidth(value = 20)
    private String ip;

    @ExcelProperty(value = "创建时间",index = 2)
    @ColumnWidth(value = 20)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
