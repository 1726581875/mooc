package cn.edu.lingnan.mooc.file.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class TransactionUtils {
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    /**
     * 开启事务
     */
    public TransactionStatus beginTransaction(){
        //事务定义类
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        // 返回事务对象
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        return status;
    }
    /**
     * 提交事务
     * @param status
     */
    public void commitTransaction(TransactionStatus status){
        platformTransactionManager.commit(status);
    }

    /**
     * 事务回滚
     * @param status
     */
    public void rollbackTransaction(TransactionStatus status){
        platformTransactionManager.rollback(status);
    }
}
