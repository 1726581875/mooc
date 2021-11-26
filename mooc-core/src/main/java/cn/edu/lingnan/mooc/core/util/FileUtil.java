package cn.edu.lingnan.mooc.core.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;

/**
 * @Author: Carlson
 * @Date: 2020/6/8 11:05
 */
@Slf4j
public class FileUtil {

    /**
     * 压缩图片
     *
     * @param sourcePath 原图片地址
     * @param targetPath 目标图片地址
     * @param limitSize  指定图片大小,单位kb
     * @return
     */
    public static boolean compressImg(String sourcePath, String targetPath, long limitSize) {

        File sourceFile = new File(sourcePath);
        try(FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(new File(targetPath))) {
            long fileSize = sourceFile.length() / 1024;
            log.info("===============原图片{}大小为{}kb===============", sourcePath, fileSize);
            if (fileSize <= limitSize) {
                return true;
            }

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            return compressImgByThumbnails(targetPath, limitSize);
        } catch (Exception e) {
            log.error("===============图片{}压缩异常===============", targetPath, e);
            return false;
        }
    }

    /**
     * 通过Thumbnails压缩图片
     *
     * @param targetPath
     * @param limitSize
     * @return
     */
    public static boolean compressImgByThumbnails(String targetPath, long limitSize) {
        try {
            File targetFile = new File(targetPath);
            long fileSize = targetFile.length() / 1024;
            if (fileSize <= limitSize) {
                return true;
            }
            //计算宽高
            log.debug("===============图片{}压缩开始，大小为{}，限制为{}kb===============", targetFile, fileSize, limitSize);
            double outputQuality = 0.5;
            BufferedImage bim = ImageIO.read(targetFile);
            int imgWidth = bim.getWidth();
            int imgHeight = bim.getHeight();
            int desWidth = new BigDecimal(imgWidth).multiply(new BigDecimal(outputQuality)).intValue();
            int desHeight = new BigDecimal(imgHeight).multiply(new BigDecimal(outputQuality)).intValue();
            Thumbnails.of(targetPath).size(desWidth, desHeight).outputQuality(outputQuality).toFile(targetPath);
            File newFile = new File(targetPath);
            log.debug("===============图片{}压缩完成，大小为{}，限制为{}kb===============", targetFile, newFile.length() / 1024, limitSize);
        } catch (Exception e) {
            log.error("===============图片{}压缩异常===============", targetPath, e);
            return false;
        }
        //如果不满足要求,递归直至满足小于1M的要求
        return compressImgByThumbnails(targetPath, limitSize);
    }

}