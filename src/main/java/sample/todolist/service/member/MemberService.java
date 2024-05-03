package sample.todolist.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.todolist.domain.member.Member;
import sample.todolist.domain.member.MemberRepositoryJap;
import sample.todolist.dto.member.request.MemberCreateRequest;

import javax.validation.ValidationException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepositoryJap memberRepositoryJap;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    private void validateDuplicateUsername(String username) {
        Member memberEntity = memberRepositoryJap.findByUsername(username);
        if (memberEntity != null) {
            throw new ValidationException("이미 사용 중인 유저명입니다.");
        }
    }

    @Transactional
    public void createUser(MemberCreateRequest request) {
        validateDuplicateUsername(request.getUsername());
        Member member = request.toEntity(bCryptPasswordEncoder.encode(request.getPassword()));
        memberRepositoryJap.save(member);
    }
}
