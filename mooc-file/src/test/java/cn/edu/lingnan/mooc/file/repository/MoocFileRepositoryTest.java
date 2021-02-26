package cn.edu.lingnan.mooc.file.repository;

import cn.edu.lingnan.mooc.file.service.MoocFileService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

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

    @Test
    public void testRemoveAll(){
        int num = 100000;
        long initBeginTime = System.currentTimeMillis();
        List<String> accountList = new ArrayList<>(num);
        List<String> accountList2 = new ArrayList<>(num);
        for (int i = 0; i <= num; i++){
            String account = UUID.randomUUID().toString();
            accountList.add(account);
            if(i == 0) {
                accountList2.add("woyaobeishanchu");
            }else {
                accountList2.add(account);
            }
        }
        long initEndTime = System.currentTimeMillis();
        //打乱数组2
        Collections.shuffle(accountList2);

        System.out.println("初始化数据" + num + "时间=" + (initEndTime - initBeginTime) + "ms");
        System.out.println("数据量accountList=" + accountList.size() + ", accountList2=" + accountList2.size());

        //测试foreach-set耗时
        long setBeginTime = System.currentTimeMillis();
        Set<String> accountSet = new HashSet<>(accountList2);
        accountList.forEach(account -> {
            if(!accountSet.contains(account)){
                System.out.println("这个人要被删除2 account=" + account);
            }
        });
        long setEndTime = System.currentTimeMillis();
        System.out.println("set耗时 " + (setEndTime - setBeginTime) + "ms");

        //测试removeAll耗时
        long removeAllBeginTime = System.currentTimeMillis();
        accountList.removeAll(accountList2);
        long removeAllEndTime = System.currentTimeMillis();
        System.out.println("removeAll耗时 " + (removeAllEndTime - removeAllBeginTime) + "ms"
                + ",即" + (removeAllEndTime - removeAllBeginTime)/60000 + "分钟");

        System.out.println("removeAll后accountList=" + accountList.size());
        accountList.forEach(System.out::println);


    }

}
