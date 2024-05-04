package sample.todolist.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.todolist.domain.member.Member;
import sample.todolist.domain.member.MemberRepositoryJpa;
import sample.todolist.domain.todo.TodoQueryRepository;
import sample.todolist.dto.member.request.MemberCreateRequest;
import sample.todolist.dto.member.response.MemberCreateResponse;
import sample.todolist.dto.todo.response.TodoResponse;
import sample.todolist.handler.ex.validationException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepositoryJpa memberRepositoryJpa;

    private final TodoQueryRepository todoQueryRepository;

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


    @Transactional
    public void deleteMember(Long memberId) {
        Member member =validateMemberId(memberId);
        memberRepositoryJpa.delete(member);
    }

    private Member validateMemberId(Long memberId) {
        Member findMember = memberRepositoryJpa.findById(memberId).orElseThrow(() -> {
            throw new validationException("해당 유저를 찾을수 없습니다.");
        });

        return findMember;
    }

    public Page<TodoResponse> getMemberDodoList(Long memberId, Pageable pageable) {
        return todoQueryRepository.searchPageTodos(memberId, pageable);
    }
}
