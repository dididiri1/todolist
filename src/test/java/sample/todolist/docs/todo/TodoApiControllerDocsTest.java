package sample.todolist.docs.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import sample.todolist.controller.api.todo.TodoApiController;
import sample.todolist.docs.RestDocsSupport;
import sample.todolist.domain.todo.TodoStatus;
import sample.todolist.dto.member.request.MemberCreateRequest;
import sample.todolist.dto.member.response.MemberCreateResponse;
import sample.todolist.dto.todo.request.TodoCreateRequest;
import sample.todolist.dto.todo.request.TodoStatusUpdateRequest;
import sample.todolist.dto.todo.response.TodoStatusUpdateResponse;
import sample.todolist.service.todo.TodoService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TodoApiControllerDocsTest extends RestDocsSupport {

    private final TodoService todoService = mock(TodoService.class);

    @Override
    protected Object initController() {
        return new TodoApiController(todoService);
    }

    @DisplayName("TODO 할일을 등록 API")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createTodo() throws Exception {
        //given
        TodoCreateRequest request = TodoCreateRequest.builder()
                .memberId(1L)
                .content("할일 내용입니다.")
                .build();

        // expected
        this.mockMvc.perform(post("/api/v1/todos")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("todo-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                        .optional()
                                        .description("회원 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("할일 제목")
                        ),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("데이터")
                        )
                ));
    }

    @DisplayName("TODO 상태를 수정 API")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void updateTodoStatus() throws Exception {
        //given
        Long todoId = 1L;

        TodoStatusUpdateRequest request = TodoStatusUpdateRequest.builder()
                .todoStatus(TodoStatus.IN_PROGRESS)
                .build();

        TodoStatusUpdateResponse result = TodoStatusUpdateResponse.builder()
                .todoId(1L)
                .todoStatus(TodoStatus.IN_PROGRESS)
                .build();

        given(todoService.updateTodoStatus(any(Long.class), any(TodoStatusUpdateRequest.class)))
                .willReturn(result);

        // expected
        this.mockMvc.perform(patch("/api/v1/{todoId}/status", todoId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("todo-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("todoId").description("TODO ID")
                        ),
                        requestParameters (
                                parameterWithName("todoStatus").description("TODO 상태").optional()
                        ),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.todoId").type(JsonFieldType.NUMBER)
                                        .description("TODO ID"),
                                fieldWithPath("data.todoStatus").type(JsonFieldType.STRING)
                                        .description("TODO 상태")
                        )
                ));
    }
}
