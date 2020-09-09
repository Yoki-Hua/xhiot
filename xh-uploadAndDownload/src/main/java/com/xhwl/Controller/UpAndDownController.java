package com.xhwl.Controller;

import com.xhwl.Service.UpAndDownService;
import com.xhwl.pojo.PersonBodyDto;
import com.xhwl.pojo.PersonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: coll man
 * @create: 2020-09-02 15:59
 */
@RestController
@RequestMapping("/xhwl")
public class UpAndDownController {
    @Autowired
    private UpAndDownService upAndDownService;

    @RequestMapping("/uploadExcel")
    public ResponseEntity<Map> uploadExcel(@RequestParam("file") MultipartFile multipartFile){

        Map map = upAndDownService.uploadExcel(multipartFile);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @RequestMapping("/downExcel")
    public String downExcel(@RequestBody PersonBodyDto personBodyDto, HttpServletResponse response){

        String result = upAndDownService.downExcel(personBodyDto, response);

        return result;
    }
}
