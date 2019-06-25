package com.otec.foodsafety.service.impl.realm;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class GenPassWord {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testGeneratePassword();
	}


	public static void testGeneratePassword() {
        String algorithmName = "md5";
        String password = "123";
		// 随机数加密盐
        String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
        int hashIterations = 2;
        SimpleHash hash = new SimpleHash(algorithmName, password, ByteSource.Util.bytes(salt2), hashIterations);
        String encodedPassword = hash.toHex();
        System.out.println(salt2);
        System.out.println(encodedPassword);
    }
}
