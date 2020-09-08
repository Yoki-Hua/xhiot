package com.xhwl.Service.impl;
import com.xhwl.Service.UpAndDownService;
import com.xhwl.POIutils.POIUtils;
import com.xhwl.commons.exceptions.XhException;
import com.xhwl.pojo.PersonBodyDto;
import com.xhwl.pojo.PersonInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import org.springframework.web.client.RestTemplate;
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
}
