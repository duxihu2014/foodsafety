package com.otec.foodsafety.service.impl.realm;


import net.sf.ehcache.Ehcache;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;

public class RetryLimitHashedCredentialsMatcher extends
		HashedCredentialsMatcher { 
	
	private Ehcache passwordRetryCache;
	 
	public RetryLimitHashedCredentialsMatcher(EhCacheManager cacheManager) {
		super();  
		passwordRetryCache = cacheManager.getCacheManager().getCache("passwordRetryCache");
	}
	 
	/*@Override
	public boolean doCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info) {
		String username = (String) token.getPrincipal();
		// retry count + 1
		Element element = passwordRetryCache.get(username);
		if (element == null) {
			element = new Element(username, new AtomicInteger(0));
			passwordRetryCache.put(element);
		}
		AtomicInteger retryCount = (AtomicInteger) element.getObjectValue();
		if (retryCount.incrementAndGet() > 5) {
			// if retry count > 5 throw
			throw new ExcessiveAttemptsException();
		}

		boolean matches = super.doCredentialsMatch(token, info);
		if (matches) {
			// clear retry count
			passwordRetryCache.remove(username);
		}
		return matches;
	}*/
	
	
	public Ehcache getEhcache(){
		return passwordRetryCache;
	}
}
