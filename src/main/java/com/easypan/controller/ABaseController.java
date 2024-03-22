package com.easypan.controller;

import com.easypan.config.WebConfig;
import com.easypan.entity.po.FileInfo;
import com.easypan.myEnum.FileCategoryEnum;
import com.easypan.myEnum.ResponseCode;
import com.easypan.entity.vo.ResponseVo;
import com.easypan.exception.BusinessException;
import com.easypan.service.FileInfoService;
import com.easypan.service.impl.FileInfoServiceImpl;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ABaseController {
    protected static final String STATUS_SUCCESS = "success";
    protected static final String STATUS_ERROR = "error";

    @Resource
    private WebConfig webConfig;

    @Resource
    private FileInfoService fileInfoService;

    protected <T> ResponseVo getSuccessResponseVo(T t) {
        ResponseVo<T> responseVo = new ResponseVo<>();
        responseVo.setStatus(STATUS_SUCCESS);
        responseVo.setCode(ResponseCode.CODE_200.getCode());
        responseVo.setInfo(ResponseCode.CODE_200.getInfo());
        responseVo.setData(t);
        return responseVo;
    }

    protected String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    protected void readFile(HttpServletResponse response, String filePath) {
        if (StringUtils.isEmpty(filePath) || StringUtils.contains("../", filePath) || StringUtils.contains("..\\", filePath)) {
            return;
        }
        OutputStream out = null;
        FileInputStream in = null;
        try {
            File file = new File(filePath);
            // d:./dwdwdff.jpg
            if (!file.exists()) {
                String substring = filePath.substring(0, filePath.lastIndexOf("/") + 1) + "default.jpg";
                file = new File(substring);
            }
            in = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            out = response.getOutputStream();
            int len = 0;
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void getImage(HttpServletResponse response, String imageFolder, String imageName) {
        if (StringUtils.isEmpty(imageFolder) || StringUtils.isEmpty(imageName)) {
            return;
        }
        if (imageFolder.contains("..") || imageName.contains("..")) {
            return;
        }
        String imageSuffix = imageName.substring(imageFolder.lastIndexOf("."), imageName.length());
        String filePath = webConfig.getFolder() + "file/" + imageFolder + "/" + imageName;
        imageSuffix = imageFolder.replace(".", "");
        String contentType = "image/" + imageSuffix;
        response.setContentType(contentType);
        response.setHeader("Cache-Control", "max-age=2592000");
        readFile(response, filePath);
    }

    protected void getFile(HttpServletResponse response, String userId, String fileId) {
        String filePath = null;
        if (fileId.endsWith(".ts")) {
            String[] tsArray = fileId.split("_");
            String realFileId = tsArray[0];
            FileInfo fileInfo = fileInfoService.getFileInfoByFileIdAndUserId(realFileId, userId);
            if (fileInfo == null) {
                return;
            }
            String filePath1 = fileInfo.getFilePath();
            String noSuffix = filePath1.substring(0, filePath1.lastIndexOf("."));
            filePath = webConfig.getFolder() + "file/" + noSuffix + "/" + "index.m3u8";

        } else {
            FileInfo fileInfo = fileInfoService.getFileInfoByFileIdAndUserId(fileId, userId);

            if (fileInfo == null) {
                return;
            }
            if (FileCategoryEnum.VIDEO.getCategory().equals(fileInfo.getFileCategory())) {
                String filePath1 = fileInfo.getFilePath();
                String noSuffix = filePath1.substring(0, filePath1.lastIndexOf("."));
                filePath = webConfig.getFolder() + "file/" + noSuffix + "/" + "index.m3u8";
            }else {
                filePath = webConfig.getFolder() + "file/" + fileInfo.getFilePath();
            }
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
        }
        readFile(response, filePath);
    }
}
