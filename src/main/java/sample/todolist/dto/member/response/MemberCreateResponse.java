package sample.todolist.dto.member.response;

import lombok.Builder;
import lombok.Getter;
import sample.todolist.domain.member.Member;

@Getter
public class MemberCreateResponse {

    private Long memberId;

    private String username;

    private String nickname;

    @Builder
    public MemberCreateResponse(Long memberId, String username, String nickname) {
        this.memberId = memberId;
        this.username = username;
        this.nickname = nickname;
    }

    public static MemberCreateResponse of(Member member) {
        return MemberCreateResponse.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();
    }
}
