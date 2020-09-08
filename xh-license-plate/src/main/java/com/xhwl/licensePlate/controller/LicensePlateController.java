package com.xhwl.licensePlate.controller;

 import com.xhwl.licensePlate.pojo.ImgPath;
import com.xhwl.licensePlate.service.LicensePlateService;
 import io.swagger.annotations.ApiOperation;
 import io.swagger.annotations.ApiParam;
 import io.swagger.annotations.ApiResponse;
 import io.swagger.annotations.ApiResponses;
 import lombok.extern.log4j.Log4j;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * 车牌识别controller
 */
@RestController
@RequestMapping("xhwl")
@Slf4j
public class LicensePlateController {

    @Autowired
    private LicensePlateService licensePlateService;

    /**
     * @param imgPath 参数，图片url
     * @return
     */
    @ApiOperation(value = "车牌识别接口")
    @ApiResponses({
            @ApiResponse(code = 200, message="成功"),
            @ApiResponse(code = 400,message = "失败，参数错误")
    })
    @PostMapping("recognition")
    public ResponseEntity<String> licensePlateRecognition(@ApiParam(value = "图片地址",example = "https://xxxx.com/xxx.jpg") @RequestBody(required = true) ImgPath imgPath) {

        String result = licensePlateService.getLicensePlateRecognitionByToken(imgPath.imgPath);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
