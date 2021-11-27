package cn.edu.lingnan.mooc.core.model.excelImport;

import cn.edu.lingnan.mooc.core.entity.IpBlacklist;
import cn.edu.lingnan.mooc.core.repository.IpBlacklistRepository;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class IpBlacklistListener extends AnalysisEventListener<IpBlacklistImport> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpBlacklistListener.class);
    /**
     * 每隔3000条存储数据库
     */
    private static final int BATCH_COUNT = 3000;

    List<IpBlacklist> importBlacklistList = new ArrayList<>();
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private IpBlacklistRepository ipBlacklistRepository;

    public IpBlacklistListener() {

    }

    /**
     *
     * 使用构造方法把ipBlacklistRepository实例传进来
     * @param ipBlacklistRepository
     */
    public IpBlacklistListener(IpBlacklistRepository ipBlacklistRepository) {
        this.ipBlacklistRepository = ipBlacklistRepository;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param rowData
     * @param context
     */
    @Override
    public void invoke(IpBlacklistImport rowData, AnalysisContext context) {

        if(null == rowData || StringUtils.isEmpty(rowData.getIp()) || StringUtils.isEmpty(rowData.getName())){
            LOGGER.warn("====解析到一条异常数据:{} ,跳过", rowData);
            return;
        }
        LOGGER.info("=====解析到一条数据:{}", rowData);
        importBlacklistList.add(new IpBlacklist(rowData.getName(),rowData.getIp().trim()));
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (importBlacklistList.size() >= BATCH_COUNT) {
            LOGGER.info("==== 数据量已经到达 {} 条，入库====", importBlacklistList.size());
            saveData();
            // 存储完成清理 list
            importBlacklistList.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        LOGGER.info("所有数据解析完成！");
    }

    /**
     * 上存储数据库
     */
    private void saveData() {
        LOGGER.info("===={}条数据，开始存储数据库！====", importBlacklistList.size());
        ipBlacklistRepository.saveAll(importBlacklistList);
        LOGGER.info("==== 存储数据库成功！======");
    }
}