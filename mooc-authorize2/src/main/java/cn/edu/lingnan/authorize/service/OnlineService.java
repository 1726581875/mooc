package cn.edu.lingnan.authorize.service;

import cn.edu.lingnan.authorize.client.NoticeServiceClient;
import cn.edu.lingnan.authorize.dao.ManagerDAO;
import cn.edu.lingnan.authorize.dao.UserDAO;
import cn.edu.lingnan.authorize.model.entity.OnlineUser;
import cn.edu.lingnan.authorize.util.PageUtil;
import cn.edu.lingnan.authorize.util.RedisKeyUtil;
import cn.edu.lingnan.mooc.common.exception.MoocException;
import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.common.model.NoticeDTO;
import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/11/25
 */
@Service
public class OnlineService {

    @Autowired
    private AuthorizeService authorizeService;
    @Resource
    private UserDAO userDAO;
    @Resource
    private ManagerDAO managerDAO;
    @Resource
    private NoticeServiceClient noticeServiceClient;

    /**
     * @param pageIndex
     * @param pageSize
     * @param matchStr
     * @return
     */
    public PageVO<OnlineUser> getOnlineUserByPage(Integer pageIndex, Integer pageSize, String matchStr) {
        // 查询所有在线用户
        List<OnlineUser> allOnlineUser = getAllOnlineUser();
        if (allOnlineUser.isEmpty()) {
            return new PageVO<>(pageIndex, pageSize, 0, 0L, null);
        }
        // 条件查询
        if (matchStr != null && !"".equals(matchStr)) {
            String accountOrName = matchStr.trim();
            List<OnlineUser> onlineUserList = allOnlineUser.stream().filter(
                    user -> (accountOrName.contains(user.getAccount()) || accountOrName.contains(user.getName()))
            ).collect(Collectors.toList());
            return PageUtil.getPageVO(onlineUserList, pageIndex, pageSize);
        }

        return PageUtil.getPageVO(allOnlineUser, pageIndex, pageSize);
    }


    /**
     * 获取所有在线用户
     *
     * @return
     */
    public List<OnlineUser> getAllOnlineUser() {
        List<OnlineUser> onlineUserList = new ArrayList<>();
        // 获取redis所有在线用户的key
        Set<String> accountKeySet = RedisUtil.getRedisTemplate().keys(RedisKeyUtil.ONLINE_USER_PREFIX + "*");
        if (CollectionUtils.isEmpty(accountKeySet)) {
            return onlineUserList;
        }
        // 根据key获取在线用户信息
        accountKeySet.forEach(accountKey -> {
            OnlineUser onlineUser = RedisUtil.get(accountKey, OnlineUser.class);
            // 管理员和教师在不同的表，账号可能相同，加个前缀区分
            onlineUser.setAccount(onlineUser.getType() + ":" + onlineUser.getAccount());
            onlineUserList.add(onlineUser);
        });
        return onlineUserList;
    }

    /**
     * 使用户下线
     * 1、删除redis里的token 和用户在线信息
     * 2、获取用户Id，webSock推送消息通知用户，“您已经被下线”
     *
     * @param accountList 用户账户list
     */
    public void offline(List<String> accountList) {

        if (CollectionUtils.isEmpty(accountList)) {
            return;
        }

        List<String> userAccountList = new ArrayList<>();
        List<String> managerAccountList = new ArrayList<>();

        //1、使登录用户下线,传过来得账号是带类型前缀的，比如 MANAGER:xmz 、 TEACHER:xmz
        final int splitLength = 2;
        accountList.forEach(accountStr -> {
            String[] split = accountStr.split(":");
            if (split.length != splitLength) {
                throw new MoocException("下线用户失败，账号有问题");
            }
            String type = split[0];
            String account = split[1];

            // 清除用户token
            authorizeService.clearRedisLoginInfo(UserTypeEnum.valueOf(type), account);

            if (UserTypeEnum.TEACHER.name().equals(type) || UserTypeEnum.USER.name().equals(type)) {
                userAccountList.add(account);
            } else {
                managerAccountList.add(account);
            }
        });


        // 获取在线用户id,webSock推送消息
        if (!CollectionUtils.isEmpty(userAccountList)) {
            NoticeDTO noticeDTO = new NoticeDTO();

            List<Integer> userIdList = userDAO.findUserIdByAccountList(userAccountList);

            //noticeServiceClient.sendOfflineNotice(UserTokenUtil.createToken(),userIdList,false);
        }
        // 获取在线管理员id,webSock推送消息
        if (!CollectionUtils.isEmpty(managerAccountList)) {
            List<Integer> managerIdList = managerDAO.findManagerIdByAccountList(managerAccountList);
             //noticeServiceClient.sendOfflineNotice(UserTokenUtil.createToken(),managerIdList,true);

        }


    }

    private void buildUserOnlineNoticeDTO(){

    }




}
