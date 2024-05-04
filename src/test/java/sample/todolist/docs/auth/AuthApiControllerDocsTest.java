package sample.todolist.docs.auth;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import sample.todolist.controller.api.auth.AuthApiController;
import sample.todolist.docs.RestDocsSupport;
import sample.todolist.dto.auth.request.AuthLoginRequest;
import sample.todolist.dto.jwt.JwtResponse;
import sample.todolist.dto.member.request.MemberCreateRequest;
import sample.todolist.dto.member.response.MemberCreateResponse;
import sample.todolist.service.jwt.JwtService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthApiControllerDocsTest extends RestDocsSupport {


    private final JwtService jwtService = mock(JwtService.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

    @Override
    protected Object initController() {
        return new AuthApiController(jwtService, authenticationManager);
    }

    @DisplayName("로그인 API")
    @Test
    void authLogin() throws Exception {
        //given
        AuthLoginRequest request = AuthLoginRequest.builder()
                .username("test")
                .password("1234")
                .build();

        String testToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJrYW5nbWluIiwiZXhwIjoxNzE1Njc2NzI0LCJ1c2VybmFtZSI6InRlc3QifQ." +
                "Ti0rMoxJmC8mXr8NfYytU2uEiFt58nBnNs0GkgSCbtLyVhEV1zPT79hP6zK5GU_V42juLHzyXXWZOezhQ4cxpg";

        Authentication authentication = mock(Authentication.class);
        given(authenticationManager.authenticate(any(Authentication.class))).willReturn(authentication);
        given(authenticationManager.authenticate(any(Authentication.class))).willReturn(authentication);
        given(jwtService.createToken(anyString())).willReturn(
                JwtResponse.builder()
                        .accessToken(testToken)
                        .build()
        );

        // expected
        this.mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("사용자명"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("비밀번호")
                        ),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터")
                        ).andWithPrefix("data.",
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                        .description("JWT 토큰")
                        )
                ));

    }
}
