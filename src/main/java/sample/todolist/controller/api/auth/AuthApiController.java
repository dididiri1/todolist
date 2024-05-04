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
import sample.todolist.dto.auth.request.AuthLoginRequest;
import sample.todolist.dto.jwt.JwtResponse;
import sample.todolist.service.jwt.JwtService;
import sample.todolist.service.member.MemberService;

import javax.validation.Valid;
import java.util.Date;

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthLoginRequest authLoginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authLoginRequest.getUsername(), authLoginRequest.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            JwtResponse response = jwtService.createToken(authLoginRequest.getUsername());

            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "로그인 성공", response), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "로그인 실패", null), HttpStatus.UNAUTHORIZED);
        }
    }
}