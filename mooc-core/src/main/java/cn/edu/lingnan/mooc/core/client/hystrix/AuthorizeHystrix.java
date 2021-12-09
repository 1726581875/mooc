package cn.edu.lingnan.mooc.core.client.hystrix;

import cn.edu.lingnan.mooc.core.client.AuthorizeClient;

public class AuthorizeHystrix implements AuthorizeClient {
    @Override
    public String getHello() {
        return "fail";
    }
}
