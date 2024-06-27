package com.inaction.springsecurity.service;

import java.security.SecureRandom;

public final class GenerateCodeUtil {
    private GenerateCodeUtil() {
    }

    public static String generateCode() {
        String code;
        try{
            SecureRandom random = SecureRandom.getInstanceStrong(); // 임의의 INT값을 생성
            int c = random.nextInt(9000) + 1000;
            code = String.valueOf(c);
        } catch (Exception e){
            return null;
        }
        return code;
    }

}
