package cn.edu.lingnan.mooc.core.controller;

import cn.edu.lingnan.mooc.core.entity.IpBlacklist;
import cn.edu.lingnan.mooc.core.model.excelImport.IpBlacklistImport;
import cn.edu.lingnan.mooc.core.model.excelImport.IpBlacklistListener;
import cn.edu.lingnan.mooc.core.model.export.IpBlacklistExport;
import cn.edu.lingnan.mooc.core.repository.IpBlacklistRepository;
import cn.edu.lingnan.mooc.core.service.IpBlacklistService;
import cn.edu.lingnan.mooc.core.util.CopyUtil;
import cn.edu.lingnan.mooc.common.model.RespResult;
import com.alibaba.excel.EasyExcel;
import com.sun.deploy.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author xmz
 * @date: 2021/03/23
 */
@Slf4j
@RestController
@RequestMapping("/admin/ipBlacklists")
public class IpBlacklistController {

    @Autowired
    private IpBlacklistService ipBlacklistService;

    @Autowired
    private IpBlacklistRepository ipBlacklistRepository;

    /**
     * Ip黑名单导入
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/import")
    public RespResult importExcel(MultipartFile file){
        try{
            EasyExcel.read(file.getInputStream(), IpBlacklistImport.class, new IpBlacklistListener(ipBlacklistRepository)).sheet().doRead();
        }catch (Exception e){
            log.error("====== 导入Excel 发生异常:",e);
            return RespResult.fail("导入黑名单IP失败");
        }
        return RespResult.success("导入成功");
    }



    @GetMapping("/export")
    public void exportExcel(IpBlacklist matchObject, HttpServletResponse response) throws IOException {

        //查询数据
        List<IpBlacklist> ipBlacklistList = ipBlacklistService.findAllByCondition(matchObject);
        //设置请求头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("IP黑名单_" + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        //使用EasyExcel导出excel
        EasyExcel.write(response.getOutputStream(), IpBlacklistExport.class).sheet("sheet1").doWrite( CopyUtil.copyList(ipBlacklistList, IpBlacklistExport.class));
    }


    /**
     * 分页查询ipBlacklist接口
     * get请求
     * url: /admin/ipBlacklists/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(IpBlacklist matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(ipBlacklistService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 更新ipBlacklist接口
     * 请求方法: put
     * url: /admin/ipBlacklists/{id}
     * @param ipBlacklist
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody IpBlacklist ipBlacklist) {
        Integer flag = ipBlacklistService.update(ipBlacklist);
        if (flag == 0) {
            return RespResult.fail("更新IpBlacklist失败");
        }
        return RespResult.success("更新IpBlacklist成功");
    }

    /**
     * 插入或更新ipBlacklist
     * 请求方法: post
     * url: /admin/ipBlacklists/ipBlacklist
     * @param ipBlacklist
     * @return
     */
    @PostMapping("/ipBlacklist")
    public RespResult insertOrUpdate(@RequestBody IpBlacklist ipBlacklist) {
        Integer flag = ipBlacklistService.insertOrUpdate(ipBlacklist);
        if (flag == 0) {
            return RespResult.fail("新增IpBlacklist失败");
        }
        return RespResult.success("新增IpBlacklist成功");
    }

    /**
     * 删除ipBlacklist接口
     * 请求方法: delete
     * url: /admin/ipBlacklists/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = ipBlacklistService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除IpBlacklist失败");
        }
        return RespResult.success("删除IpBlacklist成功");
    }

    /**
     * 批量删除ipBlacklist接口
     * 请求方法: post
     * url: /admin/ipBlacklists/bacth/delete
     * @param ipBlacklistIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> ipBlacklistIdList) {
        ipBlacklistService.deleteAllByIds(ipBlacklistIdList);
        return RespResult.success("批量删除IpBlacklist成功");
    }

}