package sample.todolist.dto.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtResponse {

    private String accessToken;

    @Builder
    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;
    }


}
