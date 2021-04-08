package cn.edu.lingnan.mooc.message.repository;


import cn.edu.lingnan.mooc.message.pojo.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NoticeRepository extends JpaRepository<Notice,Integer>, JpaSpecificationExecutor<Notice> {
    
}
