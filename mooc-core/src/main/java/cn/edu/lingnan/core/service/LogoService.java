package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.constant.Constant;
import cn.edu.lingnan.core.entity.TbLogo;
import cn.edu.lingnan.core.repository.LogoRepository;
import cn.edu.lingnan.core.util.FileUtil;
import cn.edu.lingnan.core.vo.LogoVO;
import cn.edu.lingnan.mooc.common.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author: xiaomingzhang
 * @time: 2020/12/2
 */
@Service
@Slf4j
public class LogoService{

    /**
     * 固定logo数据id是1
     */
    private static final int LOGO_ID = 1;

    @Autowired
    private LogoRepository logoRepository;
/*    @Autowired
    private AuditLogService auditLogService;*/

    @Value("${mooc.logo.path:D:\\data\\logo}")
    private String logoBasePath;
    /**
     * 登录logo名
     */
    private static final String LOGIN_LOGO_NAME = "loginLogo";
    /**
     * 系统logo名
     */
    private static final String SYSTEM_LOGO_NAME = "systemLogo";
    /**
     * 网页logo名
     */
    private static final String FAVICON = "favicon";
    /**
     *  临时图片后缀
     */
    private static final String TEMP = "Temp";


    /**
     * 压缩大小KB
     */
    private final static long COMPRESSION_SIZE = 10L;

    /**
     * 插入或更新logo信息
     * 1、查询出数据库logo记录
     * 2、拼接操作日志
     * 3、更新logo图片，更新数据库记录
     * 4、插入操作日志
     *
     * @param logo
     * @param ip
     */
    public void updateOrInsert(TbLogo logo, String ip) {

        Optional<TbLogo> optional = logoRepository.findById(LOGO_ID);
        // 操作详情
        StringBuilder describe = new StringBuilder("");
        if (optional.isPresent()) {
            TbLogo updateLogo = optional.get();

            // 记录日志
            describe.append(appendLogMsg(updateLogo, logo));

            // 更新logo信息
            updateLogo.setSystemName(logo.getSystemName());
            updateLogo.setLoginLogoPath(getSaveFilePath(logo.getLoginLogoPath()));
            updateLogo.setSystemLogoPath(getSaveFilePath(logo.getSystemLogoPath()));
            updateLogo.setFaviconPath(getSaveFilePath(logo.getFaviconPath()));
            updateLogo.setUpdateTime(new Date());
            logoRepository.save(updateLogo);

        } else {
            // 插入操作
            this.insertLogo(logo);
            describe.append("设置了自定义logo信息");
        }

        // 插入操作日志
/*        DsfLogEntity dsfLogEntity = AuditLogUtil.initDsfLogEntity(ip, "个性化设置", describe.toString(), null);
        auditLogService.insert(dsfLogEntity);*/


    }


    /**
     * 如果参数包括 “Temp” 说明需要更改数据库logo图片url
     * 1、获取保存到数据库的路径
     * 2、或取图片的全路径
     * 3、删除原来的logo图片，把临时logo更换命名为当前logo
     * （例如： 文件 systemLogoTemp.jpg 改为 systemLogo.jpg）
     *
     * @param filePath logo图片相对路径 + 文件名
     * @return
     */
    private String getSaveFilePath(String filePath) {
        // 如果包含Temp才执行下面操作
        if (filePath.contains(TEMP)) {
            StringBuilder fileNameBuilder = new StringBuilder(filePath);
            // 1、获取保存到数据库的相对路径
            String savePath = fileNameBuilder.replace(filePath.indexOf(TEMP), filePath.indexOf(TEMP) + TEMP.length(), "").toString();
            String oldFileFullName = logoBasePath + File.separator + filePath.substring(Constant.LOGO_MAPPING_PATH.length());
            String newFileFullName = logoBasePath + File.separator + savePath.substring(Constant.LOGO_MAPPING_PATH.length());
            File oldLogo = new File(oldFileFullName);
            if (!oldLogo.exists()) {
                return savePath;
            }
            // 删除之前的logo图片
            File newLogo = new File(newFileFullName);
            if (newLogo.exists()) {
                newLogo.delete();
            }
            // 更改文件名
            boolean renameSuccess = oldLogo.renameTo(newLogo);
            if (!renameSuccess) {
                log.error("更改logo图片名失败");
            }
            return savePath;
        }
        return filePath;
    }


    /**
     * 插入logo信息，id是1
     *
     * @param logo
     * @return
     */
    private TbLogo insertLogo(TbLogo logo) {
        logo.setId(LOGO_ID);
        logo.setCreateTime(new Date());
        logo.setUpdateTime(new Date());
        return logoRepository.save(logo);
    }

    /**
     * 拼接操作日志信息
     *
     * @param oldLogo
     * @param newLogo
     * @return
     */
    private String appendLogMsg(TbLogo oldLogo, TbLogo newLogo) {
        // 记录日志
        StringBuilder describe = new StringBuilder("");
        if (!oldLogo.getSystemLogoPath().equals(newLogo.getSystemLogoPath())) {
            describe.append("更改了系统页面logo图片 ");
        }
        if (!oldLogo.getLoginLogoPath().equals(newLogo.getLoginLogoPath())) {
            describe.append("更改了登录页面logo图片 ");
        }
        if (!oldLogo.getFaviconPath().equals(newLogo.getFaviconPath())) {
            describe.append("更改了系统网站图图标 ");
        }
        if (!oldLogo.getSystemName().equals(newLogo.getSystemName())) {
            describe.append("更改系统名称为 " + newLogo.getSystemName());
        }
        return describe.toString();
    }


    /**
     * 获取logo信息
     * 为了兼容前端显示
     * 1、以相对url形式返回
     * 2、以base64形式返回
     * @return
     */
    public LogoVO findLogo() {
        Optional<TbLogo> optional = logoRepository.findById(LOGO_ID);
        if (optional.isPresent()) {
            TbLogo tbLogo = optional.get();
            LogoVO logoVO = new LogoVO();
            // 图片相对路径形式返回
            logoVO.setSystemName(tbLogo.getSystemName());
            logoVO.setLoginLogoPath(tbLogo.getLoginLogoPath());
            logoVO.setSystemLogoPath(tbLogo.getSystemLogoPath());
            logoVO.setFaviconPath(tbLogo.getFaviconPath());
            return logoVO;
        }

        return null;
    }

    /**
     * 上传logo图片
     * 1、获取图片后缀名
     * 2、获取logo类型，根据类型获取对应名字（名字是固定的）
     * 3、拼接得到文件全路径，保存图片到服务器
     * 4、返回相对路径用于前端显示
     *
     * @param logoImage 图片文件
     * @param type      类型(1、登录页logo 2、系统页logo 3、网站图标)
     * @return
     */
    public RespResult uploadLogoImage(MultipartFile logoImage, Integer type) {

        // 获取后缀名，校验只允许png/jpg类型
        String fileName = logoImage.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            RespResult.fail("请上传jpg/png类型的图片");
        }
        String imageSuffix = fileName.substring(index);
        if (!Constant.IMAGE_SUFFIX_PNG.equals(imageSuffix.toLowerCase())
                && !Constant.IMAGE_SUFFIX_JPG.equals(imageSuffix.toLowerCase())) {
            RespResult.fail("请上传jpg/png类型的图片");
        }
        // 获取临时logo名
        fileName = getTempFileName(type);

        // 源图片全路径(压缩前的图片)
        String sourceFileFullPath = logoBasePath + File.separator + fileName + "SourceImage" + Constant.IMAGE_SUFFIX_JPG;
        // 需要保存的文件全路径
        String fileFullPath = logoBasePath + File.separator + fileName + Constant.IMAGE_SUFFIX_JPG;

        File sourceImage = new File(sourceFileFullPath);
        File saveImageTemp = new File(fileFullPath);
        // 删除源图片
        if (saveImageTemp.exists()) {
            saveImageTemp.delete();
        }
        // 如果所在目录不存在，先创建
        if (!sourceImage.getParentFile().exists()) {
            sourceImage.getParentFile().mkdirs();
        }
        try {
            long imageSize = logoImage.getSize() / 1024;
            // 大于10KB才压缩
            if (imageSize > COMPRESSION_SIZE) {
                // 保存图片到服务器
                logoImage.transferTo(sourceImage);
                // 压缩logo到10KB以下
                FileUtil.compressImg(sourceFileFullPath, fileFullPath, COMPRESSION_SIZE);
            } else {
                logoImage.transferTo(saveImageTemp);
            }
        } catch (IOException e) {
            log.error("logo图片上传失败", e);
            RespResult.fail("logo图片上传失败");
        }
        // 拼接相对路径返回
        String fileRelativePath = Constant.LOGO_MAPPING_PATH + fileName + Constant.IMAGE_SUFFIX_JPG;
        // 构造返回信息
        Map<String, String> uploadResponse = buildUploadResponse(type, fileRelativePath);

        return RespResult.success(uploadResponse);
    }

    /**
     * 构造上传成功返回给前端的对象
     *
     * @param type             类型:1、登录页logo 2、系统logo 3、网页logo
     * @param fileRelativePath 文件全路径
     * @return
     */
    private Map<String, String> buildUploadResponse(Integer type, String fileRelativePath) {
        String logoFullPath = logoBasePath + File.separator + fileRelativePath.substring(Constant.LOGO_MAPPING_PATH.length());
        Map<String, String> map = new HashMap<>(2);
        if (type.equals(1)) {
            map.put("loginLogoPath", fileRelativePath);
        } else if (type.equals(2)) {
            map.put("systemLogoPath", fileRelativePath);
        } else if (type.equals(3)) {
            map.put("faviconPath", fileRelativePath);
        }

        return map;
    }

    /**
     * 根据类型，获取临时logo名
     * @param type 类型(1、登录页logo 2、系统页logo 3、网站图标)
     * @return
     */
    private String getTempFileName(Integer type) {
        String fileName = "";
        if (type.equals(1)) {
            fileName = LOGIN_LOGO_NAME + TEMP;
        } else if (type.equals(2)) {
            fileName = SYSTEM_LOGO_NAME + TEMP;
        } else if (type.equals(3)) {
            fileName = FAVICON + TEMP;
        }
        return fileName;
    }


}
