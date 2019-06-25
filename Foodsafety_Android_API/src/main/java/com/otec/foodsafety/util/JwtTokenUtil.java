package com.otec.foodsafety.util;

import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.impl.realm.RetryLimitHashedCredentialsMatcher;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {
	@Resource(name = "credentialsMatcher")
	private RetryLimitHashedCredentialsMatcher matcher;

	public JwtTokenUtil() {
	}

	private static final String CLAIM_KEY_USERNAME = "sub";
	private static final String CLAIM_KEY_CREATED = "created";

	@Value("${gate.jwt.secret}")
	private String secret;

	@Value("${gate.jwt.expiration}")
	private Long expiration;

	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	public Date getCreatedDateFromToken(String token) {
		Date created;
		try {
			final Claims claims = getClaimsFromToken(token);
			created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	private Claims getClaimsFromToken(String token) throws ExpiredJwtException {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			if (e instanceof ExpiredJwtException) {
				throw (ExpiredJwtException) e;
			} else {
				claims = null;
			}

		}
		return claims;
	}

	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	public Boolean isTokenExpired(String token) {
		try {

			final Date expiration = getExpirationDateFromToken(token);
			if (expiration != null) {
				return expiration.before(new Date());
			} else {
				return true;
			}

		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	private Boolean isCreatedBeforeLastPasswordReset(Date created,
			Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	public String generateToken(SysUser info) {
		Element old = matcher.getEhcache().get(info.getUserName());
		String token = (old == null ? null : old.getObjectValue().toString());
		if (token == null || isTokenExpired(token)) {// 没有或者过期了
			Map<String, Object> claims = new HashMap<String, Object>();
			claims.put(CLAIM_KEY_USERNAME, info.getUserName());
			claims.put(CLAIM_KEY_CREATED, new Date());
			token = generateToken(claims);
			matcher.getEhcache().put(new Element(info.getUserName(), token));

		}
		return token;
	}

	String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims)
				.setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = getCreatedDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& !isTokenExpired(token);
	}

	public String refreshToken(String token) {
		String refreshedToken;
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = generateToken(claims);
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}

	public Boolean validateToken(String token, SysUser info) {
		if (StringUtils.isBlank(token)) {
			return false;
		}
		Object existToken = matcher.getEhcache().get(info.getUserName()) == null ? null
				: matcher.getEhcache().get(info.getUserName()).getObjectValue();
		if (token.equals(existToken)) {
			final String username = getUsernameFromToken(token);
			// final Date created = getCreatedDateFromToken(token);
			return (username.equals(info.getUserName()) && !isTokenExpired(token));
		} else {
			return false;
		}
		// if(!token.startsWith(prefix)){
		// return false;
		// }else {
		// token = token.substring(prefix.length() + 1);
		// }
	}

	public void invalid(String token) {
		String username = this.getUsernameFromToken(token);
		matcher.getEhcache().remove(username);

	}

}
