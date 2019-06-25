package com.otec.foodsafety.web.system;

import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.JwtAuthenticationRequest;
import com.otec.foodsafety.entity.jwt.JwtAuthenticationResponse;
import com.otec.foodsafety.service.system.SysDictService;
import com.otec.foodsafety.util.vo.FrontUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/jwt")
@RestController
public class JwtController {
	@Autowired
	private AuthService authService;
	@Autowired
	private SysDictService sysDictService;

	@RequestMapping(value = "token")
	public ResponseEntity<?> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest) {
		final String token = authService.login(
				authenticationRequest.getUsername(),
				authenticationRequest.getPassword());
		return ResponseEntity.ok(new JwtAuthenticationResponse(token));
	}

	@GetMapping(value = "user")
	public ResponseEntity<?> getUserInfo(String token) {
		FrontUser userInfo = authService.getUserInfo(token);		
		if (userInfo == null)
			return ResponseEntity.status(401).body(false);
		else
			return ResponseEntity.ok(userInfo);
	}

	@GetMapping(value = "hello")
	public void hello() {
		System.out.println("hello");
	}

	@GetMapping(value = "loadStaticData")
	public ResponseEntity<?> loadStaticData() {
		return ResponseEntity.ok(sysDictService.loadStaticData());
	}

	@RequestMapping(value = "invalid", method = RequestMethod.POST)
	public ResponseEntity<?> invalid(@RequestHeader("access-token") String token) {
		authService.invalid(token);
		return ResponseEntity.ok(true);
	}

}
