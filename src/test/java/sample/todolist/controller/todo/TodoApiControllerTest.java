package sample.todolist.controller.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import sample.todolist.ControllerTestSupport;
import sample.todolist.dto.member.request.MemberCreateRequest;
import sample.todolist.dto.todo.request.TodoCreateRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TodoApiControllerTest extends ControllerTestSupport {

    @DisplayName("TODO 할일을 등록한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createTodo() throws Exception {
        //given
        TodoCreateRequest request = TodoCreateRequest.builder()
                .memberId(1L)
                .content("할일 내용입니다.")
                .build();

        //when //then
        mockMvc.perform(post("/api/v1/todos")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
