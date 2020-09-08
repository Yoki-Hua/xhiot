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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.sdk.util.HttpClientUtils;


public class HWOcrClientToken {
	private  String domainName;  //domain name for the OCR user. If not IAM user, it's the same as username
	private  String username;    //username of the user
	private  String password;      //password of the user
	private  String regionName;  //region for the OCR service(cn-north-1,cn-south-1 etc.)
	private  String token;       //A serial of character returned by iam server for authentication
	private  String httpEndPoint; //Http endpoint for the service
	private static final Integer RETRY_MAX_TIMES = 3; // The max times for get token
	private static final long POLLING_INTERVAL = 2000L; // The time for next request to get token


	static {
		HttpClientUtils.DEFAULT_CONNECTION_TIMEOUT = 15000;
		HttpClientUtils.DEFAULT_CONNECTION_REQUEST_TIMEOUT = 15000;
		HttpClientUtils.DEFAULT_SOCKET_TIMEOUT = 15000;
	}

	/**
	 *
	 * @param domain  domainName for the OCR user. If not IAM user, it's the same as username
	 * @param usrname  username of the user
	 * @param password  password of the user
	 * @param region   region for ocr service(cn-north-1,cn-south-1 etc.)
	 * @param endpoint http endpoint for the url
	 */
	public HWOcrClientToken(String domain,String uName,String psWord,String regName) {
		if (domain== null|domain == ""|uName == null| uName == ""| psWord == null| psWord == ""| regName == null|
				regName == "")
			throw new IllegalArgumentException("the parameter for HWOcrClientToken's Constructor cannot be empty");
		domainName=domain;
		username=uName;
		password=psWord;
		regionName=regName;
		token="";
		httpEndPoint="ocr." + regName +".myhuaweicloud.com";

	}
	/**
	 * Construct a token request object for accessing the service using token authentication.
	 * For details, see the following document:
	 * https://support.huaweicloud.com/en-us/api-ocr/ocr_03_0005.html
	 *
	 * @return Construct the JSON object of the access.
	 */
	private  String RequestBody() {
		JSONObject auth = new JSONObject();

		JSONObject identity = new JSONObject();

		JSONArray methods = new JSONArray();
		methods.add("password");
		identity.put("methods", methods);

		JSONObject passwordObj = new JSONObject();

		JSONObject user = new JSONObject();
		user.put("name", username);
		user.put("password", password);

		JSONObject domainObj = new JSONObject();
		domainObj.put("name", domainName);
		user.put("domain", domainObj);

		passwordObj.put("user", user);

		identity.put("password", passwordObj);

		JSONObject scope = new JSONObject();

		JSONObject scopeProject = new JSONObject();
		scopeProject.put("name", regionName);

		scope.put("project", scopeProject);

		auth.put("identity", identity);
		auth.put("scope", scope);

		JSONObject params = new JSONObject();
		params.put("auth", auth);
		return params.toJSONString();
	}

	private void RefreshToken() throws InterruptedException, IOException, URISyntaxException {
		System.out.println("Token expired, refresh.");
		token = "";
		GetToken();
	}

	/**
	 * Obtain the token parameter.
	 * Note that this function aims to extract the token from the header in the HTTP request response body.
	 * The parameter name is X-Subject-Token.
	 *
	 * @throws URISyntaxException
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	private  void GetToken()
			throws URISyntaxException, UnsupportedOperationException, IOException, InterruptedException {
		    //if token is not empty return token directly
		    if(0 != token.length())
		    	return ;

		    // the times to get token
			Integer retryTimes = 0;

			// 1.set the Entity for the request get Token
			String requestBody = RequestBody();
			int iamSuccessResponseCode=201;
			String url = String.format("https://iam.%s.myhuaweicloud.com/v3/auth/tokens",regionName);
			Header[] headers = new Header[] { new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
			StringEntity stringEntity = new StringEntity(requestBody,"utf-8");

			while (true) {
				// 2.send the POST request to get Token value
				HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
				// 3.Detemine whether to refresh Token or not according to the request state code,
				// if the times to get Token reaches three, set the Token null.
				if (iamSuccessResponseCode != response.getStatusLine().getStatusCode()) {
					if (retryTimes < RETRY_MAX_TIMES) {
						retryTimes++;
						String content = IOUtils.toString(response.getEntity().getContent());
						System.out.println(content);
						token = "";
						Thread.sleep(POLLING_INTERVAL);    //sleep 2s
						continue;
					}else {
						token = "";
						System.out.println("Failed to obtain the token.");
						return;
					}
				} else {
						System.out.println("Token obtained successfully.");
						Header[] xst = response.getHeaders("X-Subject-Token");
						token = xst[0].getValue();
						return;
				}
			}
	}
	/**
     * Use the Base64-encoded file and access VAT Invoice OCR using token authentication.
     * @param endPoint httpendpoint for the requested service
     * @param url url for the requested service
     * @param filepath Full File path
	 * @throws URISyntaxException
	 * @throws UnsupportedOperationException
     * @throws IOException
     */
    public HttpResponse RequestOcrServiceBase64(String uri, String imgPath, JSONObject params) throws UnsupportedOperationException, URISyntaxException, IOException, InterruptedException {
		int iamTokenAbnormalCode = 403;
		int iamTokenExpiredCode = 401;
		String iamTokenAbnormalInfo = "The authentication token is abnormal.";
		String iamTokenExpiredInfo = "The token expires.";
		String iamTokenFailed = "Incorrect IAM authentication information: decrypt token fail";

        // 1. Construct the parameters required for accessing VAT Invoice OCR.
    	if(uri == null | uri == "" | imgPath == null | imgPath == "")
    		throw new IllegalArgumentException("The parameter for the requestOcrServiceBase64 constructor cannot be empty.");
		String completeurl = "https://"+httpEndPoint+uri;
		GetToken();

		if (token != "") {
			Header[] headers = new Header[]{new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
			try {
				if (imgPath.indexOf("http://") != -1 || imgPath.indexOf("https://") != -1) {
					params.put("url", imgPath);
				} else {
					byte[] fileData = FileUtils.readFileToByteArray(new File(imgPath));
					String fileBase64Str = Base64.encodeBase64String(fileData);
					params.put("image", fileBase64Str);
				}

				// 2. Pass the parameters of VAT Invoice OCR, invoke the service using the POST method, and parse and output the recognition result.
				StringEntity stringEntity = new StringEntity(params.toJSONString(), "utf-8");
				HttpResponse response = HttpClientUtils.post(completeurl, headers, stringEntity);

				if (iamTokenAbnormalCode == response.getStatusLine().getStatusCode() ||
					iamTokenExpiredCode == response.getStatusLine().getStatusCode()) {
					// token expired,refresh token
					String content = IOUtils.toString(response.getEntity().getContent());
					if (content.contains(iamTokenAbnormalInfo) ||
						content.contains(iamTokenExpiredInfo) ||
						content.contains(iamTokenFailed)) {
						RefreshToken();
						return RequestOcrServiceBase64(uri, imgPath, params);
					} else {
						System.out.println(content);
						return null;
					}
				}
				return response;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			throw new IllegalArgumentException("Token cannot be null!");
		}
	}

}
