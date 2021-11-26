package cn.edu.lingnan.mooc.authorize.controller;

import cn.edu.lingnan.mooc.authorize.model.entity.MoocManager;
import cn.edu.lingnan.mooc.authorize.model.param.ManagerParam;
import cn.edu.lingnan.mooc.authorize.model.param.PasswordParam;
import cn.edu.lingnan.mooc.authorize.service.ManagerService;
import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.common.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/24
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    /**
     * 管理员修改密码
     */
    @PostMapping("/updatePassword")
    public RespResult updatePassword(@RequestBody PasswordParam passwordParam){

        RespResult respResult = null;
        //教师、用户或者管理员位于不同的表，这里需要判断
        if(UserTypeEnum.MANAGER.equals(UserUtil.getLoginUser().getType())) {
            respResult = managerService.updatePassword(passwordParam);
        }else {
            respResult = managerService.updatePassword(passwordParam);
        }
        return RespResult.success(respResult);
    }

    /**
     * 查询管理员列表
     * @param matchStr
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(String matchStr,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return RespResult.success(managerService.findPage(matchStr, pageIndex, pageSize));
    }

    @PutMapping("/{id}")
    public RespResult update(@RequestBody MoocManager moocManager) {
        Integer flag = managerService.update(moocManager);
        if (flag == 0) {
            return RespResult.fail("更新MoocManager失败");
        }
        return RespResult.success("更新MoocManager成功");
    }

    @PostMapping("/moocManager")
    @Transactional
    public RespResult insertOrUpdate(@RequestBody ManagerParam managerParam) throws Exception {
        Integer flag = managerService.insertOrUpdate(managerParam);
        if (flag == 0) {
            return RespResult.fail("新增MoocManager失败");
        }
        return RespResult.success("新增MoocManager成功");
    }

    @PostMapping("/{id}/status/{status}")
    @Transactional
    public RespResult updateStatus(@PathVariable("id") Long managerId,
                                   @PathVariable("status") Integer status) throws Exception {
        MoocManager manager = new MoocManager();
        manager.setId(managerId);
        manager.setStatus(status);
        Integer flag = managerService.update(manager);
        if (flag == 0) {
            return RespResult.fail("更新管理员状态失败");
        }
        return RespResult.success("更新管理员状态成功");
    }


    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = managerService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除MoocManager失败");
        }
        return RespResult.success("删除MoocManager成功");
    }


    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> moocManagerIdList) {
        managerService.deleteAllByIds(moocManagerIdList);
        return RespResult.success("批量删除MoocManager成功");
    }

}
