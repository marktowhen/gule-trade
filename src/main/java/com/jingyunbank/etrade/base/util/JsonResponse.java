package com.jingyunbank.etrade.base.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public final class JsonResponse {

	public static void write(HttpServletResponse response, String result) throws IOException{
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "nocache");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(result);
		writer.flush();
		writer.close();
	}
}
