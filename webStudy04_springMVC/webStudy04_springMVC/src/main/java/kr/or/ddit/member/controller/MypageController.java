package kr.or.ddit.member.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.member.service.AuthenticateServiceImpl;
import kr.or.ddit.member.service.IAuthenticateService;
import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.vo.MemberVO;

@Controller
public class MypageController {
	@Inject
	IMemberService service = new MemberServiceImpl();
	@Inject
	IAuthenticateService authService = new AuthenticateServiceImpl();
	
	@RequestMapping("/mypage.do")
	public String passwordForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		return "member/passwordForm";
	}
	
	@RequestMapping(value="/mypage.do", method=RequestMethod.POST)
	public String mypageCheckPassword(
			@RequestParam(value="mem_pass") String mem_pass
			,HttpSession session
			,HttpServletRequest req) throws ServletException, IOException {
		
		MemberVO authMember = (MemberVO) session.getAttribute("authMember");
		String mem_id = authMember.getMem_id();
		
		ServiceResult result = authService.authenticate(new MemberVO(mem_id, mem_pass));
		String view = null;
		
		if(ServiceResult.OK.equals(result)) {
			MemberVO detailMember = service.retrieveMember(mem_id);
			req.setAttribute("member", detailMember);
			view = "member/mypage";
		}else {
			session.setAttribute("message", "비번 오류");
			view = "redirect:/mypage.do";
		}
		
		return view;
	}
	
}
