package com.jingyunbank.etrade.logistic.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.etrade.api.logistic.bo.KDNShow;
import com.jingyunbank.etrade.api.logistic.bo.LogisticData;
import com.jingyunbank.etrade.api.logistic.service.ILogisticService;

@Service("kdnService")
public class KDNService implements ILogisticService {

	// 电商ID
	private static String EBusinessID = "1255799";
	// 电商加密私钥，快递鸟提供，注意保管，不要泄漏
	private static String AppKey = "0aa8e055-436c-49ed-86cb-ec966b613e8a";
	// 请求url
	private static String ReqURL = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";

	// base64编码
	private static String base64(String str, String charset) throws UnsupportedEncodingException {
		String encoded = Base64.getEncoder().encodeToString(str.getBytes(charset));
		return encoded;
	}

	private static String urlEncoder(String str, String charset) throws UnsupportedEncodingException {
		String result = URLEncoder.encode(str, charset);
		return result;
	}

	// MD5加密
	private static String MD5(String str, String charset) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes(charset));
		byte[] result = md.digest();
		StringBuffer sb = new StringBuffer(32);
		for (int i = 0; i < result.length; i++) {
			int val = result[i] & 0xff;
			if (val <= 0xf) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(val));
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * 电商Sign签名生成
	 * 
	 * @param content
	 *            内容
	 * @param keyValue
	 *            Appkey
	 * @param charset
	 *            编码方式
	 * @throws UnsupportedEncodingException
	 *             ,Exception
	 * @return DataSign签名
	 */
	private static String encrypt(String content, String keyValue, String charset)
			throws UnsupportedEncodingException, Exception {
		if (keyValue != null) {
			return base64(MD5(content + keyValue, charset), charset);
		}
		return base64(MD5(content, charset), charset);
	}

	/**
	 * Json方式 查询订单物流轨迹
	 * 
	 * @throws Exception
	 */
	public static String getOrderTracesByJson() throws Exception {
		String requestData = "{'OrderCode':'123456','ShipperCode':'ZTO','LogisticCode':'719151393719'}";
		Map<String, String> params = new HashMap<String, String>();
		params.put("RequestData", urlEncoder(requestData, "UTF-8"));
		params.put("EBusinessID", EBusinessID);
		params.put("RequestType", "1002");
		String dataSign = encrypt(requestData, AppKey, "UTF-8");
		params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
		params.put("DataType", "2");
		String result = sendPost(ReqURL, params);
		// 根据公司业务处理返回的信息......
		return result;
	}

//	public static void main(String[] args) {
//		try {
//			String result = getOrderTracesByJson();
//			System.out.println("==>>:" + result);
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param params
	 *            请求的参数集合
	 * @return 远程资源的响应结果
	 */
	private static String sendPost(String url, Map<String, String> params) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// POST方法
			conn.setRequestMethod("POST");
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.connect();
			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			if (params != null) {
				StringBuilder param = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (param.length() > 0) {
						param.append("&");
					}
					param.append(entry.getKey());
					param.append("=");
					param.append(entry.getValue());
					System.out.println(entry.getKey() + ":" + entry.getValue());
				}
				// System.out.println("param:" + param.toString());
				out.write(param.toString());
			}
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}

	/**
	 * ZTO 719151393719
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static String getOrderTracesByJson(Map<Object, Object> map) throws Exception {
		String requestData = "{'OrderCode':'AAA','ShipperCode':'BBB','LogisticCode':'CCC'}";
		requestData = requestData.replace("AAA", map.get("OrderCode") + "").replace("BBB", map.get("ShipperCode") + "")
				.replace("CCC", map.get("LogisticCode") + "");
		Map<String, String> params = new HashMap<String, String>();
		params.put("RequestData", urlEncoder(requestData, "UTF-8"));
		params.put("EBusinessID", EBusinessID);
		params.put("RequestType", "1002");
		String dataSign = encrypt(requestData, AppKey, "UTF-8");
		params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
		params.put("DataType", "2");
		String result = sendPost(ReqURL, params);
		// 根据公司业务处理返回的信息......
		return result;
	}

	@Override
	public List<LogisticData> getRemoteExpress(Map<Object, Object> map) throws Exception {
		String result = getOrderTracesByJson(map);
		//System.out.println("result::"+result);
		ObjectMapper obj = new ObjectMapper();
		KDNShow show = obj.readValue(result.toLowerCase(), KDNShow.class);
		List<LogisticData> list = new ArrayList<LogisticData>();
		if (show.isSuccess()) {
			list = show.getTraces().stream().map(bo -> {
				LogisticData vo = new LogisticData();
				vo.setTime(bo.getAccepttime());
				vo.setContent(bo.getAcceptstation());
				vo.setRemark(bo.getRemark());
				return vo;
			}).collect(Collectors.toList());

			/*
			 * for (KDNContent con : show.getTraces()) { data = new
			 * LogisticData(); data.setTime(con.getAccepttime());
			 * data.setContent(con.getAcceptstation());
			 * data.setRemark(con.getRemark()); list.add(data); }
			 */
		}
		return list;
	}

}
