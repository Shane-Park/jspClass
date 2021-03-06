package kr.or.ddit.example.service;

import javax.annotation.Resource;
import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import kr.or.ddit.example.dao.IExampleDAO;

@Service
@Scope("prototype")
public class ExampleServiceImpl implements IExampleService {
/*
	 1. new 키워드로 인스턴스 직접 생성
	private IExampleDAO dao = new ExampleDAO_Oracle();
	private IExampleDAO dao = new ExampleDAO_MySql();
	
	2. Factory ObjectPattern
	private IExampleDAO dao = ExampleDAOFactory.getExampleDAO();
	
	 
	3. Strategy Pattern - > Dependency Injection
  	: 생성자 주입, setter 주입, 전략의 주입자가 필요함.
*/
	
	
	private IExampleDAO dao;
	
	public ExampleServiceImpl() {
		super();
		System.out.println(getClass().getSimpleName()+" 객체 생성-기본 생성자");
		// TODO Auto-generated constructor stub
	}

	public ExampleServiceImpl(IExampleDAO dao) {
		super();
		this.dao = dao;
		System.out.println(getClass().getSimpleName()+" 객체 생성-argument 있는 생성자");
	}

//	@Injects
	@Resource(name="exampleDAO_MySql")
	@Required
	public void setDao(IExampleDAO dao) {
		this.dao = dao;
		System.out.println(getClass().getSimpleName()+"에서 setter inject 받음.");
	}
	
	public void init() {
		System.out.println(getClass().getSimpleName()+"객체 초기화");
	}
	
	public void destroy() {
		System.out.println(getClass().getSimpleName()+"객체 소멸");
	}

	@Override
	public String readData(String pk) {
		String rawData = dao.selectData(pk);
		String info = rawData + "를 가공한 information";
//		if(1==1)
//				throw new RuntimeErrorException(null, "강제 발생 예외");
		return info;
	}

}
