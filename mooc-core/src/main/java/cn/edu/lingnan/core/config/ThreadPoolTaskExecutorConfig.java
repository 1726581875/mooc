package cn.edu.lingnan.core.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ThreadPoolTaskExecutorConfig {
    @Bean
    public Executor asyncPromiseExecutor() {
        ThreadPoolTaskExecutor executor;
        executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(100);
        //最大线程数
        executor.setMaxPoolSize(150);
        //队列容量
        executor.setQueueCapacity(99999);
        //活跃时间
        executor.setKeepAliveSeconds(300);
        //线程名字前缀
        executor.setThreadNamePrefix("MyExecutor-");
        //设置allowCoreThreadTimeout=true（默认false）时，核心线程会超时关闭
        executor.setAllowCoreThreadTimeOut(true);
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        executor.initialize();
        return executor;
    }
}