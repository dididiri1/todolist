package sample.todolist.domain.todo;

import lombok.*;
import sample.todolist.domain.BaseEntity;
import sample.todolist.domain.member.Member;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private TodoStatus todoStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Todo(Long id, String content, TodoStatus todoStatus, Member member) {
        this.id = id;
        this.content = content;
        this.todoStatus = todoStatus;
        this.member = member;
    }
}
