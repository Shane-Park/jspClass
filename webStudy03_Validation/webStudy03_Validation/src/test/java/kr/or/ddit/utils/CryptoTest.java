package kr.or.ddit.utils;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptoTest {
	private static final Logger logger = LoggerFactory.getLogger(CryptoTest.class);

	@Test
	public void test() throws NoSuchAlgorithmException {
		String str = "java";
		String encoded = CryptoUtil.sha512(str);
		logger.debug("인코딩된 값 : {}", encoded);
		
	}

}
