package sample.todolist.controller.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import sample.todolist.ControllerTestSupport;
import sample.todolist.dto.member.request.MemberCreateRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MemberApiControllerTest extends ControllerTestSupport {


    @DisplayName("신규 회원을 등록한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createMember() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .username("testUser")
                .password("1234")
                .nickname("홍길동")
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/members/new")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("신규 회원 등록할 때 유저명은 필수 값이다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createUserWithoutUsername() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .password("1234")
                .nickname("홍길동")
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/members/new")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())

                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("유저명은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 회원 등록할 때 패스워드는 필수 값이다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createUserWithoutPassword() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .username("test")
                .nickname("홍길동")
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/members/new")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("패스워드는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 회원 등록할 때 닉네임은 필수 값이다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createUserWithoutNickname() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .username("test")
                .password("1234")
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/members/new")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("닉네임은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("회원을 탈퇴한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void deleteMember() throws Exception {
        //given
        Long memberId = 1L;

        //when //then
        mockMvc.perform(delete("/api/v1/members/{memberId}", memberId)
                        .contentType(APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("TODO 목록을 조회 한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getUsers() throws Exception {
        //given
        Long memberId = 1L;
        int page = 0;
        int size = 3;

        // when // then
        mockMvc.perform(get("/api/v1/members/{memberId}/todos", memberId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());

    }
}
