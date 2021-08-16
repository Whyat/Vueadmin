package com.whyat.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.whyat.entity.SysUser;
import com.whyat.security.CustomUser;
import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@ConfigurationProperties(prefix = "whyat.jwt")
public class JwtUtils {

	private long expire;
	private String secret;
	private String tokenName;

	// 生成jwt
	public String generateToken(CustomUser customUser) {

		Date nowDate = new Date();
		Date expireDate = new Date(nowDate.getTime() + 1000 * expire);
		//token主体信息存入用户的用户名和用户id
		SysUser user = new SysUser();
		user.setUsername(customUser.getUsername());
		user.setId(customUser.getUserId());
		String subjectInfo = JSONUtil.toJsonStr(user);

		return Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setSubject(subjectInfo)
				.setIssuedAt(nowDate)
				.setExpiration(expireDate)// 7天过期
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	// 解析jwt
	public Claims getClaimByToken(String jwt) {
		try {
			return Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(jwt)
					.getBody();
		} catch (Exception e) {
			return null;
		}
	}

	// jwt是否过期
	public boolean isTokenExpired(Claims claims) {
		return claims.getExpiration().before(new Date());
	}

}
