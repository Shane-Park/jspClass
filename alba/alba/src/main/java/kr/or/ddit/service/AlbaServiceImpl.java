package kr.or.ddit.service;

import java.util.List;

import kr.or.ddit.dao.AlbaDAO;
import kr.or.ddit.dao.AlbaDAOImpl;
import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.vo.AlbaVO;
import kr.or.ddit.vo.PagingVO;

public class AlbaServiceImpl implements AlbaService {
	private AlbaDAO dao = AlbaDAOImpl.getInstance();
	private static AlbaServiceImpl self;
	
	private AlbaServiceImpl() {}
	
	public static AlbaServiceImpl getInstance() {
		if(self == null) self = new AlbaServiceImpl();
		return self;
	}

	@Override
	public List<AlbaVO> retrieveAlbaList(PagingVO<AlbaVO> pagingVO) {
		return dao.selectAlbaList(pagingVO);
	}

	@Override
	public AlbaVO retrieveAlba(String al_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceResult insertAlba(AlbaVO alba) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceResult updateAlba(AlbaVO alba) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int selectTotalRecord(PagingVO<AlbaVO> pagingVO) {
		// TODO Auto-generated method stub
		return 0;
	}

}