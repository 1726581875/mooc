package cn.edu.lingnan.mooc.repository;

import cn.edu.lingnan.mooc.entity.Chapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author xmz
 * @date: 2020/10/11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ChapterRepositoryTest {

    @Autowired
    private ChapterRepository chapterRepository;

    @Test
    public void testSave(){
        Optional<Chapter> optionalChapter = chapterRepository.findById(22);
        Chapter chapter = optionalChapter.get();
        chapter.setCourseId(3);
        chapter.setName("大傻b1");
        Chapter save = chapterRepository.save(chapter);
        System.out.println(save);
/*        Optional<Chapter> optionalChapter = chapterRepository.findById(88);
        System.out.println(optionalChapter.get());*/
    }


    @Test
    public void testFind(){
        Optional<Chapter> optionalChapter = chapterRepository.findById(88);
        System.out.println(optionalChapter.get());
    }

}
