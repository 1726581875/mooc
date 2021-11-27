package cn.edu.lingnan.mooc.core.repository;
import cn.edu.lingnan.mooc.core.entity.MoocFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xmz
 * @date 2020/10/31
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface MoocFileRepository extends JpaRepository<MoocFile, Integer>,JpaSpecificationExecutor<MoocFile> {

}
