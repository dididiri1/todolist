package sample.todolist.controller.api.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.todolist.config.jwt.JwtProperties;
import sample.todolist.dto.ApiResponse;
import sample.todolist.dto.auth.request.AuthRequest;
import sample.todolist.service.member.MemberService;

import java.util.Date;

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final MemberService memberService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthRequest authRequest) {
        System.out.println("authRequest.getUsername() = " + authRequest.getUsername());
        System.out.println("authRequest.getPassword() = " + authRequest.getPassword());
        
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = JWT.create()
                    .withSubject(JwtProperties.SECRET)
                    .withExpiresAt(new Date(System.currentTimeMillis()+(60000*3)))
                    .withClaim("username", authRequest.getUsername())
                    .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));

            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "로그인 성공", token), HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            // Return response entity with status unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed!");
        }
    }
}