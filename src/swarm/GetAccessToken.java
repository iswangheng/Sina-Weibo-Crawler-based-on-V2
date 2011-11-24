package swarm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import weibo4j.model.MySSLSocketFactory;

public class GetAccessToken
{
	public GetAccessToken()
	{
		setClient_ID("124165210");
		setClient_SECRET("7d2cea2c268180d9d84bbd9378d7ef6f");
		setUserId("cnjswangheng66@yahoo.com.cn");
		setPasswd("swarmhere");
	}
	public GetAccessToken(String clientId, String clientSecret, String userId, String passwd){
		setClient_ID(clientId);
		setClient_SECRET(clientSecret);
		setUserId(userId);
		setPasswd(passwd);
	}
	
	public void setToken(){
		Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		String result= null;
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(
		"https://api.t.sina.com.cn/oauth2/authorize");
		postMethod.addParameter("client_id", getClient_ID()); //appkey
		postMethod.addParameter(
				"redirect_uri",
		"http://www.baidu.com");      //oauth2 回调地址   
		postMethod.addParameter("response_type", "code");
		postMethod.addParameter("action", "submit");
		postMethod.addParameter("userId", getUserId());    //微博帐号
		postMethod.addParameter("passwd", getPasswd());    //帐号密码
		try {
			client.executeMethod(postMethod);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		String url=postMethod.getResponseHeader("location").getValue();
		String params=url.substring(url.lastIndexOf("?")+1);
		Map<String ,String> paramsMap= new HashMap<String, String>();
		for(String s:params.split("&")){
			String[] t=s.split("=");
			paramsMap.put(t[0],t[1]);
		}
		String code=paramsMap.get("code");
		//System.out.println("code: "+code);
		PostMethod tokenMethod=new PostMethod("https://api.t.sina.com.cn/oauth2/access_token");
		tokenMethod.addParameter("client_id", getClient_ID());       //appkey
		tokenMethod.addParameter("client_secret", getClient_SECRET());   //appsecret
		tokenMethod.addParameter("grant_type","authorization_code");
		tokenMethod.addParameter("code",code);           //上一步骤拿到的code
		tokenMethod.addParameter("redirect_uri","http://www.baidu.com");   //回调地址
		try {
			client.executeMethod(tokenMethod);
			result=Thread.currentThread().getName()+"--->"+tokenMethod.getResponseBodyAsString();	
			result = tokenMethod.getResponseBodyAsString();
			System.out.println(result);
			
			accessToken = parseResult(result);

			System.out.println(accessToken);
			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public String getToken()
	{
		return accessToken;
	}
	
	public String parseResult(String result)
	{
		String token = null;
		int beginIndex = 0;
		int endIndex = 1;
		
		if(result != null)
		{
			beginIndex = result.indexOf(":")+2;
			endIndex = result.indexOf(",")-1;
		}
		token = result.substring(beginIndex, endIndex);
		
		return token;
	}
	
	
	public String getClient_ID() {
		return client_ID;
	}

	public void setClient_ID(String client_ID) {
		this.client_ID = client_ID;
	}

	public String getClient_SECRET() {
		return client_SECRET;
	}

	public void setClient_SECRET(String client_SECRET) {
		this.client_SECRET = client_SECRET;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	private String accessToken = "";
	private String client_ID = "";
	private String client_SECRET = "";
	private String userId = "";
	private String passwd = "";
	
	public static void main(String[] args)
	{
		GetAccessToken demo = new GetAccessToken();
		demo.setToken();
	}
	
}

