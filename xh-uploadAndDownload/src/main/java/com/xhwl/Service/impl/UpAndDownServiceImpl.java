package com.xhwl.Service.impl;
import com.xhwl.Service.UpAndDownService;
import com.xhwl.POIutils.POIUtils;
import com.xhwl.commons.exceptions.XhException;
import com.xhwl.pojo.PersonBodyDto;
import com.xhwl.pojo.PersonInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import static com.xhwl.commons.enumPackage.ExceptionEnum.*;


/**
 * @author: coll man
 * @create: 2020-09-02 16:14
 */
@Service
public class UpAndDownServiceImpl implements UpAndDownService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${huawei.tokenUrl}")
    private String tokenUrl;
    @Value("${huawei.excelUrl}")
    private String excelUrl;
    @Value("${huawei.filePath}")
    private String filePath;

    /**
     * 读取文件
     * @param multipartFile
     * @return
     */
    @Override
    public Map uploadExcel(MultipartFile multipartFile) {
      PersonBodyDto personInfos = new PersonBodyDto();
        try {
            //检查
            POIUtils.checkFile(multipartFile);
            //读取Excel数据
            List<String[]> rows = POIUtils.readExcel(multipartFile);
            int size = rows.size();
           //创建PersonInfo对象
            PersonInfo[] personInfoArr = new PersonInfo[size];
            //读取表格里的数据，写入到personInfo类中
            for (int i = 0; i < rows.size(); i++) {
                //创建对象
                PersonInfo personInfo = new PersonInfo();
                if(StringUtils.isEmpty(rows.get(i)[0])){
                    throw  new XhException(NAME_NOT_ISNULL);
                }
                personInfo.setName(rows.get(i)[0]);
                personInfo.setContact(rows.get(i)[1]);
                personInfo.setIdentity(rows.get(i)[2]);
                personInfo.setIdNumber(rows.get(i)[3]);
                personInfo.setProject(rows.get(i)[4]);
                personInfo.setHomeAddress(rows.get(i)[5]);
                personInfo.setLoginPerson(rows.get(i)[6]);
                personInfo.setEmergencyContact(rows.get(i)[7]);
                personInfo.setIsBlack(rows.get(i)[8]);
                //将对象添加到personInfos数组中
                personInfoArr[i] = personInfo;
            }
            //把数组存到PersonBodyDto中
            personInfos.setPersonInfos(personInfoArr);
            //创建获取token的请求体
            MultiValueMap<String, String> getTokenHeader = new LinkedMultiValueMap<>();
            getTokenHeader.add("Content-Type","application/json");
            HttpEntity<Object> tokenHttpEntity = new HttpEntity<>(getTokenHeader);
            //获取响应体
            ResponseEntity<Map> responseEntity = restTemplate.exchange(tokenUrl, HttpMethod.POST,tokenHttpEntity, Map.class);
            //获取token请求返回的body数据
           Map responseEntityBody = responseEntity.getBody();
            String access_token = null;
            try {
                access_token = (String) responseEntityBody.get("access_token");
            } catch (Exception e) {
                throw new XhException(GET_ACCESS_TOKEN_FAIL);
            }
            //设置调用接口的请求头参数
            MultiValueMap<String, String> hashMapExcelHeader = new LinkedMultiValueMap<>();
//            把token保存到header中
            hashMapExcelHeader.add("access-token",access_token);
//            把personInfos保存到请求Entity中
            HttpEntity httpEntityExcel = new HttpEntity<>(personInfos, hashMapExcelHeader);
//             获取返回的响应数据
            ResponseEntity<Map> responseEntityExcel = restTemplate.exchange(excelUrl, HttpMethod.POST, httpEntityExcel, Map.class);
//           获取响应数据中的body
            Map body = responseEntityExcel.getBody();
            return body;

        } catch (Exception e) {
            throw new XhException(DATA_WRITING_FAILED);
        }

    }

    /**
     * 下载文件
     * @param personBodyDto
     * @param response
     */
    @Override
    public String downExcel(PersonBodyDto personBodyDto, HttpServletResponse response) {
        String path = filePath;
        File excelFile = new File(path + "人员信息模板.xlsx");
        File newExcelFile = POIUtils.createNewFile(excelFile);

        PersonInfo[] persons = personBodyDto.getPersonInfos();
        //准备写入数据
        InputStream is = null;
        OutputStream out = null;
        Workbook workbook = null;
        try {
            is = new FileInputStream(newExcelFile);
            workbook = new XSSFWorkbook(is);

            //读取模板内所有sheet内容
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < persons.length; i++) {

                Row row = sheet.getRow(i + 1);

                if(row == null){
                    row = sheet.createRow(i + 1);
                    row.createCell(0).setCellValue(persons[i].getName());
                    row.createCell(1).setCellValue(persons[i].getContact());
                    row.createCell(2).setCellValue(persons[i].getIdentity());
                    row.createCell(3).setCellValue(persons[i].getIdNumber());
                    row.createCell(4).setCellValue(persons[i].getProject());
                    row.createCell(5).setCellValue(persons[i].getHomeAddress());
                    row.createCell(6).setCellValue(persons[i].getLoginPerson());
                    row.createCell(7).setCellValue(persons[i].getEmergencyContact());
                    row.createCell(8).setCellValue(persons[i].getIsBlack());
                }else {
                    row.getCell(0).setCellValue(persons[i].getName());
                    row.getCell(1).setCellValue(persons[i].getContact());
                    row.getCell(2).setCellValue(persons[i].getIdentity());
                    row.getCell(3).setCellValue(persons[i].getIdNumber());
                    row.getCell(4).setCellValue(persons[i].getProject());
                    row.getCell(5).setCellValue(persons[i].getHomeAddress());
                    row.getCell(6).setCellValue(persons[i].getLoginPerson());
                    row.getCell(7).setCellValue(persons[i].getEmergencyContact());
                    row.getCell(8).setCellValue(persons[i].getIsBlack());
                }
            }
            out = new FileOutputStream(newExcelFile);
            workbook.write(out);

//            String fileName = newExcelFile.getName();
//            OutputStream output = response.getOutputStream();
//            response.reset();
//            fileName = URLEncoder.encode("人员信息.xslx", "UTF-8");
//            response.setContentType("applicationnd/msexcel");
//            response.setCharacterEncoding("UTF-8");
//            response.setHeader("Content-Disposition", "attachment; filename=\"" +
//                    fileName + "\"");//下载文件
//            workbook.write(output);//写入到Excel模板文件中
//            output.close();//关闭
            //POIUtils.deleteFile(newExcelFile);
            return "导出成功:"+newExcelFile.getName();
        } catch (Exception e) {
            throw new XhException(DATA_DOWNLOAD_FAILED);
        }finally {
            try {
                workbook.close();
                out.close();
                is.close();
            } catch (IOException e) {
                throw new XhException(DATA_DOWNLOAD_FAILED);
            }
        }

    }

}
