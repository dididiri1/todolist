package sample.todolist.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import sample.todolist.dto.Role;


import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String nickname;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(Long id, String username, String password, String nickname, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }
}
