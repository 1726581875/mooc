package cn.edu.lingnan.mooc.file.controller;

import cn.edu.lingnan.mooc.file.constant.FileConstant;
import cn.edu.lingnan.mooc.file.entity.MoocFile;
import cn.edu.lingnan.mooc.file.enums.FileStatusEnum;
import cn.edu.lingnan.mooc.file.service.MoocFileService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.file.service.SectionService;
import cn.edu.lingnan.mooc.file.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author xmz
 * @date: 2020/10/06
 */
@Slf4j
@RestController
@RequestMapping("/video")
@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
public class VideoController {

    @Autowired
    private MoocFileService moocFileService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private VideoService videoService;
    @Value("${mooc.file.path}")
    private String FILE_PATH;

    @PostMapping("/upload")
    public RespResult upload(@RequestParam(value = "fileShard") MultipartFile fileShard, //分片文件
                         String suffix,  //文件后缀
                         String fileKey, //文件唯一标识
                         Integer fileSize, // 文件大小 字节
                         Integer shardIndex, //分片下标
                         Integer shardSize, //分片大小
                         Integer shardCount) { //分片总数

        String newFileName = "";
        // 入库对象（插入或更新）
        MoocFile moocFile = new MoocFile();
        // 根据key查找，看看文件表是否存在该文件
        MoocFile dbMoocFile = moocFileService.findByFileKey(fileKey);
        if (dbMoocFile != null) {
            newFileName = dbMoocFile.getName();
            //如果存在,设置id，执行更新操作
            moocFile.setId(dbMoocFile.getId());
            moocFile.setName(newFileName);
        } else {
            // 新生成的文件名
            newFileName = UUID.randomUUID().toString();
            moocFile.setName(newFileName);
        }

        // 文件相对路径
        String fileRelativePath = newFileName + "." + suffix;
        File newFile = new File(FILE_PATH + File.separator + fileRelativePath + ".shard-" + shardIndex);
        // 如果该文件所在目录不存在，则创建该目录
        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }

        try {
            fileShard.transferTo(newFile);
        } catch (IOException e) {
            log.error("分片上传文件失败，error={}", e);
        }

        // 构造入库对象
        moocFile.setFileKey(fileKey);
        moocFile.setName(newFileName);
        moocFile.setFileSize(fileSize);
        moocFile.setFileSuffix(suffix);
        moocFile.setShardIndex(shardIndex);
        moocFile.setShardSize(shardSize);
        moocFile.setShardCount(shardCount);
        moocFile.setFilePath(FileConstant.MAPPING_PATH + fileRelativePath);
        moocFile.setStatus(FileStatusEnum.NORMAL.getStatus());
        moocFile.setCourseId(1);
        moocFile.setFileType(1);
        moocFile.setUserId(1);

        log.info("moocFile={}", moocFile);
        // 插入或更新
        MoocFile moocFile1 = moocFileService.insertOrUpdate(moocFile);
        // 如果是最后一个分片
        if(shardIndex == shardCount){
            // 合并分片
            mergeShard(fileKey);
            // 返回文件相对路径给前端
            Map<String,Object> respMap = new HashMap<>(2);
            respMap.put("filePath", FileConstant.MAPPING_PATH + fileRelativePath);
            respMap.put("fileId",moocFile1.getId());
            return RespResult.success(respMap,"上传成功");
        }

       return RespResult.success();
    }


    @PostMapping("/upload2")
    public RespResult uploadShard(@RequestParam(value = "fileShard") MultipartFile fileShard, MoocFile moocFile) { //分片总数

        // TODO 参数校验

        String newFileName = "";
        // 根据key查找，看看文件表是否存在该文件
        MoocFile dbMoocFile = moocFileService.findByFileKey(moocFile.getFileKey());
        if (dbMoocFile != null) {
            newFileName = dbMoocFile.getName();
            //如果存在,设置id，执行更新操作
            moocFile.setId(dbMoocFile.getId());
            moocFile.setName(newFileName);
        } else {
            // 新生成的文件名
            newFileName = UUID.randomUUID().toString();
            moocFile.setName(newFileName);
        }

        // 文件相对路径
        String fileRelativePath = File.separator + newFileName + "." + moocFile.getFileSuffix();
        moocFile.setFilePath(fileRelativePath);

        File newFile = new File(FILE_PATH + fileRelativePath + ".shard-" + moocFile.getShardIndex());
        // 如果该文件所在目录不存在，则创建该目录
        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }

        try {
            fileShard.transferTo(newFile);
        } catch (IOException e) {
            log.error("分片上传文件失败，error={}", e);
        }
        log.info("moocFile={}", moocFile);
        // 插入或更新
        moocFileService.insertOrUpdate(moocFile);

        return RespResult.success();
    }

    /**
     * 获取文件分片下标
     * @param key
     * @return
     */
    @GetMapping("/key/{key}/shardIndex")
    public RespResult getFileShardIndexByKey(@PathVariable String key) {

        MoocFile moocFile = moocFileService.findByFileKey(key);
        if (moocFile == null) {
            return RespResult.success(1);
        }
        if(moocFile.getShardIndex() == moocFile.getShardCount()){
            return RespResult.success(moocFile.getShardIndex());
        }
        return RespResult.success(moocFile.getShardIndex());
    }

    /**
     * 文件合并，方法已过期
     * @SEE 被mergeShard()方法取代
     * @param key
     * @return
     * @throws FileNotFoundException
     */
    @Deprecated
    public RespResult merge(String key) throws FileNotFoundException {

        MoocFile file = moocFileService.findByFileKey(key);
        if (file == null) {
            log.warn("分片合并发生错误，文件key不存在");
            return RespResult.fail("文件上传发生错误");
        }

        int shardCount = file.getShardCount();

        String fileRerativePath = file.getFilePath();

        File mergeFile = new File(FILE_PATH + fileRerativePath);

        FileOutputStream fileOutputStream = new FileOutputStream(mergeFile, true);

        FileInputStream fileInputStream = null;//分片文件
        byte[] byt = new byte[10 * 1024 * 1024];
        int len;

        try {
            for (int i = 1; i <= shardCount; i++) {
                // 读取第i个分片
                fileInputStream = new FileInputStream(new File(FILE_PATH + fileRerativePath + ".shard-" + i)); //  course\6sfSqfOwzmik4A4icMYuUe.mp4.1
                while ((len = fileInputStream.read(byt)) != -1) {
                    fileOutputStream.write(byt, 0, len);
                }
            }
        } catch (IOException e) {
            log.error("分片合并异常", e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                fileOutputStream.close();
                log.info("IO流关闭");
            } catch (Exception e) {
                log.error("IO流关闭", e);
            }
        }
        log.info("合并分片结束");

        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("删除分片开始");
        for (int i = 1; i <= shardCount; i++) {
            String filePath = FILE_PATH + fileRerativePath + ".shard-" + i;
            File delFile = new File(filePath);
            boolean result = delFile.delete();
            log.info("删除{}，{}", filePath, result ? "成功" : "失败");
        }
        log.info("删除分片结束");

        return null;
    }


    @GetMapping("/key/{key}/merge")
    public RespResult mergeShard(@PathVariable String key){
        log.info("====合并分片开始====");
        //  查出文件表
        MoocFile file = moocFileService.findByFileKey(key);
        if (file == null) {
            log.warn("分片合并发生错误，文件key不存在");
            return RespResult.fail("文件上传发生错误");
        }

        int shardCount = file.getShardCount();
        String fileMappingPath = file.getFilePath();
        String fileRelativePath = fileMappingPath.substring(FileConstant.MAPPING_PATH.length());

        File mergeFile = new File(FILE_PATH + File.separator + fileRelativePath);

        try(FileOutputStream fileOutput = new FileOutputStream(mergeFile, true);
             BufferedOutputStream buffOutput = new BufferedOutputStream(fileOutput, 8 * 1024 * 10)){
            byte[] bytes = new byte[8 * 1024];
            int len;
            for (int i = 1; i <= shardCount; i++) {
                try(FileInputStream fileInput = new FileInputStream(new File(FILE_PATH + File.separator + fileRelativePath + ".shard-" + i));
                BufferedInputStream buffInput = new BufferedInputStream(fileInput, 8 * 1024 * 10)) {
                    while ((len = buffInput.read(bytes)) != -1) {
                        buffOutput.write(bytes, 0, len);
                    }
                    buffOutput.flush();
                }catch (Exception e){
                    log.error("文件合并发生错误，{}",e);
                }
            }
            // 提醒java执行gc操作，否则下面删除分片文件会失败
            System.gc();
            Thread.sleep(100);

        }catch (Exception e){
            log.error("文件合并发生错误，{}",e);
        }

        log.info("====合并分片结束====");

        // 删除分片
        log.info("====删除分片开始====");
        for (int i = 1; i <= shardCount; i++) {
            String filePath = FILE_PATH  + File.separator + fileRelativePath + ".shard-" + i;
            File delFile = new File(filePath);
            boolean result = delFile.delete();
            log.info("删除{}，{}", filePath, result ? "成功" : "失败");
        }
        log.info("=====删除分片结束=====");

        return RespResult.success();
    }

    /**
     * 逻辑删除
     * @param fileId 文件id
     * @return
     */
    @DeleteMapping("/delete/{sectionId}/{fileId}")
    public RespResult deleteFile(@PathVariable("sectionId") Integer sectionId, @PathVariable("fileId") Integer fileId){
        // 更改小节url为空
        // 如果sectionId传了0，说明是新增还没有小节，不需要更改url
        if(sectionId != 0) {
            sectionService.setSectionVideoUrl(sectionId, "");
        }
        // 检查是否要逻辑删除文件表
        videoService.checkSectionExists(fileId);
        return RespResult.success();
    }

}
