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
import sample.todolist.domain.member.MemberRepositoryJpa;
import sample.todolist.handler.ex.JwtException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private MemberRepositoryJpa memberRepositoryJpa;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepositoryJpa memberRepositoryJpa) {
        super(authenticationManager);
        this.memberRepositoryJpa = memberRepositoryJpa;
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
                    .getClaim("username").asString();
        } catch (TokenExpiredException e) {
            throw new JwtException("토큰이 만료되었습니다.");
        } catch (JWTVerificationException e) {
            throw new JwtException(e.getMessage());
        }
        if(username != null){
            Member memberEntity = memberRepositoryJpa.findByUsername(username);
            PrincipalDetails principalDetails = new PrincipalDetails(memberEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
