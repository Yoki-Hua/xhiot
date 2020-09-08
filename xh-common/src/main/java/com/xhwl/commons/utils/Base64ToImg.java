package com.xhwl.commons.utils;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Base64ToImg {
    public static boolean Base64ToImage(String imgStr, String imgFilePath) {


// 图像数据为空
        if (StringUtils.isEmpty(imgStr)) {
            return false;
        }

        BASE64Decoder decoder = new BASE64Decoder();
        try {
// Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
//            ImageIO.write(b,"jpg",new File(imgFilePath));
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();

            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
