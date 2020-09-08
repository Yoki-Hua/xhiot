package com.xhwl.licensePlate.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xhwl.commons.enumPackage.ExceptionEnum;
import com.xhwl.commons.exceptions.XhException;
import com.xhwl.commons.utils.Base64ToImg;
import com.xhwl.licensePlate.huawei.ais.orc.HWOcrClientToken;
import com.xhwl.licensePlate.service.LicensePlateService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class licensePlateServiceImpl implements LicensePlateService {
    //华为云账号
    @Value("${huaweiCloud.userName}")
    private String username;
    //华为云密码
    @Value("${huaweiCloud.passWord}")
    private String password;
    //If the current user is not an IAM user, the domainName is the same as the userName.
    @Value("${huaweiCloud.domainName}")
    private String domainName;
    @Override
    public String getLicensePlateRecognitionByToken(String imgPath) {

        /**
         *  Token demo code
         * */
        String regionName="cn-north-4";
        String httpUri = "/v1.0/ocr/license-plate";
        //判断路径是不是base64
        if (imgPath.indexOf("http") == -1 || imgPath.indexOf("https") == -1) {
            //云上绝对路径
//            String imgFilePath = "/usr/local/tomcat/webapps/static/images/" + UUID.randomUUID() + ".jpg";
           //本地绝对路径
            String imgFilePath = "I:\\WorkSpace\\xhiot\\xh-license-plate\\src\\main\\resources\\static\\" + UUID.randomUUID() + ".jpg";

            Base64ToImg.Base64ToImage(imgPath, imgFilePath);
            String imgPathT = imgFilePath; //File path or URL of the image to be recognized.
            return getLicensePlate(regionName, httpUri, imgPathT);
        } else {
            return getLicensePlate(regionName, httpUri, imgPath);

        }



    }

    private String getLicensePlate(String regionName, String httpUri, String imgFilePath) {


        // Set params except image
        String sideKey = "side";
        String sideValue = "front";
        JSONObject params = new JSONObject();
//        params.put(sideKey, sideValue);
        String result = "";
        try {
            HWOcrClientToken ocrClient = new HWOcrClientToken(domainName, username, password, regionName);
            HttpResponse response = ocrClient.RequestOcrServiceBase64(httpUri, imgFilePath, params);
            result = IOUtils.toString(response.getEntity().getContent(), "utf-8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new XhException(ExceptionEnum.PARAMS_ERROR);
        }
    }
}
