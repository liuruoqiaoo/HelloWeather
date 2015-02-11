package com.liuruoqiao.helloweather.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ParseException;

public class HttpClientGet {
	private static DefaultHttpClient httpClient = null;

	public static String HttpClientGet(Context context, String url_path) {
		try {
			// 构造httpGet对象
			HttpGet httpget = new HttpGet(url_path);
			/*
			 * httpget.setHeader("Content-Type",
			 * "application/x-www-form-urlencoded");
			 */
			// 自定义的httpClient
			DefaultHttpClient httpClient = getHttpClient(context);
			// 用httpClient的execute发送请求，返回httpresponse
			HttpResponse httpresponse = httpClient.execute(httpget);
			// HttpStatus.SC_OK代表http请求码200，代表请求成功的状态码
			if (httpresponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("HttpClientGet失败");
			}
			// 获得HttpEntity对象，该对象包装了服务器的响应内容
			HttpEntity entity = httpresponse.getEntity();
			// EntityUtils.toString(entity);如果没有utf-8,就会结果出现乱码，这个是转换将返回的结果的编码格式
			return EntityUtils.toString(entity, HTTP.UTF_8);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	private static synchronized DefaultHttpClient getHttpClient(Context context) {
		if (null == httpClient) {
			HttpParams params = new BasicHttpParams();
			// 设置一些基本参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams
					.setUserAgent(
							params,
							"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
									+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			// 超时设置 从连接池中取连接的超时时间
			ConnManagerParams.setTimeout(params, 1000);
			int ConnectionTimeOut = 5000;
			/*
			 * if (!HttpUtils.isWifiDataEnable(context)) { ConnectionTimeOut =
			 * 10000; }
			 */
			HttpConnectionParams
					.setConnectionTimeout(params, ConnectionTimeOut);
			// 请求超时
			HttpConnectionParams.setSoTimeout(params, 4000);
			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			httpClient = new DefaultHttpClient(conMgr, params);
		}
		return httpClient;
	}

}
