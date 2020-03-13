package tech.fsdn.javatoshell2.utils;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import tech.fsdn.javatoshell2.utils.StringUtil;

public class HttpClientUtil {
	private  static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * 连接超时时间
	 */
	public static final int CONNECTION_TIMEOUT_MS = 360000;

	/**
	 * 读取数据超时时间
	 */
	public static final int SO_TIMEOUT_MS = 360000;

	public static final String CONTENT_TYPE_JSON_CHARSET = "application/json;charset=utf-8";

	public static final String CONTENT_TYPE_XML_CHARSET = "application/xml;charset=utf-8";
	
	
	public static final String CONTENT_TYPE_JSON_CHARSET_UTF8 = "application/json;charset=utf-8";

	public static final String CONTENT_TYPE_XML_CHARSET_UTF8 = "application/xml;charset=utf-8";


	/**
	 * httpclient读取内容时使用的字符集
	 */
	public static final String CHARSET_GBK = "GBK";

	public static final String CHARSET_UTF8 = "UTF-8";
	
	public static final Charset UTF_8 = Charset.forName(CHARSET_UTF8);

	public static final Charset GBK = Charset.forName(CHARSET_GBK);
	
	public static String JsonPostInvoke(String url, Map<String, Object> params, String charsets) 
			throws Exception{  
        // 接收参数json列表  
        JSONObject jsonParam = new JSONObject();  
        // http客户端
        HttpClient httpClient = null;
        String data = null;
        
        try {
			httpClient = buildHttpClient(false);
			// post请求
			HttpPost method = new HttpPost(url);  
			if(null != params){
				for(String key : params.keySet()){
					jsonParam.put(key, params.get(key));
				}
				// 参数实体
				StringEntity entity = new StringEntity(jsonParam.toString(), CHARSET_UTF8);//解决中文乱码问题    
				entity.setContentEncoding(CHARSET_UTF8);    
			    entity.setContentType(CONTENT_TYPE_JSON_CHARSET_UTF8);
			    method.setEntity(entity);
			}
			// 执行响应操作
			HttpResponse result = httpClient.execute(method);  
			// 请求结束，返回结果  
			data = EntityUtils.toString(result.getEntity());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
        return data;
	} 
	public static Map<String,Object> JsonGetInvoke(String url, String charsets) 
			throws ClientProtocolException, IOException{  
		System.out.println("get方式提交json数据开始......");
		// 接收参数json列表  
		
		 Map<String,Object> resultMap=new HashMap<String,Object>();
		// http客户端
		HttpClient httpClient = null;
		
		try {
			httpClient = buildHttpClient(false);
			// post请求
			HttpGet method = new HttpGet(url);  
			
			// 执行响应操作
			HttpResponse result = httpClient.execute(method);  
			// 请求结束，返回结果  
			String data = EntityUtils.toString(result.getEntity()); 
			resultMap.put("body", data);
			int status = result.getStatusLine().getStatusCode();
			resultMap.put("status", status);
			Header[] headers= result.getAllHeaders();
			resultMap.put("header", headers);
			resultMap.put("resultLink", url);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			if(httpClient != null){
				httpClient.getConnectionManager().shutdown();
			}
		}
		return resultMap;
	} 
	
	
	public static Map<String,Object> JsonGetInvoke(String url, String charsets,HttpClient httpClient) 
			throws ClientProtocolException, IOException{  
		System.out.println("get方式提交json数据开始......");
		// 接收参数json列表  
		
		 Map<String,Object> resultMap=new HashMap<String,Object>();
		
			
		
		try {
			if (httpClient==null)
					httpClient = buildHttpClient(false);
			// post请求
			HttpGet method = new HttpGet(url);  
			
			// 执行响应操作
			HttpResponse result = httpClient.execute(method);  
			// 请求结束，返回结果  
			String data = EntityUtils.toString(result.getEntity()); 
			resultMap.put("body", data);
			int status = result.getStatusLine().getStatusCode();
			resultMap.put("status", status);
			Header[] headers= result.getAllHeaders();
			resultMap.put("header", headers);
			resultMap.put("resultLink", url);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			if(httpClient != null){
				httpClient.getConnectionManager().shutdown();
			}
		}
		return resultMap;
	} 
	
	public static String JsonGetInvoke(String url, String charsets,String str) 
			throws ClientProtocolException, IOException{  
		System.out.println("get方式提交json数据开始......");
		// 接收参数json列表  
		JSONObject jsonParam = new JSONObject();  
		// 执行响应操作
		HttpResponse result = null;
		HttpClient httpClient = null;
		String data = null;
		try {
			// http客户端
			httpClient = buildHttpClient(false);
			// post请求
			HttpGet method = new HttpGet(url);  
			
			result = httpClient.execute(method);
			
			// 请求结束，返回结果  
			data = EntityUtils.toString(result.getEntity()); 
			System.out.println(data);
			System.out.println("get方式提交json数据结束......");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}  finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		return data;
	} 
	
	/***
	 * 只传入url和参数不需要编码
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static Map<String,Object> JsonPostInvokeNew(String url, Map<String, Object> params) 
			throws ClientProtocolException, IOException{  
        // 接收参数json列表  
        JSONObject jsonParam = new JSONObject();  
        
        HttpClient httpClient = null;
        Map<String,Object> resultMap=new HashMap<String,Object>();
        
        try {
			// http客户端
			httpClient = buildHttpClient(false);
			// post请求
			HttpPost method = new HttpPost(url);  
			if(null != params){
				for(String key : params.keySet()){
					jsonParam.put(key, params.get(key));
				}
				// 参数实体
				StringEntity entity = new StringEntity(jsonParam.toString(), CHARSET_UTF8);//解决中文乱码问题    
				entity.setContentEncoding(CHARSET_UTF8);    
			    entity.setContentType(CONTENT_TYPE_JSON_CHARSET_UTF8);    
			    method.setEntity(entity);  
			}
			// 执行响应操作
			HttpResponse result = null;
			String data="";
			
			try {
				result = httpClient.execute(method);
				data= EntityUtils.toString(result.getEntity()); 
				resultMap.put("body", data);
				int status = result.getStatusLine().getStatusCode();
				resultMap.put("status", status);
				Header[] headers= result.getAllHeaders();
				resultMap.put("header", headers);
				resultMap.put("resultLink", url);
			} catch (Exception e) {
				data="请求错误";
				resultMap.put("body", e.getMessage());
				resultMap.put("status", "404");
				resultMap.put("resultLink", url);
				
			}
		} catch (UnsupportedCharsetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			if(httpClient != null){
				httpClient.getConnectionManager().shutdown();
			}
		} 
        // 请求结束，返回结果  
       
        return resultMap;
	} 
	
	public static String JsonPostInvoke(String url, Map<String, Object> params) 
			throws ClientProtocolException, IOException{  
	        System.out.println("post方式提交json数据开始......");
	        // 接收参数json列表  
	        JSONObject jsonParam = new JSONObject();  
	        HttpClient httpClient = null;
	        String data="";
	        
	        try {
				// http客户端
				httpClient = buildHttpClient(false);
				// post请求
				HttpPost method = new HttpPost(url);  
				if(null != params){
					for(String key : params.keySet()){
						jsonParam.put(key, params.get(key));
					}
					// 参数实体
					StringEntity entity = new StringEntity(jsonParam.toString(), CHARSET_UTF8);//解决中文乱码问题    
					entity.setContentEncoding(CHARSET_UTF8);    
				    entity.setContentType(CONTENT_TYPE_JSON_CHARSET_UTF8);    
				    method.setEntity(entity);  
				}
				// 执行响应操作
				HttpResponse result = null;
				
				try {
					result = httpClient.execute(method);
					data= EntityUtils.toString(result.getEntity()); 
				} catch (Exception e) {
					data="请求错误";
					logger.error(e.getMessage());
				
				}
			} catch (UnsupportedCharsetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}  finally {
				if(httpClient != null){
					httpClient.getConnectionManager().shutdown();
				}
			}
	        // 请求结束，返回结果  
	       
	        return data;
	}

	/**
	 * 可以设置header
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String JsonPostInvoke(String url, Map<String, Object> params,Header[] headers)
			throws ClientProtocolException, IOException{
	        System.out.println("post方式提交json数据开始......");
	        // 接收参数json列表
	        JSONObject jsonParam = new JSONObject();
	        HttpClient httpClient = null;
	        String data="";

	        try {
				// http客户端
				httpClient = buildHttpClient(false);
				// post请求
				HttpPost method = new HttpPost(url);
				if(null != params){
					for(String key : params.keySet()){
						jsonParam.put(key, params.get(key));
					}
					// 参数实体
					StringEntity entity = new StringEntity(jsonParam.toString(), CHARSET_UTF8);//解决中文乱码问题
					entity.setContentEncoding(CHARSET_UTF8);
				    entity.setContentType(CONTENT_TYPE_JSON_CHARSET_UTF8);
				    method.setEntity(entity);
				    method.setHeaders(headers);
				}
				// 执行响应操作
				HttpResponse result = null;

				try {
					result = httpClient.execute(method);
					data= EntityUtils.toString(result.getEntity());
				} catch (Exception e) {
					data="请求错误";
					logger.error(e.getMessage());

				}
			} catch (UnsupportedCharsetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}  finally {
				if(httpClient != null){
					httpClient.getConnectionManager().shutdown();
				}
			}
	        // 请求结束，返回结果

	        return data;
	}

	
	public static String JsonPostInvoke(String url, Map<String, Object> params , HttpClient httpClient) 
			throws ClientProtocolException, IOException{  
	        System.out.println("post方式提交json数据开始......");
	        // 接收参数json列表  
	        JSONObject jsonParam = new JSONObject();  
	        String data="";
	        
	        try {
				// http客户端
	        	if(httpClient == null)
	        		httpClient = buildHttpClient(false);
				// post请求
				HttpPost method = new HttpPost(url);  
				if(null != params){
					for(String key : params.keySet()){
						jsonParam.put(key, params.get(key));
					}
					// 参数实体
					StringEntity entity = new StringEntity(jsonParam.toString(), CHARSET_UTF8);//解决中文乱码问题    
					entity.setContentEncoding(CHARSET_UTF8);    
				    entity.setContentType(CONTENT_TYPE_JSON_CHARSET_UTF8);    
				    method.setEntity(entity);  
				}
				// 执行响应操作
				HttpResponse result = null;
				
				try {
					result = httpClient.execute(method);
					data= EntityUtils.toString(result.getEntity()); 
				} catch (Exception e) {
					data="请求错误";
					logger.error(e.getMessage());
				
				}
			} catch (UnsupportedCharsetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}  finally {
				if(httpClient != null){
					httpClient.getConnectionManager().shutdown();
				}
			}
	        // 请求结束，返回结果  
	       
	        return data;
	}
	
	/**
	 * 简单get调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String simpleGetInvoke(String url, Map<String, String> params)
			throws ClientProtocolException, IOException, URISyntaxException {
		return simpleGetInvoke(url, params, CHARSET_UTF8);
	}

	/**
	 * 简单get调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String simpleGetInvoke(String url, Map<String, String> params, String charset)
			throws ClientProtocolException, IOException, URISyntaxException {
		HttpClient client = null;
		try {
			client = buildHttpClient(false);
			HttpGet get = buildHttpGet(url, params);
			HttpResponse response = client.execute(get);
			assertStatus(response);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String returnStr = EntityUtils.toString(entity, charset);
				return returnStr;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}finally {
			if(client != null){
				client.getConnectionManager().shutdown();
			}
		}
		return null;
	}

	/**
	 * 简单post调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String simplePostInvoke(String url, Map<String, String> params)
			throws URISyntaxException, ClientProtocolException, IOException {
		return simplePostInvoke(url, params, CHARSET_UTF8);
	}

	/**
	 * 简单post调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String simplePostInvoke(String url, Map<String, String> params, String charset)
			throws URISyntaxException, ClientProtocolException, IOException {
		HttpClient client = null;
		try {
			client = buildHttpClient(false);
			HttpPost postMethod = buildHttpPost(url, params, charset);
			HttpResponse response = client.execute(postMethod);
			assertStatus(response);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String returnStr = EntityUtils.toString(entity, charset);
				return returnStr;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}finally {
			if(client!=null){
				client.getConnectionManager().shutdown();
			}
		}
		
		return null;
	}
	
	/**
	 * 简单post调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String simplePostInvoke(String url, Map<String, String> params, HttpClient client)
			throws URISyntaxException, ClientProtocolException, IOException {
		try {
			if(client == null)
				client = buildHttpClient(false);
			HttpPost postMethod = buildHttpPost(url, params, CHARSET_UTF8);
			postMethod.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON_CHARSET);
			HttpResponse response = client.execute(postMethod);
			assertStatus(response);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String returnStr = EntityUtils.toString(entity, CHARSET_UTF8);
				return returnStr;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}finally {
			if(client!=null){
				client.getConnectionManager().shutdown();
			}
		}
		
		return null;
	}

	/**
	 * 创建HttpClient
	 * 
	 * @param isMultiThread
	 * @return
	 */
	public static HttpClient buildHttpClient(boolean isMultiThread) {
		CloseableHttpClient client;
		if (isMultiThread)
			client = HttpClientBuilder.create().setConnectionManager(new PoolingHttpClientConnectionManager()).build();
		else
			client = HttpClientBuilder.create().build();
		// 设置代理服务器地址和端口
		return client;
	}

	/**
	 * 构建httpPost对象
	 * 
	 * @param url

	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 */
	public static HttpPost buildHttpPost(String url, Map<String, String> params, String charset)
			throws UnsupportedEncodingException, URISyntaxException {
		Assert.notNull(url, "构建HttpPost时,url不能为null");
		HttpPost post = new HttpPost(url);
		setCommonHttpMethod(post);
		HttpEntity he = null;
		if (params != null) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				formparams.add(new BasicNameValuePair(key, params.get(key)));
			}
			he = new UrlEncodedFormEntity(formparams, Charset.forName(charset));
			post.setEntity(he);
		}
		return post;

	}

	/**
	 * 构建httpGet对象
	 * 
	 * @param url

	 * @return
	 * @throws URISyntaxException
	 */
	public static HttpGet buildHttpGet(String url, Map<String, String> params) throws URISyntaxException {
		Assert.notNull(url, "构建HttpGet时,url不能为null");
		HttpGet get = new HttpGet(buildGetUrl(url, params));
		return get;
	}

	/**
	 * build getUrl str
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private static String buildGetUrl(String url, Map<String, String> params) {
		StringBuffer uriStr = new StringBuffer(url);
		if (params != null) {
			List<NameValuePair> ps = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				ps.add(new BasicNameValuePair(key, params.get(key)));
			}
			uriStr.append("?");
			uriStr.append(URLEncodedUtils.format(ps, UTF_8));
		}
		return uriStr.toString();
	}

	/**
	 * 设置HttpMethod通用配置
	 * 
	 * @param httpMethod
	 */
	public static void setCommonHttpMethod(HttpRequestBase httpMethod) {
		httpMethod.setHeader(HTTP.CONTENT_ENCODING, CHARSET_UTF8);// setting
		httpMethod.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON_CHARSET);
	}

	/**
	 * 设置成消息体的长度 setting MessageBody length
	 * 
	 * @param httpMethod
	 * @param he
	 */
	public static void setContentLength(HttpRequestBase httpMethod, HttpEntity he) {
		if (he == null) {
			return;
		}
		httpMethod.setHeader(HTTP.CONTENT_LEN, String.valueOf(he.getContentLength()));
	}

	/**
	 * 构建公用RequestConfig
	 * 
	 * @return
	 */
	public static RequestConfig buildRequestConfig() {
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SO_TIMEOUT_MS)
				.setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
		return requestConfig;
	}

	/**
	 * 强验证必须是200状态否则报异常
	 * 
	 * @param res
	 * @throws HttpException
	 */
	static void assertStatus(HttpResponse res) throws IOException {
		Assert.notNull(res, "http响应对象为null");
		Assert.notNull(res.getStatusLine(), "http响应对象的状态为null");
		switch (res.getStatusLine().getStatusCode()) {
		case HttpStatus.SC_OK:
			break;
		default:
			throw new IOException("服务器响应状态异常,失败.");
		}
	}

	private HttpClientUtil() {
	}
	public static String getContent(String url) throws Exception{  
        String backContent = null;  
        //先建立一个客户端实例，将模拟一个浏览器  
        HttpClient httpclient = null;  
        HttpGet httpget = null;
        HttpPost httpPost=null;
        BufferedReader in = null;
        try {  
            //************************************************************  
            // 设置超时时间  
            // 创建 HttpParams 以用来设置 HTTP 参数  
            HttpParams params = new BasicHttpParams();  
            // 设置连接超时和 Socket 超时，以及 Socket 缓存大小  
            HttpConnectionParams.setConnectionTimeout(params, 180 * 1000);  
            HttpConnectionParams.setSoTimeout(params, 180 * 1000);  
            HttpConnectionParams.setSocketBufferSize(params, 8192);  
            // 设置重定向，缺省为 true  
            HttpClientParams.setRedirecting(params, false);  
            //************************************************************     
            httpclient = new DefaultHttpClient(params);  
//          httpclient = new DefaultHttpClient();  
            // 建立一个get方法请求，提交刷新  
            httpget = new HttpGet(url);       
              
            HttpResponse response = httpclient.execute(httpget);   
            //HttpStatus.SC_OK(即:200)服务器收到并理解客户端的请求而且正常处理了  
            HttpEntity entity = response.getEntity();  
            if (entity != null) {              
                //start 读取整个页面内容  
                InputStream is = entity.getContent();  
                in = new BufferedReader(new InputStreamReader(is));   
                StringBuffer buffer = new StringBuffer();   
                String line = "";  
                while ((line = in.readLine()) != null) {  
                    buffer.append(line);  
                }  
                //end 读取整个页面内容  
                backContent = buffer.toString();  
            }  
        } catch (Exception e) {  
            httpget.abort();  
            backContent = "有异常,获取不到";     
            e.printStackTrace();  
            logger.error(e.getMessage(), e);
        }finally{  
            if(httpclient != null)  
                httpclient.getConnectionManager().shutdown();  
            if(in != null){
            	in.close();
            }
        }          
        //返回结果  
        return backContent;  
    }  
	
	
	
	/**
	 * 用urlconnect  json post 传递参数
	 */
	public static String urlPost(String url,String jsonString){
		if (url==null || StringUtils.isEmpty(url)){
			return "";
		}
		if (jsonString==null || StringUtils.isEmpty(jsonString)){
			return "";
		}
		String returnLine = "";
		BufferedReader reader = null;
		DataOutputStream out = null;
		try {
			URL my_url = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) my_url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.connect();
			out = new DataOutputStream(connection.getOutputStream());
			byte[] content = jsonString.getBytes("utf-8");
			out.write(content, 0, content.length);
			out.flush();
			out.close(); // flush and close
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			//StringBuilder builder = new StringBuilder();
			String line = "";
			while ((line = reader.readLine()) != null) {
				// line = new String(line.getBytes(), "utf-8");
				returnLine += line;
			}
			reader.close();
			connection.disconnect();

			return returnLine;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}
			}
		}
		return "";
	}
	public static String JsonPostInvoke(String url, String jsonParam,Header[] headers)
			throws ClientProtocolException, IOException{  
        // 接收参数json列表  
        // http客户端
        HttpClient httpClient = buildHttpClient(false);
        // post请求
        HttpPost method = new HttpPost(url);  
      
        	// 参数实体
        	StringEntity entity = new StringEntity(jsonParam, CHARSET_UTF8);//解决中文乱码问题    
        	entity.setContentEncoding(CHARSET_UTF8);    
            entity.setContentType(CONTENT_TYPE_JSON_CHARSET_UTF8);    
            method.setEntity(entity);
            method.setHeaders(headers);
        // 执行响应操作
        HttpResponse result = null;
        String data="";
		try {
			result = httpClient.execute(method);
			data= EntityUtils.toString(result.getEntity()); 
		} catch (Exception e) {
			data="请求错误";
			logger.error(e.getMessage());
		}finally {
			if(httpClient != null){
				httpClient.getConnectionManager().shutdown();
			}
		}  
        return data;
	} 
	  public static String postStringInvoke(String url, Map<String, String> params)
	  {
	    CloseableHttpClient httpclient = HttpClients.createDefault();

	    HttpPost httppost = new HttpPost(url);

	    List formparams = new ArrayList();
	    for (String key : params.keySet()) {
	      formparams.add(new BasicNameValuePair(key, (String)params.get(key)));
	    }
	    try
	    {
	      UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
	      httppost.setEntity(uefEntity);
	      System.out.println("executing request " + httppost.getURI());
	      CloseableHttpResponse response = httpclient.execute(httppost);
	      try {
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {
	          System.out.println("--------------------------------------");
	          String result = EntityUtils.toString(entity, "UTF-8");
	          System.out.println("Response content: " + result);
	          System.out.println("--------------------------------------");
	          String str1 = result;

	          response.close();

	          return str1;
	        }
	      } finally {
	        response.close();
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	      logger.error(e.getMessage(), e);
	      try
	      {
	        httpclient.close();
	      } catch (IOException f) {
	        f.printStackTrace();
	        logger.error(e.getMessage(), e);
	      }
	    }
	    finally
	    {
	      try
	      {
	        httpclient.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	        logger.error(e.getMessage(), e);
	      }
	    }
	    return null;
	  }
	  public static String postObjectInvoke(String url, Map<String, Object> params)
	  {
	    CloseableHttpClient httpclient = HttpClients.createDefault();

	    HttpPost httppost = new HttpPost(url);

	    JSONObject jsonParam = new JSONObject();
	    if (params != null) {
	      for (String key : params.keySet()) {
	        jsonParam.put(key, params.get(key));
	      }

	      StringEntity entity = new StringEntity(jsonParam.toString(), "UTF-8");
	      entity.setContentEncoding("UTF-8");
	      entity.setContentType("application/json;charset=utf-8");
	      httppost.setEntity(entity);
	    }
	    try {
	      System.out.println("executing request " + httppost.getURI());
	      CloseableHttpResponse response = httpclient.execute(httppost);
	      try {
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {
	          System.out.println("--------------------------------------");
	          String result = EntityUtils.toString(entity, "UTF-8");
	          System.out.println("Response content: " + result);
	          System.out.println("--------------------------------------");
	          String str1 = result;

	          response.close();

	          return str1;
	        }
	      } finally {
	        response.close();
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	      logger.error(e.getMessage(), e);
	      try
	      {
	        httpclient.close();
	      } catch (IOException f) {
	        f.printStackTrace();
	        logger.error(f.getMessage(), f);
	      }
	    }
	    finally
	    {
	      try
	      {
	        httpclient.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	        logger.error(e.getMessage(), e);
	      }
	    }
	    return null;
	  }

	
}