package cn.edu.lingnan.mooc.core.client.hystrix;

import cn.edu.lingnan.mooc.core.client.DocmanClient;

/**
 * @author xiaomingzhang
 * @date 2021/12/14
 */
public class DocmanHystrix implements DocmanClient {
    @Override
    public String getManagerList() {
        return "error";
    }
}
