package cn.edu.lingnan.authorize.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.authorize.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xmz
 * @date: 2020/11/24
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;


    public RespResult addManager(){

        return null;
    }




}
