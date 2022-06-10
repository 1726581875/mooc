package cn.edu.lingnan.mooc.core.client;

import cn.edu.lingnan.mooc.core.client.hystrix.DocmanHystrix;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xiaomingzhang
 * @date 2021/12/14
 */
@FeignClient(value = "test", url = "http://127.0.0.1:9002", fallback = DocmanHystrix.class)
public interface DocmanClient {

    @PostMapping("/postTest")
    @Headers("")
    String getManagerList(@RequestBody FeignParam feignParam);

}
