package cn.edu.lingnan.core.service.reception;

import cn.edu.lingnan.core.entity.MoocUser;
import cn.edu.lingnan.core.repository.MoocUserRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.core.vo.reception.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

}
