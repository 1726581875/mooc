package cn.edu.lingnan.core.controller;

import cn.edu.lingnan.core.entity.Category;
import cn.edu.lingnan.core.param.CategoryParam;
import cn.edu.lingnan.core.service.CategoryService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/12/05
 */
@RestController
@RequestMapping("/admin/categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;



    /**
     * 分查询所有分类信息
     * @return
     */
    @GetMapping("/all")
    public RespResult findAllCategory() {
        return RespResult.success(categoryService.findAll());
    }


    /**
     * 分页查询category接口
     * get请求
     * url: /admin/categorys/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(Category matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(categoryService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 更新category接口
     * 请求方法: put
     * url: /admin/categorys/{id}
     * @param category
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody Category category) {
        Integer flag = categoryService.update(category);
        if (flag == 0) {
            return RespResult.fail("更新Category失败");
        }
        return RespResult.success("更新Category成功");
    }

    /**
     * 插入或更新category
     * 请求方法: post
     * url: /admin/categorys/category
     * @param categoryParam
     * @return
     */
    @PostMapping("/category")
    public RespResult insertOrUpdate(@RequestBody CategoryParam categoryParam) {
        Integer flag = categoryService.insertOrUpdate(categoryParam);
        if (flag == 0) {
            return RespResult.fail("新增Category失败");
        }
        return RespResult.success("新增Category成功");
    }

    /**
     * 删除category接口
     * 请求方法: delete
     * url: /admin/categorys/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = categoryService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除Category失败");
        }
        return RespResult.success("删除Category成功");
    }

    /**
     * 批量删除category接口
     * 请求方法: post
     * url: /admin/categorys/bacth/delete
     * @param categoryIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> categoryIdList) {
        categoryService.deleteAllByIds(categoryIdList);
        return RespResult.success("批量删除Category成功");
    }

}