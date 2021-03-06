package kr.or.ddit.prod.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.prod.service.IProdService;
import kr.or.ddit.vo.PagingVO;
import kr.or.ddit.vo.ProdVO;

@Controller
public class ProdReadController{
	@Inject
	private IProdService service;
	
	@RequestMapping("/prod/prodList.do")
	public String list(
			@ModelAttribute("detailSearch") ProdVO detailSearch
			,@RequestParam(value="page", required=false, defaultValue="1") int currentPage
			,Model model
			,HttpServletRequest req
			,HttpServletResponse resp) throws ServletException, IOException {
		
		PagingVO<ProdVO> pagingVO = new PagingVO<>();
		pagingVO.setCurrentPage(currentPage);
		pagingVO.setDetailSearch(detailSearch);
		
		int totalRecord = service.retrieveProdCount(pagingVO);
		pagingVO.setTotalRecord(totalRecord);
				
		List<ProdVO> prodList =  service.retrieveProdList(pagingVO);
		pagingVO.setDataList(prodList);

		String accept = req.getHeader("Accept");
		String view = null;
		if(StringUtils.containsIgnoreCase(accept, "json")) {
			resp.setContentType("application/json;charset=UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			try(
				PrintWriter out = resp.getWriter();
			){
				mapper.writeValue(out, pagingVO);
			}
		}else {
			model.addAttribute("pagingVO", pagingVO);
			view = "prod/prodList";
		}
		return view;
		
	}
	
	@RequestMapping("/prod/prodView.do")
	public String view(
			@RequestParam(value="what", required=true) String prod_id
			, HttpServletRequest req){
		
		ProdVO prod = service.retrieveProd(prod_id);
		req.setAttribute("prod", prod);
		
		return "prod/prodView";
	}
}
