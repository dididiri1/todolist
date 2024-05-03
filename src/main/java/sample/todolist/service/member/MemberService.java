package sample.todolist.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.todolist.domain.member.Member;
import sample.todolist.domain.member.MemberRepositoryJpa;
import sample.todolist.dto.auth.request.AuthRequest;
import sample.todolist.dto.member.request.MemberCreateRequest;
import sample.todolist.dto.member.response.MemberCreateResponse;
import sample.todolist.handler.ex.validationException;

import javax.validation.ValidationException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepositoryJpa memberRepositoryJpa;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    private void validateDuplicateUsername(String username) {
        Member memberEntity = memberRepositoryJpa.findByUsername(username);
        if (memberEntity != null) {
            throw new validationException("이미 사용 중인 유저명입니다.");
        }
    }

    @Transactional
    public MemberCreateResponse createMember(MemberCreateRequest request) {
        validateDuplicateUsername(request.getUsername());
        Member member = request.toEntity(bCryptPasswordEncoder.encode(request.getPassword()));
        Member memberEntity = memberRepositoryJpa.save(member);

        return MemberCreateResponse.of(memberEntity);
    }


}
