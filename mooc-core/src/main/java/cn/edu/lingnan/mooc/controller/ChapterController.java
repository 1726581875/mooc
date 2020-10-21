package cn.edu.lingnan.mooc.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.dto.ChapterDTO;
import cn.edu.lingnan.mooc.entity.Chapter;
import cn.edu.lingnan.mooc.service.ChapterService;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/10/06
 */
@Slf4j
@RestController
@RequestMapping("/admin/chapters")
@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    /**
     * 分页查询chapter接口
     * get请求
     * url: /admin/chapters/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(Chapter matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(chapterService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 更新chapter接口
     * 请求方法: put
     * url: /admin/chapters/{id}
     * @param chapter
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody Chapter chapter) {
        Integer flag = chapterService.update(chapter);
        if (flag == 0) {
            return RespResult.fail("更新Chapter失败");
        }
        return RespResult.success("更新Chapter成功");
    }

    /**
     * 插入或更新chapter
     * 请求方法: post
     * url: /admin/chapters/chapter
     * @param chapter
     * @return
     */
    @PostMapping("/chapter")
    public RespResult insertOrUpdate(@RequestBody Chapter chapter) {
        log.info("chapter={}", chapter);
        Integer flag = chapterService.insertOrUpdate(chapter);
        if (flag == 0) {
            return RespResult.fail("新增Chapter失败");
        }
        return RespResult.success("新增Chapter成功");
    }

    /**
     * 删除chapter接口
     * 请求方法: delete
     * url: /admin/chapters/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = chapterService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除Chapter失败");
        }
        return RespResult.success("删除Chapter成功");
    }

    /**
     * 批量删除chapter接口
     * 请求方法: post
     * url: /admin/chapters/bacth/delete
     * @param chapterIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> chapterIdList) {
        chapterService.deleteAllByIds(chapterIdList);
        return RespResult.success("批量删除${Domain}成功");
    }



    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link }
     * <p>
     * 2. 设置返回的 参数
     * <p>
     * 3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
     */
    @GetMapping("/test")
    public void download(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ChapterDTO.class).sheet("模板").doWrite(chapterService.findAll());
    }


}
