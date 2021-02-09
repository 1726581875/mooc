package cn.edu.lingnan.authorize.service;

import cn.edu.lingnan.authorize.constant.UserConstant;
import cn.edu.lingnan.authorize.model.OnlineUser;
import cn.edu.lingnan.authorize.util.PageUtil;
import cn.edu.lingnan.authorize.util.RedisUtil;
import cn.edu.lingnan.mooc.common.model.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
     * @param accountList 用户账户list
     */
    public void offline(List<String> accountList){
        if(CollectionUtils.isEmpty(accountList)){
            return;
        }
        accountList.forEach(authorizeService::delRedisTokenOnline);
    }




}
