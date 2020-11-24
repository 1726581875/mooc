package cn.edu.lingnan.mooc.util;

import cn.edu.lingnan.authorize.util.RsaUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@SpringBootTest
public class RsaUtilTest {

    @Value("${mooc.rsa.privateKey}")
    private String PRI_KEY;

    @Value("${mooc.rsa.publicKey}")
    private String PUB_KEY;

    private String password = "xmz";

    @Test
    public void test() throws Exception {

        String encryptResult = RsaUtil.encryptByPublicKey(PUB_KEY, password);
        String decryptResult = RsaUtil.decryptByPrivateKey(PRI_KEY, encryptResult);
        System.out.println(decryptResult);
    }


}
