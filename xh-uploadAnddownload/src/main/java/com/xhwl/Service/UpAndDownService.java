package com.xhwl.Service;

import org.springframework.web.multipart.MultipartFile;

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

}
