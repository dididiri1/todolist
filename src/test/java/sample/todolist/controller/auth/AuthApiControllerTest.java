package sample.todolist.controller.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import sample.todolist.ControllerTestSupport;
import sample.todolist.dto.auth.request.AuthLoginRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthApiControllerTest extends ControllerTestSupport {

    @DisplayName("로그인 API")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void loginTest() throws Exception {
        //given
        AuthLoginRequest request = AuthLoginRequest.builder()
                .username("testUser")
                .password("1234")
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
