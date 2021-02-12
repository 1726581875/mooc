package cn.edu.lingnan.authorize.controller;

import cn.edu.lingnan.authorize.param.PasswordParam;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author xmz
 * @date: 2020/11/24
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {

    @PostMapping("/updataPassword")
    public RespResult updatePassword(@RequestBody PasswordParam passwordParam){


        return RespResult.success();
    }

}
