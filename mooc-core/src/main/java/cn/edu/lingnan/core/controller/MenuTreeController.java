package cn.edu.lingnan.core.controller;

import cn.edu.lingnan.core.service.MenuTreeService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@RestController
@RequestMapping("/admin/menu")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class MenuTreeController {

    @Autowired
    private MenuTreeService menuTreeService;

    /**
     * 获取权限菜单树
     * @return
     */
    @GetMapping("/tree")
    public RespResult getMenuTree(){
        return RespResult.success(menuTreeService.getMenuTree());
    }

}
