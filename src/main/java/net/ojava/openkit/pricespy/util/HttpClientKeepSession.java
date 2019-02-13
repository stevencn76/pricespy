package net.ojava.openkit.pricespy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import net.ojava.openkit.util.StrUtil;

public class HttpClientKeepSession {
	private static final Logger LOG = Logger.getLogger(HttpClientKeepSession.class);
	private static final String DEFALUT_ENCODE = "utf-8";
	public  CloseableHttpClient httpClient = null;
	public  HttpClientContext context = null;
	public  CookieStore cookieStore = null;
	public  RequestConfig requestConfig = null;

	public HttpClientKeepSession() {
		context = HttpClientContext.create();
		cookieStore = new BasicCookieStore();
		// 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
		requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
				       .setConnectionRequestTimeout(60000).build();
		// 设置默认跳转以及存储cookie
		httpClient = HttpClientBuilder.create()
				     .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
				     .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)
				     .setDefaultCookieStore(cookieStore).build();
	}

	/**
	 * http get
	 * 
	 * @param url
	 * @return response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String get(String url) throws ClientProtocolException, IOException {
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = httpClient.execute(httpget, context);
		try {
			cookieStore = context.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			for (Cookie cookie : cookies) {
				System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
			}
			
			return copyResponse2Str(response);
		} finally {
			response.close();
		}
	}
	
	public void downloadFile(String url, File file) throws ClientProtocolException, IOException {
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = httpClient.execute(httpget, context);
		copyResponse2File(response, file);
	}

	/**
	 * http post
	 * 
	 * @param url
	 * @param parameters
	 *            form表单
	 * @return response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String post(String url, String parameters)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = toNameValuePairList(parameters);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		CloseableHttpResponse response = httpClient.execute(httpPost, context);
		try {
			cookieStore = context.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			for (Cookie cookie : cookies) {
				System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
			}
			
			return copyResponse2Str(response);
		} finally {
			response.close();
		}
	}

	@SuppressWarnings("unused")
	private List<NameValuePair> toNameValuePairList(String parameters) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String[] paramList = parameters.split("&");
		for (String parm : paramList) {
			int index = -1;
			for (int i = 0; i < parm.length(); i++) {
				index = parm.indexOf("=");
				break;
			}
			String key = parm.substring(0, index);
			String value = parm.substring(++index, parm.length());
			nvps.add(new BasicNameValuePair(key, value));
		}
		System.out.println(nvps.toString());
		return nvps;
	}
	/**
	 * 手动增加cookie
	 * @param name
	 * @param value
	 * @param domain
	 * @param path
	 */
	public void addCookie(String name, String value, String domain, String path) {
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookieStore.addCookie(cookie);
	}
	/**
	 * 把结果console出来
	 * 
	 * @param httpResponse
	 * @throws ParseException
	 * @throws IOException
	 */
	public void printResponse(HttpResponse httpResponse) throws ParseException, IOException {
		// 获取响应消息实体
//		HttpEntity entity = httpResponse.getEntity();
		// 响应状态
		System.out.println("status:" + httpResponse.getStatusLine());
		System.out.println("headers:");
		HeaderIterator iterator = httpResponse.headerIterator();
		while (iterator.hasNext()) {
			System.out.println("\t" + iterator.next());
		}
		
		// 判断响应实体是否为空
//		if (entity != null) {
//			String responseString = EntityUtils.toString(entity);
//			System.out.println("response length:" + responseString.length());
//			System.out.println("response content:" + responseString.replace("\r\n", ""));
//		}
		System.out.println(
				"------------------------------------------------------------------------------------------\r\n");
	}

    /**
     * 将返回的Response转化成String对象
     * @param response 返回的Response
     * @return
     */
    private String copyResponse2Str(CloseableHttpResponse response){
        try {
            int code = response.getStatusLine().getStatusCode();
            //当请求的code返回值不是400的情况
            if((code == HttpStatus.SC_MOVED_TEMPORARILY )
            || (code == HttpStatus.SC_MOVED_PERMANENTLY)
            || (code == HttpStatus.SC_SEE_OTHER)
            || (code == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                return null;
            }else{
                return copyInputStream2Str(response.getEntity().getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void copyResponse2File(CloseableHttpResponse response, File file) {
        try {
            int code = response.getStatusLine().getStatusCode();
            //当请求的code返回值不是400的情况
            if((code == HttpStatus.SC_MOVED_TEMPORARILY )
            || (code == HttpStatus.SC_MOVED_PERMANENTLY)
            || (code == HttpStatus.SC_SEE_OTHER)
            || (code == HttpStatus.SC_TEMPORARY_REDIRECT)) {
            }else{
                copyInputStream2File(response.getEntity().getContent(), file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 将InputStream转化为String类型的数据
     * @param in
     * @return
     */
    private String copyInputStream2Str(InputStream in){
         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,DEFALUT_ENCODE));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            LOG.debug("获取字符串失败");
        }
        return null;
    }
    private String copyInputStream2File(InputStream in, File file){
        try {
           FileOutputStream writer = new FileOutputStream(file);
           byte buf[] = new byte[1024];
           int len = 0;
           
           while((len = in.read(buf)) != -1){
               writer.write(buf, 0, len);
           }
           
           writer.close();
       } catch (Exception e) {
           LOG.debug("获取字符串失败");
       }
       return null;
   }

	
	/**
	 * 把当前cookie从控制台输出出来
	 * 
	 */
	public void printCookies() {
		System.out.println("headers:");
		cookieStore = context.getCookieStore();
		List<Cookie> cookies = cookieStore.getCookies();
		for (Cookie cookie : cookies) {
			System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
		}
	}

	/**
	 * 检查cookie的键值是否包含传参
	 * 
	 * @param key
	 * @return
	 */
	public boolean checkCookie(String key) {
		cookieStore = context.getCookieStore();
		List<Cookie> cookies = cookieStore.getCookies();
		boolean res = false;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(key)) {
				res = true;
				break;
			}
		}
		return res;
	}

	/**
	 * 直接把Response内的Entity内容转换成String
	 * 
	 * @param httpResponse
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public String toString(CloseableHttpResponse httpResponse) throws ParseException, IOException {
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		if (entity != null)
			return EntityUtils.toString(entity);
		else
			return null;
	}
	
	
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		HttpClientKeepSession httpClient = new HttpClientKeepSession();
		String content = null;
		httpClient.get("http://www.nzhg.co.nz");
		//用户登陆
		content = httpClient.post("http://www.nzhg.co.nz/user/handleLogin", "accountVal=0291284151&passwordVal=lion1976");
//
//		try {
//			Thread.sleep(2000);
//		} catch (Exception e) {}
//		
		String url = "http://www.nzhg.co.nz/product/detail?id=1000000";
		content = httpClient.get(url);
		String price = StrUtil.subString(content, "<span class=\"info-detail price\">", "</span>");
		System.out.println("产品价格: " + price);
	}

}
