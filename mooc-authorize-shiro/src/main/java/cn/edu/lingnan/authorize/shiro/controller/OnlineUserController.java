package cn.edu.lingnan.authorize.shiro.controller;

import cn.edu.lingnan.authorize.shiro.service.OnlineService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/25
 */
@RestController
@RequestMapping("/onlineUser")
public class OnlineUserController {

    @Autowired
    private OnlineService onlineService;

    /**
     * 根据条件分页查找在线用户
     * @param pageIndex
     * @param pageSize
     * @param matchStr
     * @return
     */
    // todo 使用这个注解会没有数据返回
    //@RequiresPermissions("list")
    @GetMapping("/list")
    public RespResult findOnlineByCondition(@RequestParam(value="pageIndex",defaultValue = "1") Integer pageIndex,
                                            @RequestParam(value="pageSize",defaultValue = "10") Integer pageSize,
                                            @RequestParam(value="matchStr",defaultValue = "") String matchStr){
        return RespResult.success(onlineService.getOnlineUserByPage(pageIndex,pageSize,matchStr));
    }

    /**
     * 根据账号下线单个用户
     * @param account
     * @return
     */
    @PostMapping("/offline/{account}")
    public RespResult offlineByAccount(@PathVariable String account){
        List<String> accountList = new ArrayList<>(1);
        accountList.add(account);
        // 使用户下线
        onlineService.offline(accountList);
        return RespResult.success();
    }

    /**
     * 批量下线用户
     * @param accountList 用户账号list
     * @return
     */
    @PostMapping("/offline/list")
    public RespResult offlineByAccountList(@RequestBody List<String> accountList){
        System.out.println("accountList=" +  accountList);
        // 使用户下线
        onlineService.offline(accountList);
        return RespResult.success();
    }

}
