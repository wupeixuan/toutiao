package com.wpx.service;


import com.alibaba.fastjson.JSONObject;
import com.wpx.aspect.LogAspect;
import com.wpx.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


/**
 * 七牛云服务
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    Configuration cfg = new Configuration(Zone.zone2());
    String ACCESS_KEY = "Vydj6ZFXt0B7bt56UGLlURmM38pjQmezfxM74r-n";
    String SECRET_KEY = "-J4GR3HdnJcWIql4AG5KjBuiYWLNWmEYDXC2BgU0";
    String bucket = "toutiao";
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    UploadManager uploadManager = new UploadManager(cfg);

    private static String QINIU_IMAGE_DOMAIN = "http://opfm5z11e.bkt.clouddn.com/";

    public String getUploadToken() {
        return auth.uploadToken(bucket);
    }

    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if (!ToutiaoUtil.isFileAllowed(fileExt)) {
                return null;
            }
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            Response res = uploadManager.put(file.getBytes(), fileName, getUploadToken());

            if (res.isOK() && res.isJson()) {
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                logger.error("七牛异常:" + res.bodyString());
                return null;
            }

        } catch (QiniuException ex) {
            logger.error("七牛异常:" + ex.getMessage());
            return null;
        }
    }


}
