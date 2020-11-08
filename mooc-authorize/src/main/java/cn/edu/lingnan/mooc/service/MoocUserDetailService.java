package cn.edu.lingnan.mooc.service;

import cn.edu.lingnan.mooc.entity.MoocManager;
import cn.edu.lingnan.mooc.repository.MoocManagerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author xmz
 * @date: 2020/10/25
 */
@Slf4j
@Service
public class MoocUserDetailService  implements UserDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MoocManagerRepository moocManagerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 查询该用户
        MoocManager manager = new MoocManager();
        manager.setAccount(username);
        Optional<MoocManager> moocManagerOptional = moocManagerRepository.findOne(Example.of(manager));
       //用户名不存在
        if(!moocManagerOptional.isPresent()){
            log.info("该用户账号不存在");
            throw new UsernameNotFoundException("该用户账号不存在");
        }

        MoocManager moocManager = moocManagerOptional.get();
        log.info("moocManager = {}",moocManager);

/*        if(passwordEncoder.matches(moocManager.getPassword(),"")){
            log.info("密码有误");
            throw new UsernameNotFoundException("密码有误");
        }*/

        log.info("登录用户名："+ username);
        //根据用户名查找用户信息
        //根据查找到的用户信息判断用户是否被冻结



       // String password = passwordEncoder.encode(moocManager.getPassword());
        //log.info("password={}",password);
        //return new User(username,"123456",AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        return new User(username, moocManager.getPassword(),
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

    }
}
