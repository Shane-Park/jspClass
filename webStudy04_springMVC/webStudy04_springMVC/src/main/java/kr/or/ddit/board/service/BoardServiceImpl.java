package kr.or.ddit.board.service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ddit.board.dao.IAttachDAO;
import kr.or.ddit.board.dao.IBoardDAO;
import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.exception.CustomException;
import kr.or.ddit.utils.CryptoUtil;
import kr.or.ddit.vo.AttachVO;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.PagingVO;

@Service
public class BoardServiceImpl implements IBoardService {
	private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);
	
	private IBoardDAO boardDao;
	@Inject
	public void setBoardDao(IBoardDAO boardDao) {
		this.boardDao = boardDao;
		logger.info("주입된 boardDAO : {}", boardDao.getClass().getName());
	}
	private IAttachDAO attachDao;
	@Inject
	public void setAttachDao(IAttachDAO attachDao) {
		this.attachDao = attachDao;
		logger.info("주입된 boardDAO : {}", attachDao.getClass().getName());
	}
	@Value("#{appInfo.attachPath}")
	private String attachPath;
	private File saveFolder;
	
	@PostConstruct// Life cycle callback
	public void init() {
		saveFolder = new File(attachPath);
		logger.info("{} 초기화, {}주입됨.", getClass().getSimpleName(), saveFolder.getAbsolutePath());
	}
	
	private static BoardServiceImpl self;
	
	private BoardServiceImpl() {}
	public static BoardServiceImpl getInstance() {
		if(self == null) self = new BoardServiceImpl();
		return self;
	}
	
	private void encodePassword(BoardVO board) {
		if(StringUtils.isBlank(board.getBo_pass())) return;
		try {
			String encPass = CryptoUtil.sha512(board.getBo_pass());
			board.setBo_pass(encPass);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private int process(BoardVO board) {
		
		int cnt = 0;
		
		List<AttachVO> attachList = board.getAttachList();
		if(attachList != null && attachList.size()>0) {
			// 첨부파일들 metaData DB에 저장하기
			cnt += attachDao.insertAttaches(board);
			
			// 첨부파일들의 binary데이터를 파일시스템에 저장하기
			try {
				for(AttachVO attach : attachList) {
					attach.saveTo(saveFolder);
				}
				
			} catch(IOException e) {
				throw new RuntimeException();
			}
		}
		return cnt;
	}
	
	@Transactional
	@Override
	public ServiceResult createBoard(BoardVO board) {
		ServiceResult result = ServiceResult.FAIL;
		encodePassword(board); // 비밀번호 암호화
		
			// board DB에 새글 등록
			int cnt = boardDao.insertBoard(board);
			
			if(cnt >0) {
				cnt += process(board);
				
				if(cnt > 0) {
					result = ServiceResult.OK;
				}
			}
		return result;
	}

	@Override
	public int retrieveBoardCount(PagingVO<BoardVO> pagingVO) {
		return boardDao.selectBoardCount(pagingVO);
	}

	@Override
	public List<BoardVO> retrieveBoardList(PagingVO<BoardVO> pagingVO) {
		return boardDao.selectBoardList(pagingVO);
	}

	@Override
	public BoardVO retrieveBoard(BoardVO search) {
		return boardDao.selectBoard(search);
	}
	
	@Transactional
	@Override
	public ServiceResult modifyBoard(BoardVO board) {
			ServiceResult result = ServiceResult.FAIL;
			
			if(board.getDeleteAttachList()!=null && board.getDeleteAttachList().length > 0) {
				List<String> saveNames = attachDao.selectSaveNamesForDelete(board);
				// 첨부 파일의 메타 데이터 삭제
				attachDao.deleteAttaches(board);
				// 이진 데이터 삭제
				String saveFolder ="/Users/shane/Documents/GitHub/jspClass/attaches";
				for(String saveName : saveNames) {
					File saveFile = new File(saveFolder, saveName);
					saveFile.delete();
				}
			}
			
			int cnt = boardDao.updateBoard(board);
			if(cnt >0) {
				cnt += process(board);
				
				if(cnt > 0) {
					result = ServiceResult.OK;
				}
			}
			return result;
			
	}
	
	@Transactional
	@Override
	public ServiceResult removeBoard(BoardVO search) {
		BoardVO board = boardDao.selectBoard(search);
		ServiceResult result = ServiceResult.FAIL;
		int cnt = 0;

		// 해당글의 첨부파일 메타데이터 및 이진데이터 먼저 모두 삭제
			List<AttachVO> attList = board.getAttachList();
			if(attList != null) {
				final int SIZE = attList.size();
				int[] delAttNos = new int[SIZE];
				for(int i=0; i<SIZE; i++) {
					delAttNos[i] = attList.get(i).getAtt_no();
				}
				board.setDeleteAttachList(delAttNos);
						
				if(board.getDeleteAttachList()!=null && board.getDeleteAttachList().length > 0) {
					List<String> saveNames = attachDao.selectSaveNamesForDelete(board);
					// 첨부 파일의 메타 데이터 삭제
					attachDao.deleteAttaches(board);
					// 이진 데이터 삭제
					String saveFolder ="/Users/shane/Documents/GitHub/jspClass/attaches";
					for(String saveName : saveNames) {
						File saveFile = new File(saveFolder, saveName);
						saveFile.delete();
					}
				}
			}
			
			cnt = boardDao.deleteBoard(board);
			
			if(cnt >0) {
				result = ServiceResult.OK;
			}
			
		
		return result;
	}

	@Override
	public AttachVO download(int att_no) {
		AttachVO attach = attachDao.selectAttaches(att_no);
		if(attach == null)
			throw new CustomException(att_no+"에 해당하는 파일이 없음.");
		return attach;
	}
	@Override
	public boolean boardAuthenticate(BoardVO search) {
		BoardVO saved = boardDao.selectBoard(search);
		encodePassword(search);
		String savedPass = saved.getBo_pass();
		String inputPass = search.getBo_pass();
		return savedPass.equals(inputPass);
	}
	@Override
	public ServiceResult recommend(int bo_no) {
		int result = boardDao.recommend(bo_no);
		return result == 1 ? ServiceResult.OK : ServiceResult.FAIL;
		
	}
	@Override
	public ServiceResult report(int bo_no) {
		int result = boardDao.report(bo_no);
		return result == 1 ? ServiceResult.OK : ServiceResult.FAIL;
	}
	@Override
	public ServiceResult hit(int bo_no) {
		int result = boardDao.hit(bo_no);
		return result == 1 ? ServiceResult.OK : ServiceResult.FAIL;
	}

}
