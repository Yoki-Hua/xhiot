package com.xhwl.Service;

import com.xhwl.pojo.PersonBodyDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: coll man
 * @create: 2020-09-02 16:13
 */
public interface UpAndDownService {

    /**
     * 上传
     * @return
     */
    public Map uploadExcel(MultipartFile multipartFile);


    /**
     * 下载
     * @param personBodyDto
     * @param response
     */
    public String downExcel(PersonBodyDto personBodyDto ,HttpServletResponse response);
}
