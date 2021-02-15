package cn.edu.lingnan.authorize.controller;

import cn.edu.lingnan.authorize.authentication.util.UserUtil;
import cn.edu.lingnan.authorize.param.PasswordParam;
import cn.edu.lingnan.authorize.service.ManagerService;
import cn.edu.lingnan.authorize.service.reception.ReceptionUserService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xmz
 * @date: 2020/11/24
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;
    @Autowired
    private ReceptionUserService receptionUserService;
    @PostMapping("/updatePassword")

    /**
     * 管理员修改密码
     */
    public RespResult updatePassword(@RequestBody PasswordParam passwordParam){

        RespResult respResult = null;
        //教师或者管理员位于不同的表，这里需要判断
        if(UserUtil.getUserToken().getAccount().startsWith("teacher-")) {
            respResult = receptionUserService.updatePassword(passwordParam);
        }else {
            respResult = managerService.updatePassword(passwordParam);
        }
        return RespResult.success(respResult);
    }

}
