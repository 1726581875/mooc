package cn.edu.lingnan.core.controller;

import cn.edu.lingnan.core.entity.MoocLogo;
import cn.edu.lingnan.core.service.LogoService;
import cn.edu.lingnan.core.util.IPUtil;
import cn.edu.lingnan.core.vo.LogoVO;
import cn.edu.lingnan.mooc.common.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaomingzhang
 * @date 2020/12/1
 * 自定义logo功能
 */
@Slf4j
@RestController
@RequestMapping("/admin/logo")
public class LogoController{

    /**
     * logo图片最大值，不超过5M
     */
    private static final long LOGO_MAX_SIZE = 5242880L;
    /**
     * 系统名称最大长度不能超过36个字符
     */
    private static final int SYSTEM_NAME_LENGTH_MAX = 36;

    @Autowired
    private LogoService logoService;


    /**
     * 上传logo图片
     *
     * @param logoImage
     * @param type
     * @return
     */
    @PostMapping("/upload")
    public RespResult uploadLogo(@RequestParam(value = "logoImage") MultipartFile logoImage, @RequestParam("type") Integer type) {

        if (null == logoImage || logoImage.isEmpty()) {
            return RespResult.fail("logo图片不能为空");
        }
        // 判断大小
        if (logoImage.getSize() > LOGO_MAX_SIZE) {
            return RespResult.fail("图片大小不能超过5M");
        }
        return logoService.uploadLogoImage(logoImage, type);
    }


    /**
     * 更新logo信息
     *
     * @param logo
     * @param request
     * @return
     */
    @PostMapping("update")
    public RespResult saveLogo(@RequestBody MoocLogo logo, HttpServletRequest request) {
        // 校验特殊字符
        if (!StringUtils.isEmpty(logo.getSystemName())) {
            String regEx = "[`~!@#%^&*()+={}':;',\\[\\].<>?~！@#￥%……&*（）——+{}【】‘；：”“’。，\\-_、？]";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(logo.getSystemName());
            if (matcher.find()) {
                return RespResult.parameterError("您输入的系统名称含有非法字符,请查证");
            }
        }
        // 校验系统名长度
        if (!StringUtils.isEmpty(logo.getSystemName())) {
            if (logo.getSystemName().length() > SYSTEM_NAME_LENGTH_MAX) {
                return RespResult.parameterError("系统名称不能大于36个字符");
            }
        }

        logoService.updateOrInsert(logo, IPUtil.getIpAddr(request));
        return RespResult.success();
    }

    /**
     * 获取全部logo信息显示
     *
     * @return
     */
    @GetMapping("/get")
    public RespResult getLogo() {
        return RespResult.success(logoService.findLogo());
    }

    /**
     * 登录页需要显示的logo信息获取
     *
     * @return
     */
    @GetMapping("/loginLogo")
    public RespResult getLoginLogo() {
        LogoVO logo = logoService.findLogo();
        if (null != logo) {
            Map<String, String> respMap = new HashMap<>(3);
            respMap.put("loginLogoPath", logo.getLoginLogoPath());
            respMap.put("faviconPath", logo.getFaviconPath());
            respMap.put("systemName", logo.getSystemName());
            return RespResult.success(respMap);
        }

        return RespResult.success();
    }


}
