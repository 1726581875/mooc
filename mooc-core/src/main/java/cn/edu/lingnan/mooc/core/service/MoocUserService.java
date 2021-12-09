package cn.edu.lingnan.mooc.core.service;

import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.common.util.CopyUtil;
import cn.edu.lingnan.mooc.core.model.entity.MoocUser;
import cn.edu.lingnan.mooc.core.model.param.UserParam;
import cn.edu.lingnan.mooc.core.repository.MoocUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/12/07
 */
@Slf4j
@Service
public class MoocUserService {

    @Resource
    private MoocUserRepository moocUserRepository;

    @PersistenceContext
    private EntityManager entityManager;
    /**
     * 根据用户id list 获取用户list
     * @param userIdList
     * @return
     */
    public Map<Long, MoocUser> getUserMap(List<Long> userIdList){

        List<MoocUser> moocUserList = moocUserRepository.findAllById(new HashSet<>(userIdList));
        Map<Long, List<MoocUser>> userMapList = moocUserList.stream().collect(Collectors.groupingBy(MoocUser::getId));
        Map<Long,MoocUser> resultMap = new HashMap<>();
        userMapList.forEach((k,v) -> resultMap.put(k,v.get(0)));
        return resultMap;

/*        // 构造sql
        StringBuilder sqlBuilder = new StringBuilder("select id,user_image , name, account,status,login_time,create_time," +
                "update_time from mooc_user u where u.id in (");
        sqlBuilder.append(userIdList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        sqlBuilder.append(")");
        log.info("get user name sql is sql:{}",sqlBuilder.toString());
        // 执行语句
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        // 获取和构造返回结果
        List<Object[]> resultList = query.getResultList();
        Map<Integer,MoocUser> resultMap = new HashMap<>();
        resultList.forEach(o -> resultMap.put(Integer.valueOf(o[0].toString()),(MoocUser)o[1]));
        return resultMap;
        */

    }
    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public MoocUser findById(Long id){
        Optional<MoocUser> optional = moocUserRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<MoocUser> findAll(){
        return moocUserRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<MoocUser> findAllByCondition(MoocUser matchObject){
        return moocUserRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<MoocUser> findPage(MoocUser matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
        Specification<MoocUser> specification = (root, criteriaQuery, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> predicateOr = new ArrayList<>();
            Predicate conditionPre = null;

            //姓名模糊
            if(!StringUtils.isEmpty(matchObject.getName())){
                predicateOr.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + matchObject.getName() + "%"));
            }
            //账号模糊
            if(!StringUtils.isEmpty(matchObject.getAccount())){
                predicateOr.add(criteriaBuilder.like(root.get("account").as(String.class), "%" + matchObject.getAccount() + "%"));
            }
            //and 用户类型
            if(!StringUtils.isEmpty(matchObject.getUserType())){
                predicates.add(criteriaBuilder.equal(root.get("userType").as(String.class), matchObject.getUserType()));
            }
            //and status
            if(!StringUtils.isEmpty(matchObject.getStatus())){
                predicates.add(criteriaBuilder.equal(root.get("status").as(Integer.class), matchObject.getStatus()));
            }

            //构造where and/or
            if (!predicateOr.isEmpty()) {
                conditionPre = criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])),
                criteriaBuilder.or(predicateOr.toArray(new Predicate[predicateOr.size()]))).getRestriction();
            } else {
                conditionPre = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }

           return conditionPre;
        };
        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<MoocUser> moocUserPage = moocUserRepository.findAll(specification, pageable);
        //获取page对象里的list
        List<MoocUser> moocUserList = moocUserPage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<MoocUser> pageVO = new PageVO<>();
        pageVO.setContent(moocUserList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(moocUserPage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param moocUser
     * @return 返回成功数
     */
    public Integer insert(MoocUser moocUser){
        if (moocUser == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        MoocUser newMoocUser = moocUserRepository.save(moocUser);
        return newMoocUser == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param moocUser
     * @return 返回成功条数
     */
    public Integer update(UserParam moocUser){
        // 入参校验
        if(moocUser == null || moocUser.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<MoocUser> optional = moocUserRepository.findById(moocUser.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ moocUser.getId() +"的MoocUser");
        }
        MoocUser dbMoocUser = optional.get();
        //把不为null的属性拷贝到dbMoocUser
        CopyUtil.notNullCopy(moocUser, dbMoocUser);
        //执行保存操作
        MoocUser updateMoocUser = moocUserRepository.save(dbMoocUser);
        return updateMoocUser == null ? 0 : 1;
    }


    public Integer deleteById(Long id){
        moocUserRepository.deleteById(id);
        return findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param moocUserIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Long> moocUserIdList){
        List<MoocUser> delMoocUserList = moocUserRepository.findAllById(moocUserIdList);
        moocUserRepository.deleteInBatch(delMoocUserList);
        return delMoocUserList.size();
    }


}
