package cn.edu.lingnan.mooc.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xmz
 * @date: 2020/11/21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RsaUtilTest {


    private String PRI_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJz4kbAr5IsBWBY6lR61MITmzpTtmA6DDgDHrgHGtFfk6g/3CBNVnHJ/+2Z6KwU+Euci0HXTtqWPFFJqu5erfONnAGMmaW9whkkxAWSw6HAiJkQfT0U/NxOyvX1Q70ffRIaOg0DeWil+BSeqHmyFwzJ2KZRV9PlhZPZmkIcUc+k3AgMBAAECgYAZfJ6jHS1ZiJlV2qM1vjOnkJZARienS44tgD53mVzU3J1IhFKyWPcy/CbEp9duXXrSPqvcRcjlLssc6qmU4cpAHN6JUsb3oL7ot67hxdtxBglQLQ6ex1s2zNLe+E20xN117MPjdmvuy7YCJ8L265kSGWfKhognuRl9uzLaTD7w8QJBANK/uuDFfw9GvJVRcdTq2iH18PcSadVXLf91tzpeUzhiGUaINbnjdYj8p2PXKsOSF/ILuPtDgIHJyy9XFroO6AkCQQC+rNEjixn7nWNPgW8NbKPYuclYPPUPUiT4vk/dDTaCNviqyncVLWrakwFsPSLazaGpbtyGfH2VKnYwBFrwcBc/AkEAp6U3Hnhck6VcfJdWKt5LeTpwXcp6/+eiwpU8lSTVpIN8wbu2qVGEfN0mtuhBr2M2wyeKFe5jelsw006+2HDlKQJAapBFFhtAuznb7exFt3f3HkG/AHyY7SYYmZgrJDylMSMt2K3s3b4TUPMFwYttdmGkLwBRaSwcN4iSowVcGbJbmwJAWSm8C/pCbKNDgEAoyDjNCM27tLffgBeKucl2jhKbNUZizT46rn+bbSNiuGhuYYh0qJy5hw0axxJ/LWBihqHbSA==";

    private String PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCc+JGwK+SLAVgWOpUetTCE5s6U7ZgOgw4Ax64BxrRX5OoP9wgTVZxyf/tmeisFPhLnItB107aljxRSaruXq3zjZwBjJmlvcIZJMQFksOhwIiZEH09FPzcTsr19UO9H30SGjoNA3lopfgUnqh5shcMydimUVfT5YWT2ZpCHFHPpNwIDAQAB";

    // 公钥加密
    @Test
    public void testPublicEncrypt() throws Exception {
        String encryptStr = RsaUtil.encryptByPublicKey(PUB_KEY, "肖明章");
        System.out.println(encryptStr);
    }
    // 利用私钥解密
    @Test
    public void testPrivateDeEncrypt() throws Exception {
        String text = "Z/OvmjCVzz/z9g3oF8vBGHS6oh7VfHi11XLXO1Yg2Mx898PRCeZ810OO8wrAxYcKXmMdQPedlBsQdAV/Bbcu7CaUT7v3k+NVN/5zCpmoWlThW1Zb9yAqLiJ+5irVdA2JvX2/Tx95tjIZ9sbhqrNhozn/pKur20oLfAwzuWf9ybs=";
        String decryptResult = RsaUtil.decryptByPrivateKey(PRI_KEY, text);
        System.out.println(decryptResult);
    }

   // =========================这是分隔线 ^_^ =============

    // 私钥加密
    @Test
    public void testPrivateEncrypt() throws Exception {
        String encryptStr = RsaUtil.encryptByPrivateKey(PRI_KEY, "肖明章");
        System.out.println(encryptStr);
    }
    // 再利用公钥解密
    @Test
    public void testPublicDeEncrypt() throws Exception {
        String text = "TrtVLQ+B0zkXMqqT5KMa9gnBSD9hfQe8msM8WEWpDm492i2ofIF/8yV8EhA3o2IQ+AUZn6sZxCmYOrERNTm9Xkz3sS3P9ipwPxQsYNsf7dBVk90k1O9EhEHUMRqwB46aQpmhkyhPmYms2kKoLorg8SXYYmb3JFA/XOlC8bwO650=";
        String decryptResult = RsaUtil.decryptByPublicKey(PUB_KEY, text);
        System.out.println(decryptResult);
    }

    @Test
    public void testEque(){
        System.out.println(PUB_KEY.equals("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCJ2MUCgeKIDKr9qGkM3C+1eXufEuxOPO8K9hwNwnrrZYpOOGTUMY8RP85T0pBioUhd1N2jrBBdvrb1GnGGIt/r9HFPzehQLE49MG2RMrDujApw5Z8JHj2Silp7os4Qs3CgOQRahuoHR3oprggRO25uFd9KNCkea6ffsOU/cyW6wIDAQAB"));
    }


}
