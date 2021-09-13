package cn.edu.lingnan.mooc.portal.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.portal.service.ReceptionUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xmz
 * @date: 2021/02/08
 */
@RestController
@RequestMapping("/users")
public class ReceptionUserController {

    @Autowired
    private ReceptionUserService receptionUserService;

    @GetMapping("/{userId}")
    public RespResult findUserById(@PathVariable Integer userId){
        return RespResult.success(receptionUserService.findUserDetailById(userId));
    }


}
