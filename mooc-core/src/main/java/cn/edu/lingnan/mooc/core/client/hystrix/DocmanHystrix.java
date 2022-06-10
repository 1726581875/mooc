package cn.edu.lingnan.mooc.core.client.hystrix;

import cn.edu.lingnan.mooc.core.client.DocmanClient;
import cn.edu.lingnan.mooc.core.client.FeignParam;

/**
 * @author xiaomingzhang
 * @date 2021/12/14
 */
public class DocmanHystrix implements DocmanClient {
    @Override
    public String getManagerList(FeignParam feignParam) {
        return "error";
    }
}
