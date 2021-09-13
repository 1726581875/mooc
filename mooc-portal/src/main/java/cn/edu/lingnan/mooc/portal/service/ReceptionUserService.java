package cn.edu.lingnan.mooc.portal.service;

import cn.edu.lingnan.mooc.portal.authorize.model.entity.MoocUser;
import cn.edu.lingnan.mooc.portal.authorize.util.CopyUtil;
import cn.edu.lingnan.mooc.portal.dao.MoocUserRepository;
import cn.edu.lingnan.mooc.portal.model.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author xmz
 * @date: 2021/02/08
 */
@Service
public class ReceptionUserService {

    @Autowired
    private MoocUserRepository moocUserRepository;

    public UserDetailVO findUserDetailById(Integer userId){
        Optional<MoocUser> userOptional = moocUserRepository.findById(userId);
        if(userOptional.isPresent()){
            return CopyUtil.copy(userOptional.get(),UserDetailVO.class);
        }
        return null;
    }

    /**
     * 根据用户id list 获取用户list
     * @param userIdList
     * @return
     */
    public Map<Integer,MoocUser> getUserMap(List<Integer> userIdList){

        List<MoocUser> moocUserList = moocUserRepository.findAllById(new HashSet<>(userIdList));
        Map<Integer, List<MoocUser>> userMapList = moocUserList.stream().collect(Collectors.groupingBy(MoocUser::getId));
        Map<Integer,MoocUser> resultMap = new HashMap<>();
        userMapList.forEach((k,v) -> resultMap.put(k,v.get(0)));
        return resultMap;

    }

}
