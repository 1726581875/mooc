package cn.edu.lingnan.mooc.service;

import cn.edu.lingnan.mooc.entity.MenuTree;
import cn.edu.lingnan.mooc.entity.MoocManager;
import cn.edu.lingnan.mooc.entity.Role;
import cn.edu.lingnan.mooc.repository.MenuTreeRepository;
import cn.edu.lingnan.mooc.repository.MoocManagerRepository;
import cn.edu.lingnan.mooc.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/10/25
 */
@Slf4j
@Service
public class MoocUserDetailService  implements UserDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private MoocManagerRepository moocManagerRepository;
    @Resource
    private MenuTreeRepository menuTreeRepository;
    @Resource
    private RoleRepository roleRepository;

    private  AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

/*        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);*/


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

    public Optional<MoocManager> findManagerByAccount(String account){
        // 查询该用户
        MoocManager manager = new MoocManager();
        manager.setAccount(account);
        return moocManagerRepository.findOne(Example.of(manager));
    }


    public List<MenuTree> getMenuByManagerId(Integer managerId){

        List<Role> roleList = roleRepository.findAllRoleByManagerId(managerId);
        if(CollectionUtils.isEmpty(roleList)){
            log.error("该管理员没有任何角色");
            return new ArrayList<>();
        }
        List<Integer> roleIdList = roleList.stream().map(Role::getId).collect(Collectors.toList());
        return menuTreeRepository.findMenuPermByRoleIds(roleIdList);
    }


}
