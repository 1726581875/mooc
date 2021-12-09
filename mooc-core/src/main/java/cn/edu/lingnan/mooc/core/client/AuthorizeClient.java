package cn.edu.lingnan.mooc.core.client;

import cn.edu.lingnan.mooc.core.client.hystrix.CoreNoticeHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "mooc-authorize",fallback = CoreNoticeHystrix.class)
public interface AuthorizeClient {

    @GetMapping(value = "/hello")
    String getHello();

}
