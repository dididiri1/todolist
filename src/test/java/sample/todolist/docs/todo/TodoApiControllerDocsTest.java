package sample.todolist.docs.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import sample.todolist.controller.api.todo.TodoApiController;
import sample.todolist.docs.RestDocsSupport;
import sample.todolist.domain.todo.TodoStatus;
import sample.todolist.dto.EnumResponse;
import sample.todolist.dto.todo.request.TodoCreateRequest;
import sample.todolist.dto.todo.request.TodoStatusUpdateRequest;
import sample.todolist.dto.todo.response.TodoStatusUpdateResponse;
import sample.todolist.service.todo.TodoService;
import sample.todolist.util.EnumMapperType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
                        .header("Authorization", "Bearer {accessToken}")
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

    @DisplayName("TODO 상태 리스트 조회 API")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void getTodoStatusList() throws Exception {
        //given
        given(todoService.getTodoStatus())
                .willReturn(getTodoStatus());

        // expected
        this.mockMvc.perform(get("/api/v1/todos/categories")
                        .header("Authorization", "Bearer {accessToken}")
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("todo-categories",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("데이터"),
                                fieldWithPath("data[].code").type(JsonFieldType.STRING).description("code"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("title")
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
        this.mockMvc.perform(patch("/api/v1/todos/{todoId}/status", todoId)
                        .header("Authorization", "Bearer {accessToken}")
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

    private List<EnumResponse> getTodoStatus() {
        Class<? extends EnumMapperType> e = TodoStatus.class;
        List<EnumResponse> response = Arrays.stream(e.getEnumConstants()).map(EnumResponse::new).collect(Collectors.toList());
        return response;
    }
}
