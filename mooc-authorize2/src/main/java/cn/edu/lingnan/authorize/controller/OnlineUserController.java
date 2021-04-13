package cn.edu.lingnan.authorize.controller;

import cn.edu.lingnan.authorize.service.OnlineService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
    @GetMapping("/list")
    public RespResult findOnlineByCondition(@RequestParam(value="pageIndex",defaultValue = "1") Integer pageIndex,
                                            @RequestParam(value="pageSize",defaultValue = "10") Integer pageSize,
                                            @RequestParam(value="matchStr",defaultValue = "") String matchStr){
        return RespResult.success(onlineService.getOnlineUserByPage(pageIndex,pageSize,matchStr));
    }

    @PostMapping("/offline/{account}")
    public RespResult offlineByAccount(@PathVariable String account){
        List<String> accountList = new ArrayList<>(1);
        accountList.add(account);
        // 使用户下线
        onlineService.offline(accountList);
        return RespResult.success();
    }

    @PostMapping("/offline/list")
    public RespResult offlineByAccountList(@RequestBody List<String> accountList){
        // 使用户下线
        onlineService.offline(accountList);
        return RespResult.success();
    }

}
