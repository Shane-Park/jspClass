package kr.or.ddit.prod.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.mvc.annotation.Controller;
import kr.or.ddit.mvc.annotation.RequestMapping;
import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.resolvers.ModelAttribute;
import kr.or.ddit.mvc.annotation.resolvers.RequestParam;
import kr.or.ddit.prod.dao.IOthersDAO;
import kr.or.ddit.prod.dao.OthersDAOImpl;
import kr.or.ddit.prod.service.IProdService;
import kr.or.ddit.prod.service.ProdServiceImpl;
import kr.or.ddit.vo.BuyerVO;
import kr.or.ddit.vo.ProdVO;

@Controller
public class ProdController {
	private IProdService service = ProdServiceImpl.getInstance();
	private IOthersDAO othersDAO = OthersDAOImpl.getInstance();
	
	private void addAttribute(HttpServletRequest req ) {
		List<Map<String, Object>> lprodList = othersDAO.selectLprodList();
		List<BuyerVO> buyerList = othersDAO.selectBuyerList(null);
		req.setAttribute("lprodList", lprodList);
		req.setAttribute("buyerList", buyerList);
	}
	
	private void addCommandAttribute(HttpServletRequest req) {
		req.setAttribute("command", "update");
	}
	
	@RequestMapping("/prod/prodInsert.do")
	public String prodInsertForm(
			HttpServletRequest req) throws ServletException, IOException {
		addAttribute(req);
		return "prod/prodForm";
	}
	
	@RequestMapping("/prod/prodUpdate.do")
	public String prodUpdateForm(
			@RequestParam(value="what") String prodId 
			,HttpServletRequest req
			) throws ServletException, IOException {
		addCommandAttribute(req);
		addAttribute(req);
		
		ProdVO prod = service.retrieveProd(prodId);
		req.setAttribute("prod", prod);
				
		return "prod/prodForm";
	}
	
	@RequestMapping(value="/prod/prodUpdate.do", method=RequestMethod.POST)
	public String prodUpdate(
			@ModelAttribute("prod") ProdVO prod
			,HttpServletRequest req) throws ServletException, IOException {
		
		req.setAttribute("prod", prod);
		
		// ????????? ??????
		Map<String, String> errors = new LinkedHashMap<>();
		req.setAttribute("errors", errors);
		boolean valid = validate(prod, errors);
		String view = null;
		String message = null;
		
		if(valid) {
			ServiceResult result = service.modifyProd(prod);
			switch(result) {
			case OK :
				view = "redirect:/prod/prodView.do?what="+prod.getProd_id();
				break;
			default :
				view = "prod/prodForm";
				message = "?????? ??????, ?????? ??? ?????? ???????????????.";
				break;
			}
		}else {
			view = "prod/prodForm";
		}
		
		req.setAttribute("message", message);
		return view;
		
	}
	
	@RequestMapping(value="/prod/prodInsert.do", method=RequestMethod.POST)
	public String prodInsert(
			@ModelAttribute("prod") ProdVO prod
			,HttpServletRequest req) throws ServletException, IOException {
		req.setAttribute("prod", prod);
		
		Map<String, String> errors = new LinkedHashMap<>();
		req.setAttribute("errors", errors);
		
		boolean valid = validate(prod, errors);
		
		String view = null;
		String message = null;
		
		if(valid) {
			ServiceResult result = service.createProd(prod);
			if(ServiceResult.OK.equals(result)) {
				String newProdId = prod.getProd_id();
				view = "redirect:/prod/prodView.do?what="+newProdId;
			}else {
				message = "?????? ??????. ????????? ?????? ???????????????";
				view = "prod/prodForm";
			}
			req.setAttribute("message", message);
		}else {
			view = "prod/prodForm";
		}
		return view;
		
	}
	
	private boolean validate(ProdVO prod, Map<String,String> errors) {
		return true;
	}
	
	
}
