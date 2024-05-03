package sample.todolist.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import sample.todolist.config.auth.PrincipalDetails;
import sample.todolist.domain.member.Member;
import sample.todolist.domain.member.MemberRepositoryJap;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private MemberRepositoryJap memberRepositoryJap;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepositoryJap memberRepositoryJap) {
        super(authenticationManager);
        this.memberRepositoryJap = memberRepositoryJap;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String jwtHeader = request.getHeader("Authorization");
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")){
            chain.doFilter(request,response);
            return;
        }

        String jwtToken = request.getHeader("Authorization").replace("Bearer ","");
        String username = null;
        try{
            username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build()
                    .verify(jwtToken)
                    .getClaim("userId").asString();
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            request.setAttribute("exception", 401);
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            request.setAttribute("exception", 401);
        }
        if(username != null){
            Member memberEntity = memberRepositoryJap.findByUsername(username);
            PrincipalDetails principalDetails = new PrincipalDetails(memberEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
