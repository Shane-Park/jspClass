package kr.or.ddit.board.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.board.dao.AttachDAOImpl;
import kr.or.ddit.board.dao.IAttachDAO;
import kr.or.ddit.mvc.annotation.Controller;
import kr.or.ddit.mvc.annotation.RequestMapping;
import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.resolvers.RequestParam;
import kr.or.ddit.mvc.annotation.resolvers.RequestPart;
import kr.or.ddit.mvc.filter.wrapper.MultipartFile;
import kr.or.ddit.vo.AttachVO;
import kr.or.ddit.vo.BoardVO;

@Controller
public class BoardFileController {
	IAttachDAO attachDAO = AttachDAOImpl.getInstance();
	
	@RequestMapping(value="/board/boardImage.do", method = RequestMethod.POST)
	public String imageUpload(
		@RequestPart("upload") MultipartFile upload
		,HttpServletResponse resp
		,HttpServletRequest req
		) throws IOException {
		String saveFolderUrl = "/boardImages";
		String saveFolderPath = req.getServletContext().getRealPath(saveFolderUrl);
		File saveFolder = new File(saveFolderPath);
		Map<String, Object> resultMap = new HashMap<>();
		if(!upload.isEmpty()) {
			upload.saveTo(saveFolder);
			int uploaded = 1;
			String fileName = upload.getOriginalFilename();
			String url = req.getContextPath() + saveFolderUrl + "/" + upload.getUniqueSaveName();
			resultMap.put("uploaded",uploaded);
			resultMap.put("fileName",fileName);
			resultMap.put("url",url);
		}
		
		resp.setContentType("application/json; charset=UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		try(
			PrintWriter out = resp.getWriter();
			){
			mapper.writeValue(out, resultMap);
		}
		
		return null;
	}
	
	@RequestMapping(value="/board/fileDelete.do",method = RequestMethod.POST)
	public String deleteFileAjax(
			HttpServletResponse resp
			,@RequestParam("att_no") int att_no
			) throws IOException {
		BoardVO board = new BoardVO();
		List<AttachVO> attachList = new ArrayList<>();
		AttachVO attach = new AttachVO();
		attach.setAtt_no(att_no);
		attachList.add(attach);
		board.setAttachList(attachList);
		
		int result = attachDAO.deleteAttaches(board);
		
		try(
			PrintWriter out = resp.getWriter()
		){
			if(result > 0) {
				out.write("OK");
			}else {
				out.write("FAIL");
			}
		}
		
		return null;
	}
	
}
