package cn.edu.lingnan.mooc.core.repository;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author xmz
 * @date: 2020/12/06
 */
@SpringBootTest
public class TagRepositoryTest {

    @Resource
    private TagRepository tagRepository;

    @Test
    public void findAllTagByCategoryId(){
        tagRepository.findAllByCategoryId(2).forEach(System.out::println);
    }

    @Test
    public void findAllTagByCategoryIdIn(){
        tagRepository.findAllByCategoryIdIn(Lists.newArrayList(2,3)).forEach(System.out::println);
    }

    @Test
    public void findTagListByCourseIdTest(){
        tagRepository.findTagListByCourseId(1L).forEach(System.out::println);
    }


}
