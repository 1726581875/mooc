package cn.edu.lingnan.mooc.file.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.file.constant.FileConstant;
import cn.edu.lingnan.mooc.file.entity.MoocFile;
import cn.edu.lingnan.mooc.file.model.FileExport;
import cn.edu.lingnan.mooc.file.service.MoocFileService;
import com.alibaba.excel.EasyExcel;
import com.sun.deploy.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/12/22
 */
@Slf4j
@RestController
@RequestMapping("/fileManage")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class FileManageController {


    @Autowired
    private MoocFileService moocFileService;


    @Value("${mooc.file.path}")
    private String FILE_PATH;

    /**
     * 分页查询moocFile接口
     * get请求
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(MoocFile matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(moocFileService.findPage(matchObject, pageIndex, pageSize));
    }



    @GetMapping("/export")
    public void exportExcel(MoocFile matchObject, HttpServletResponse response) throws IOException {
        List<FileExport> fileExportList = moocFileService.findAllByCondition(matchObject);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("文件列表"+ new SimpleDateFormat("yyyy-MM-dd").format(new Date()), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), FileExport.class).sheet("sheet1").doWrite(fileExportList);
    }





    /**
     * 更新moocFile接口
     * 请求方法: put
     * @param moocFile
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody MoocFile moocFile) {
        MoocFile update = moocFileService.update(moocFile);
        if (update == null) {
            return RespResult.fail("更新MoocFile失败");
        }
        return RespResult.success("更新MoocFile成功");
    }

    /**
     * 插入或更新moocFile
     * 请求方法: post
     * @param moocFile
     * @return
     */
    @PostMapping("/save")
    public RespResult insertOrUpdate(@RequestBody MoocFile moocFile) {
        MoocFile update = moocFileService.insertOrUpdate(moocFile);
        if (update == null) {
            return RespResult.fail("新增MoocFile失败");
        }
        return RespResult.success("新增MoocFile成功");
    }

    /**
     * 删除moocFile接口
     * 请求方法: delete
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = moocFileService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除MoocFile失败");
        }
        return RespResult.success("删除MoocFile成功");
    }

    /**
     * 批量删除moocFile接口
     * 请求方法: post
     * @param /batch/delete
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> moocFileIdList) {
        moocFileService.deleteAllByIds(moocFileIdList);
        return RespResult.success("批量删除MoocFile成功");
    }


    /**
     * 文件下载方法
     * @param fileId
     * @param response
     */
    @GetMapping("/download/{fileId}")
    public void downloadFile(@PathVariable("fileId") Integer fileId, HttpServletResponse response) {

        MoocFile moocFile = moocFileService.findById(fileId);
        if (moocFile == null) {
            log.error("该文件不存在，数据库查询记录为null,对应文件Id为：{}",fileId);
            return;
        }
        String mappingPath = moocFile.getFilePath();
        String filename = moocFile.getName() + "." +moocFile.getFileSuffix();
        String filepath = FILE_PATH + File.separator + mappingPath.substring(FileConstant.MAPPING_PATH.length());
        File file = new File(filepath);
        // 如果文件不存在，则结束
        if (!file.exists()) {
            log.error("文件下载发生异常，文件路径不存在，文件Id为：{} ，文件路径：{}",fileId,filepath);
            return;
        }

        // 配置文件下载响应头
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        // 下载文件能正常显示中文
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        // 实现文件下载
        byte[] buffer = new byte[1024];
        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
            OutputStream os = response.getOutputStream();
            int i = bufferedInputStream.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bufferedInputStream.read(buffer);
            }
        } catch (Exception e) {
            log.error("文件下载发生异常，文件路径不存在，文件Id为：{} ，文件路径：{}",fileId,filename,filepath);
        }


    }

}
