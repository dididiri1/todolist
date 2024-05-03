package sample.todolist.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sample.todolist.domain.member.Member;
import sample.todolist.domain.member.MemberRepositoryJpa;


@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member memberEntity = memberRepositoryJpa.findByUsername(userId);
        if(memberEntity == null){
            return null;
        } else {
            return new PrincipalDetails(memberEntity);
        }

    }
}
