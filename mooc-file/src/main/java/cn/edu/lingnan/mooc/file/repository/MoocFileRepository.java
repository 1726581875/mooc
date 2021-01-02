package cn.edu.lingnan.mooc.file.repository;
import cn.edu.lingnan.mooc.file.entity.MoocFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author xmz
 * @date 2020/10/17
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface MoocFileRepository extends JpaRepository<MoocFile, Integer>,JpaSpecificationExecutor<MoocFile> {

}
