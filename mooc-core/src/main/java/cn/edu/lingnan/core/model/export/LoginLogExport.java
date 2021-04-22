package cn.edu.lingnan.core.model.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author xmz
 * @date: 2021/03/10
 * 登录日志导出
 */
@Data
public class LoginLogExport {

    @ExcelProperty(value = "管理员账号",index = 0)
    @ColumnWidth(value = 20)
    private String account;

    @ExcelProperty(value = "日志名",index = 1)
    @ColumnWidth(value = 20)
    private String logName;

    @ExcelProperty(value = "是否成功",index = 2)
    @ColumnWidth(value = 20)
    private String succeed;

    @ExcelProperty(value = "具体消息",index = 3)
    @ColumnWidth(value = 20)
    private String message;

    @ExcelProperty(value = "登录ip",index = 4)
    @ColumnWidth(value = 20)
    private String ip;

    @ExcelProperty(value = "创建时间",index = 5)
    @ColumnWidth(value = 20)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;


}
