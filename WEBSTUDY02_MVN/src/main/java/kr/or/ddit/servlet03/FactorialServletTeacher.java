package kr.or.ddit.servlet03;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.enumpkg.MimeType;
import kr.or.ddit.servlet03.view.JsonView;

@WebServlet("/03/factorialTeacher")
public class FactorialServletTeacher extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String view = "/WEB-INF/views/factorialFormTeacher.jsp";
		req.getRequestDispatcher(view).forward(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String single = req.getParameter("single");
		String accept = req.getHeader("accept");
		int status = 200;
		String message = null;
		String view = null;
		
		if(single == null || !single.matches("[0-9]{1,2}+")) {
			status = HttpServletResponse.SC_BAD_REQUEST;
			message = "필수 파라미터 누락";
		}
		
		long op = Long.parseLong(single);
		
		try{
			long result = factorial(op);
			MimeType mime = MimeType.searchMimeType(accept);
			
			Map<String, Object> target = new HashMap<>();
			target.put("op",op);
			target.put("result",result);
			target.put("expression",String.format("%d!=%d", op, result));
			
			if(MimeType.HTML.equals(mime)){
//				resp.setHeader("Content-Type", "application/json; charset=utf-8");
				new JsonView().mergeModelAndView(mime, resp);
			}else {
				req.setAttribute("target", target);
				view = "/WEB-INF/views/factorialFormTeacher.jsp";
			}
			
		}catch(IllegalArgumentException e) {
			status = HttpServletResponse.SC_BAD_REQUEST;
			message = "음수는 연산 불가";
		}
		if(status == HttpServletResponse.SC_OK) {
			if(view != null) {
				req.getRequestDispatcher(view).forward(req, resp);
			}
		}else {
			resp.sendError(status, message);
		}
		
	}
	
	// 재귀호출(recursive calling)
	private long factorial(long op) {
		if(op<=0) {
			throw new IllegalArgumentException("양수만 대상으로 연산 수행가능");
		}if(op == 1) {
			return 1;
		}else {
			return op * factorial(op -1);
		}
	}
	
}
