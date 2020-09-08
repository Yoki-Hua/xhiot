/**
* Copyright 2018 Huawei Technologies Co.,Ltd.
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not use
* this file except in compliance with the License.  You may obtain a copy of the
* License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software distributed
* under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
* CONDITIONS OF ANY KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations under the License.
**/
package com.xhwl.licensePlate.huawei.ais.orc;

import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.common.AuthInfo;
import com.huawei.ais.common.ProxyHostInfo;
import com.huawei.ais.sdk.AisAccess;
import com.huawei.ais.sdk.AisAccessWithProxy;
import com.huawei.ais.sdk.util.HttpClientUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;

import java.io.File;
import java.io.IOException;


public class HWOcrClientAKSK {
	private AuthInfo HEC_AUTH;       //Authorization Information
	private AisAccess HttpService;   //access to ocr service on cloud
	private ProxyHostInfo PROXYINFO; //proxy host info (optional)

	static {
		HttpClientUtils.DEFAULT_CONNECTION_TIMEOUT = 15000;
		HttpClientUtils.DEFAULT_CONNECTION_REQUEST_TIMEOUT = 15000;
		HttpClientUtils.DEFAULT_SOCKET_TIMEOUT = 15000;
	}

	/**
	 * Constructor for OcrClientAKSK without proxy
	 * @param Endpoint HTTPEndPoint for OCR Service
	 * @param Region The region info for OCR Service
	 * @param YourAK The Access Key from Authentication
	 * @param YourSK The Secret Key from Authentication
	 * */
	public HWOcrClientAKSK(String Region,String YourAK,String YourSK) {
		if (Region == null| Region == ""| YourAK == null| YourAK == ""| YourSK == null|
				YourSK == "")
					throw new IllegalArgumentException("the parameter for HWOcrClientAKSK's Constructor cannot be empty");
		String endPoint = "https://ocr." + Region + ".myhuaweicloud.com";
		HEC_AUTH= new AuthInfo(endPoint,Region,YourAK,YourSK);
		HttpService=new AisAccess(HEC_AUTH);
	}
	/**
	 * Constructor for OcrClientAKSK with proxy
	 * @param Endpoint HTTPEndPoint for OCR Service
	 * @param Region The region info for OCR Service
	 * @param YourAK The Access Key from Authentication
	 * @param YourSK The Secret Key from Authentication
	 * @param ProxyHost The URL for the proxy server
	 * @param ProxyPort The Port number for the proxy service
	 * @param ProxyUsrName The Loggin user for the proxy server if authentication is required
	 * @param ProxyPwd  The Loggin pwd for the proxy server if authentication is required
	 * */
	public HWOcrClientAKSK(String Endpoint,String Region,String YourAK,String YourSK,String ProxyHost,int ProxyPort,String ProxyUsrName,String ProxyPwd) {
		if (Endpoint== null|Endpoint == ""|Region == null| Region == ""| YourAK == null| YourAK == ""| YourSK == null|
				YourSK == ""|ProxyHost == null | ProxyHost == "" | ProxyUsrName == null |ProxyUsrName == ""| ProxyPwd == null | ProxyPwd == "")
					throw new IllegalArgumentException("the parameter for HWOcrClientAKSK's Constructor cannot be empty");
		HEC_AUTH= new AuthInfo(Endpoint,Region,YourAK,YourSK);
		PROXYINFO= new ProxyHostInfo(ProxyHost,ProxyPort,ProxyUsrName,ProxyPwd);
		HttpService=new AisAccessWithProxy(HEC_AUTH,PROXYINFO);
	}
	/*
	 * Release the access service when the object is collected
	 * */
	protected void finalize(){
		HttpService.close();
	}
	/**
	 * Call the OCR API with a local picture file
	 * @param uri the uri for the http request to be called
	 * @param filepath the path for the picture file to be recognized
	 * */
	public HttpResponse RequestOcrServiceBase64(String uri,String imgPath, JSONObject params) throws IOException {
    	if(uri == null | uri == "" | imgPath == null | imgPath == "")
    		throw new IllegalArgumentException("the parameter for requestOcrServiceBase64 cannot be empty");
		try {
			if (imgPath.indexOf("http://") != -1 || imgPath.indexOf("https://") != -1) {
				params.put("url", imgPath);
			} else {
				byte[] fileData = FileUtils.readFileToByteArray(new File(imgPath));
				String fileBase64Str = Base64.encodeBase64String(fileData);
				params.put("image", fileBase64Str);
			}

			// Pass the parameters in JSON objects and invoke the service using POST.
			StringEntity stringEntity = new StringEntity(params.toJSONString(), "utf-8");
			HttpResponse response = HttpService.post(uri, stringEntity);
			return response;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
