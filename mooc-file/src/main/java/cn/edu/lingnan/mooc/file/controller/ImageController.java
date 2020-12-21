package cn.edu.lingnan.mooc.file.controller;

import cn.edu.lingnan.mooc.file.util.FileUtils;
import cn.edu.lingnan.mooc.common.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author xmz
 * @date: 2020/10/30
 */
@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController {

    @Value("mooc.file.path")
    private String FILE_PATH;

    @PostMapping("/upload")
    public RespResult upload(MultipartFile file){
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取到文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 新文件名
        fileName = UUID.randomUUID() + suffixName;

        String fileFullPath = FILE_PATH + File.separator + fileName;
        File dest = new File(fileFullPath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            //保存文件
            file.transferTo(dest);
            //压缩到1M以下
            FileUtils.compressImgByThumbnails(fileFullPath,1024);
        } catch (IOException e) {
          log.info("图片上传发生异常",e);
        }


        return RespResult.success();
    }



}
