package com.liuruoqiao.helloweather.utils;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

/**
 * @author Ruoqiao url的加密等，调用静态方法getInterfaceURL可直接获得url
 */
public class UrlUtils {
	private final static String TAG = "UrlUtil";
	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";
	private static final String appid_old = "c61ed11753c3ade6";
	private static final String private_key_old = "0905b7_SmartWeatherAPI_e58c89f";
	private static final String appid_new = "e2f731aaf32e8d89";
	private static final String private_key_new = "4cf244_SmartWeatherAPI_b5a8772";
	private static final String url_header = "http://open.weather.com.cn/data/?";

	private static byte[] HmacSHA1Encrypt(String url, String privatekey)
			throws Exception {
		byte[] data = privatekey.getBytes(ENCODING);
		SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
		Mac mac = Mac.getInstance(MAC_NAME);
		mac.init(secretKey);
		byte[] text = url.getBytes(ENCODING);
		return mac.doFinal(text);
	}

	/**
	 * @param url
	 * @param privatekey
	 * @return
	 * @throws Exception
	 *             加密获得publickey
	 */
	private static String getKey(String url, String privatekey)
			throws Exception {
		byte[] key_bytes = HmacSHA1Encrypt(url, privatekey);
		String base64encoderStr = Base64.encodeToString(key_bytes,
				Base64.NO_WRAP);
		return URLEncoder.encode(base64encoderStr, ENCODING);
	}

	private static String getInterfaceURL(String areaid, String type,
			String date) throws Exception {
		String keyurl = url_header + "areaid=" + areaid + "&type=" + type
				+ "&date=" + date + "&appid=";
		String appid = "";
		String private_key = "";
		// 我申请了2个appid，因为智慧天气接口改版，前一个appid现在只能用于实况，后一个用于预报和天气指数
		if (type.equals("observe")) {
			appid = appid_old;
			private_key = private_key_old;
		} else {
			appid = appid_new;
			private_key = private_key_new;
		}
		String key = getKey(keyurl + appid, private_key);
		String appid6 = appid.substring(0, 6);// appid的前6位

		return keyurl + appid6 + "&key=" + key;
	}

	public static String getInterfaceURL(String areaid, String type) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
		String date = dateFormat.format(new Date());
		try {
			return getInterfaceURL(areaid, type, date);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e.fillInStackTrace());
		}
		return null;
	}

}
