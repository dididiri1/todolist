package sample.todolist.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepositoryJap extends JpaRepository<Member, Long> {
    Member findByUsername(String userId);
}
