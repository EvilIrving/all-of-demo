package com.ygkj.utils;


import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.jwt.TokenSettings;

/**
 * @ClassName: StaticContextInitializer
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/26 10:07
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/26 10:07
 * @Version: 0.0.1
 */
//@Component
public class StaticInitializerUtil {
    private TokenSettings tokenSettings;

    public StaticInitializerUtil(TokenSettings tokenSettings) {
        JwtTokenUtil.setTokenSettings(tokenSettings);
    }
}
