package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.service.MoocManagerService;
import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.core.entity.MoocManager;
import cn.edu.lingnan.core.util.SpringContextHolder;
import cn.edu.lingnan.core.vo.MoocManagerVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

/**
 * @author xmz
 * @date: 2020/11/10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MoocManagerServiceTest {

    @Autowired
    private MoocManagerService moocManagerService ;

    @Test
    public void findByPageTest(){
        MoocManagerService service = SpringContextHolder.getBean(MoocManagerService.class);
        MoocManager moocManager = new MoocManager();
        Integer pageInsex = 1;
        Integer pageSize = 10;
        PageVO<MoocManagerVO> pageVO = service.findPage("moocManager", pageInsex, pageSize);
        Assert.notEmpty(pageVO.getContent(),"分页查询获取不到数据");
        System.out.println(pageVO.getContent());

    }

}
