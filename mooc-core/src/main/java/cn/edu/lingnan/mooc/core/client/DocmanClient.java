package cn.edu.lingnan.mooc.core.client;

import cn.edu.lingnan.mooc.core.client.hystrix.DocmanHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author xiaomingzhang
 * @date 2021/12/14
 */
@FeignClient(value = "docman", url = "http://192.168.23.172:9008", fallback = DocmanHystrix.class)
public interface DocmanClient {

    @GetMapping("/manager/list")
    String getManagerList();

}
