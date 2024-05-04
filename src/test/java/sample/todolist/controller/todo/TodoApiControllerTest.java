package sample.todolist.controller.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import sample.todolist.ControllerTestSupport;
import sample.todolist.domain.todo.TodoStatus;
import sample.todolist.dto.todo.request.TodoCreateRequest;
import sample.todolist.dto.todo.request.TodoStatusUpdateRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @DisplayName("TODO 상태를 수정한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void updateTodoStatus() throws Exception {
        //given
        Long todoId = 1L;

        TodoStatusUpdateRequest request = TodoStatusUpdateRequest.builder()
                .todoStatus(TodoStatus.IN_PROGRESS)
                .build();

        //when //then
        mockMvc.perform(patch("/api/v1/{todoId}/status", todoId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("TODO 상태를 수정할때 상태 타입은 필수 값이다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void updateTodoStatusWithoutStatus() throws Exception {
        //given
        Long todoId = 1L;

        TodoStatusUpdateRequest request = TodoStatusUpdateRequest.builder()
                .todoStatus(null)
                .build();

        //when //then
        mockMvc.perform(patch("/api/v1/{todoId}/status", todoId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
