package cn.edu.lingnan.mooc.repository;

import cn.edu.lingnan.mooc.entity.MenuTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/08
 */
public interface MenuTreeRepository extends JpaRepository<MenuTree,Integer> {

    @Query(value = "select m.* from menu_tree m, role_menu_rel r where m.id = r.menu_id and role_id in ?1",nativeQuery=true)
    List<MenuTree> findMenuPermByRoleIds(List<Integer> roleIdList);
}
