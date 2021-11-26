package cn.edu.lingnan.mooc.authorize.controller;

import cn.edu.lingnan.mooc.authorize.service.AdminMenuTreeService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@RestController
@RequestMapping("/admin/menu")
public class MenuTreeController {

    @Autowired
    private AdminMenuTreeService menuTreeService;

    /**
     * 获取权限菜单树
     * @return
     */
    @GetMapping("/tree")
    public RespResult getMenuTree(){
        return RespResult.success(menuTreeService.getMenuTree());
    }

}
