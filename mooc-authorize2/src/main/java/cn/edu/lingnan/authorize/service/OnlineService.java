package cn.edu.lingnan.authorize.service;

import cn.edu.lingnan.authorize.authentication.util.UserTokenUtil;
import cn.edu.lingnan.authorize.client.NoticeServiceClient;
import cn.edu.lingnan.authorize.constant.UserConstant;
import cn.edu.lingnan.authorize.dao.ManagerDAO;
import cn.edu.lingnan.authorize.dao.UserDAO;
import cn.edu.lingnan.authorize.model.entity.OnlineUser;
import cn.edu.lingnan.authorize.util.PageUtil;
import cn.edu.lingnan.authorize.util.RedisUtil;
import cn.edu.lingnan.mooc.common.model.PageVO;
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
     *
     * @param pageIndex
     * @param pageSize
     * @param matchStr
     * @return
     */
    public PageVO<OnlineUser> getOnlineUserByPage(Integer pageIndex,Integer pageSize, String matchStr){
        // 查询所有在线用户
        List<OnlineUser> allOnlineUser = getAllOnlineUser();
        if(allOnlineUser.isEmpty()){
            return new PageVO<>(pageIndex,pageSize,0,0,null);
        }
        // 条件查询
        String accountOrName = matchStr.trim();
        if(matchStr != null && !matchStr.equals("")) {
            List<OnlineUser> onlineUserList = allOnlineUser.stream().filter(user -> user.getAccount().equals(accountOrName)).collect(Collectors.toList());
            //如果账户不匹配
            if (onlineUserList.isEmpty()) {
                //匹配名称
                onlineUserList = allOnlineUser.stream().filter(user -> user.getName().contains(matchStr)).collect(Collectors.toList());
                if (onlineUserList.isEmpty()) {
                    return new PageVO<>(pageIndex, pageSize, 0, 0, null);
                }
            }
            return PageUtil.getPageVO(onlineUserList,pageIndex,pageSize);
        }

        return PageUtil.getPageVO(allOnlineUser,pageIndex,pageSize);
    }




    /**
     * 获取所有在线用户
     * @return
     */
    public List<OnlineUser> getAllOnlineUser(){
        List<OnlineUser> onlineUserList = new ArrayList<>();
        // 获取redis所有在线用户的key
        Set<String> accountKeySet = RedisUtil.getRedisTemplate().keys(UserConstant.ONLINE_USER_PREFIX + "*");
        if(CollectionUtils.isEmpty(accountKeySet)) {
            return onlineUserList;
        }
        // 根据key获取在线用户信息
        accountKeySet.forEach(accountKey->onlineUserList.add(RedisUtil.get(accountKey,OnlineUser.class)));
        return onlineUserList;
    }

    /**
     * 使用户下线
     * 1、删除redis里的token 和用户在线信息
     * 2、获取用户Id，webSock推送消息通知用户，“您已经被下线”
     * @param accountList 用户账户list
     */
    public void offline(List<String> accountList){
        if(CollectionUtils.isEmpty(accountList)){
            return;
        }
        //1、使登录用户下线
        accountList.forEach(authorizeService::delRedisTokenOnline);

        //2、找出用户ID,发送webSock推送
        //获取用户账号
        List<String> userAccountList = new ArrayList<>();
        //获取管理员账号
        List<String> managerAccountList = new ArrayList<>();
        //循环遍历出账号
        accountList.forEach(account -> {
            if(account.contains("teacher-")){
                userAccountList.add(account.replace("teacher-",""));
            }else if(account.contains("user-")){
                userAccountList.add(account.replace("user-",""));
            }else {
                managerAccountList.add(account);
            }
        });

        //获取在线用户id,webSock推送消息
        if(!CollectionUtils.isEmpty(userAccountList)){
            List<Integer> userIdList = userDAO.findUserIdByAccountList(userAccountList);
            noticeServiceClient.sendOfflineNotice(UserTokenUtil.createToken(),userIdList,false);
        }
        //获取在线管理员id,webSock推送消息
        if(!CollectionUtils.isEmpty(managerAccountList)){
            List<Integer> managerIdList = managerDAO.findManagerIdByAccountList(managerAccountList);
            noticeServiceClient.sendOfflineNotice(UserTokenUtil.createToken(),managerIdList,true);

        }


    }




}
