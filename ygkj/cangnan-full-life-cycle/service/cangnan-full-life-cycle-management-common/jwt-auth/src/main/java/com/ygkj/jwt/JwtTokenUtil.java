package com.ygkj.jwt;

import com.ygkj.jwt.constants.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName: JwtTokenUtil
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/6 23:49
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/6 23:49
 * @Version: 0.0.1
 */
@Slf4j
public class JwtTokenUtil {

    private static String secretKey;
    private static Duration accessTokenExpireTime;
    private static Duration refreshTokenExpireTime;
    private static Duration refreshTokenExpireAppTime;
    private static String  issuer;

    private static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public static void setTokenSettings(TokenSettings tokenSettings){
        secretKey=tokenSettings.getSecretKey();
        accessTokenExpireTime=tokenSettings.getAccessTokenExpireTime() == null ?
                Duration.ofHours(8l):tokenSettings.getAccessTokenExpireTime();
        refreshTokenExpireTime=tokenSettings.getRefreshTokenExpireTime() == null?Duration.ofMinutes(1l):tokenSettings.getAccessTokenExpireTime();
        refreshTokenExpireAppTime=tokenSettings.getRefreshTokenExpireAppTime();
        issuer=tokenSettings.getIssuer();
    }

    public static boolean compareAndSet(){
        return atomicBoolean.compareAndSet(false,true);
    }
    /**
     * 生成 access_token
     * @Author:      Huang.zh
     * @CreateDate:  2019/9/26 10:22
     * @UpdateUser:
     * @UpdateDate:  2019/9/26 10:22
     * @Version:     0.0.1
     * @param subject
     * @param claims
     * @return       java.lang.String
     * @throws
     */
    public static String getAccessToken(String subject,Map<String,Object> claims){

        return generateToken(issuer,subject,claims,accessTokenExpireTime.toMillis(),secretKey);
    }
    /**
     * 生产 App端 refresh_token
     * @Author:      Huang.zh
     * @CreateDate:  2019/9/26 10:24
     * @UpdateUser:
     * @UpdateDate:  2019/9/26 10:24
     * @Version:     0.0.1
     * @param subject
     * @param claims
     * @return       java.lang.String
     * @throws
     */
    public static String getRefreshAppToken(String subject,Map<String,Object> claims){
        return generateToken(issuer,subject,claims,refreshTokenExpireAppTime.toMillis(),secretKey);
    }

    /**
     * 生产 PC refresh_token
     * @Author:      Huang.zh
     * @CreateDate:  2019/9/26 10:24
     * @UpdateUser:
     * @UpdateDate:  2019/9/26 10:24
     * @Version:     0.0.1
     * @param subject
     * @param claims
     * @return       java.lang.String
     * @throws
     */
    public static String getRefreshToken(String subject,Map<String,Object> claims){
        return generateToken(issuer,subject,claims,refreshTokenExpireTime.toMillis(),secretKey);
    }
    /**
     * 签发token
     * @Author:      Huang.zh
     * @CreateDate:  2019/9/7 20:42
     * @UpdateUser:
     * @UpdateDate:  2019/9/7 20:42
     * @Version:     0.0.1
     * @param issuer 签发人
     * @param subject 代表这个JWT的主体，即它的所有人 一般是用户id
     * @param claims 存储在JWT里面的信息 一般放些用户的权限/角色信息
     * @param ttlMillis 有效时间(毫秒)
     * @return       java.lang.String
     * @throws
     */
    public static String generateToken(String issuer, String subject,Map<String, Object> claims, long ttlMillis,String secret) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] signingKey = DatatypeConverter.parseBase64Binary(secret);

        JwtBuilder builder = Jwts.builder();
        if(null!=claims){
            builder.setClaims(claims);
        }
        if (!StringUtils.isEmpty(subject)) {
            builder.setSubject(subject);
        }
        if (!StringUtils.isEmpty(issuer)) {
            builder.setIssuer(issuer);
        }
        builder.setIssuedAt(now);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        builder.signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }
    /**
     * 获取用户id
     * @Author:      Huang.zh
     * @CreateDate:  2019/9/7 17:29
     * @UpdateUser:
     * @UpdateDate:  2019/9/7 17:29
     * @Version:     0.0.1
     * @param token
     * @return       java.lang.String
     * @throws
     */
    public static String getUserId(String token){
        String userId=null;
        try {
            Claims claims = getClaimsFromToken(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            log.error("error={}",e);
            return StringUtils.EMPTY;
        }
        return userId;
    }

    public static String getDeptId(String token){
        String deptId=null;
        try {
            Claims claims = getClaimsFromToken(token);
            deptId = (String)claims.get(Constants.DEPT_CODE_KEY);
        } catch (Exception e) {
            log.error("error={}",e);
        }
        return deptId;
    }
    public static String getDepartmentName(String token){
        String departmentName=null;
        try {
            Claims claims = getClaimsFromToken(token);
            departmentName = (String)claims.get(Constants.DEPARTMENT_NAME_KEY);
        } catch (Exception e) {
            log.error("error={}",e);
        }
        return departmentName;
    }

    /**
     * 获取用户名
     * @Author:      Huang.zh
     * @CreateDate:  2019/9/23 15:39
     * @UpdateUser:
     * @UpdateDate:  2019/9/23 15:39
     * @Version:     0.0.1
     * @param token
     * @return       java.lang.String
     * @throws
     */
    public static String getUserName(String token){

        String username=null;
        try {
            Claims claims = getClaimsFromToken(token);
            username = (String) claims .get(Constants.JWT_USER_NAME);
        } catch (Exception e) {
            log.error("error={}",e);
        }
        return username;
    }

    /**
     * @Author Huang.zh
     * @Description 用户类型 1水利局用户 2普通企业用户
     * @Date 2020/8/24 14:46
     */
    public static String getUserType(String token){

        String type=null;
        try {
            Claims claims = getClaimsFromToken(token);
            type = (String) claims .get(Constants.JWT_USER_TYPE);
        } catch (Exception e) {
            log.error("error={}",e);
        }
        return type;
    }

    /**
     * @Author Huang.zh
     * @Description 当前用户是否管理员
     * @Date 2020/8/24 14:46
     */
    public static boolean getUserAdmin(String token){
        boolean isAdmin=false;
        try {
            Claims claims = getClaimsFromToken(token);
            isAdmin = (boolean) claims .get(Constants.JWT_USER_ADMIN);
        } catch (Exception e) {
            log.error("error={}",e);
        }
        return isAdmin;
    }

    /**
     * 从令牌中获取数据声明
     * @Author:      Huang.zh
     * @CreateDate:  2019/9/7 21:21
     * @UpdateUser:
     * @UpdateDate:  2019/9/7 21:21
     * @Version:     0.0.1
     * @param token
     * @return       io.jsonwebtoken.Claims
     * @throws
     */
    public static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            claims = null;
        }
        return claims;
    }
    /**
     * 校验令牌
     * @Author:      Huang.zh
     * @CreateDate:  2019/9/7 22:15
     * @UpdateUser:
     * @UpdateDate:  2019/9/7 22:15
     * @Version:     0.0.1
     * @param token
     * @return       java.lang.Boolean
     * @throws
     */
    public static Boolean validateToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return (null!=claimsFromToken && !isTokenExpired(token));
    }
    /**
     * 验证token 是否过期
     * @Author:      Huang.zh
     * @CreateDate:  2019/9/7 21:27
     * @UpdateUser:
     * @UpdateDate:  2019/9/7 21:27
     * @Version:     0.0.1
     * @param token
     * @param secretKey
     * @return       java.lang.Boolean
     * @throws
     */
    public static Boolean isTokenExpired(String token) {

        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("error={}",e);
            return true;
        }
    }
    /**
     * 刷新token
     * @Author:      Huang.zh
     * @CreateDate:  2019/9/7 22:14
     * @UpdateUser:
     * @UpdateDate:  2019/9/7 22:14
     * @Version:     0.0.1
     * @param refreshToken
     * @param claims 主动去刷新的时候 改变JWT payload 内的信息
     * @return       java.lang.String
     * @throws
     */
    public static String refreshToken(String refreshToken,Map<String, Object> claims) {
        String refreshedToken;
        try {
            Claims parserclaims = getClaimsFromToken(refreshToken);
            /**
             * 刷新token的时候如果为空说明原先的 用户信息不变 所以就引用上个token里的内容
             */
            if(null==claims){
                claims=parserclaims;
            }
            refreshedToken = generateToken(parserclaims.getIssuer(),parserclaims.getSubject(),claims,accessTokenExpireTime.toMillis(),secretKey);
        } catch (Exception e) {
            refreshedToken = null;
            log.error("error={}",e);
        }
        return refreshedToken;
    }
    /**
     * 获取token的剩余过期时间
     * @Author:      Huang.zh
     * @CreateDate:  2019/9/7 22:19
     * @UpdateUser:
     * @UpdateDate:  2019/9/7 22:19
     * @Version:     0.0.1
     * @param token
     * @param secretKey
     * @return       long
     * @throws
     */
    public static long getRemainingTime(String token){
        long result=0;
        try {
            long nowMillis = System.currentTimeMillis();
            result= getClaimsFromToken(token).getExpiration().getTime()-nowMillis;
        } catch (Exception e) {
            log.error("error={}",e);
        }
        return result;
    }

    public static Optional<String> getCurrentUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        String userId = null;
        if (!StringUtils.isEmpty(token)) {
            userId = JwtTokenUtil.getUserId(token);
        }
        return Optional.ofNullable(userId);
    }

    public static Optional<String> getCurrentDepartmentName(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        String name = null;
        if (!StringUtils.isEmpty(token)) {
            name = JwtTokenUtil.getDepartmentName(token);
        }
        return Optional.ofNullable(name);
    }

    public static Optional<String> getCurrentUserName(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        String userId = null;
        if (!StringUtils.isEmpty(token)) {
            userId = getUserName(token);
        }
        return Optional.ofNullable(userId);
    }

    public static Optional<String> getCurrentUserDepartmentId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        String userId = null;
        if (!StringUtils.isEmpty(token)) {
            userId = getDeptId(token);
        }
        return Optional.ofNullable(userId);
    }

}
