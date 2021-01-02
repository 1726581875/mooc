package cn.edu.lingnan.mooc.file.repository;

import cn.edu.lingnan.mooc.file.service.MoocFileService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xmz
 * @date: 2020/12/29
 */
@SpringBootTest
public class MoocFileRepositoryTest {

    @Autowired
    private MoocFileRepository moocFileRepository;
    @Autowired
    private MoocFileService moocFileService;

    @Test
    public void getUserNameMap(){
        moocFileService.getUserNameMap(Lists.newArrayList(1,2,3))
                .forEach((k,v) -> System.out.println("k:" + k + ",v=" + v));
    }

    @Test
    public void getCourseNameMap(){
        moocFileService.getCourseNameMap(Lists.newArrayList(1,2,3))
                .forEach((k,v) -> System.out.println("k:" + k + ",v=" + v));
    }

}
