package sample.todolist.docs.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import sample.todolist.controller.api.member.MemberApiController;
import sample.todolist.docs.RestDocsSupport;
import sample.todolist.domain.todo.TodoStatus;
import sample.todolist.dto.member.request.MemberCreateRequest;
import sample.todolist.dto.member.response.MemberCreateResponse;
import sample.todolist.dto.todo.response.TodoResponse;
import sample.todolist.service.member.MemberService;

import java.time.LocalDateTime;
import java.util.List;

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

public class MemberApiControllerDocsTest extends RestDocsSupport {

    private final MemberService memberService = mock(MemberService.class);

    @Override
    protected Object initController() {
        return new MemberApiController(memberService);
    }


    @DisplayName("신규 회원을 등록한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void createMember() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .username("test")
                .password("1234")
                .nickname("홍길동")
                .build();

        given(memberService.createMember(any(MemberCreateRequest.class)))
                .willReturn(MemberCreateResponse.builder()
                        .memberId(1L)
                        .username("test")
                        .nickname("홍길동")
                        .build());

        // expected
        this.mockMvc.perform(post("/api/v1/members/new")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("member-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("사용자명"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("비밀번호"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("닉네임")
                        ),
                        responseFields (
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터")
                        ).andWithPrefix("data.",
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .description("사용자명"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("닉네임")
                        )
                ));

    }

    @DisplayName("회원을 탈퇴한다.")
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void deleteMember() throws Exception {
        //given
        Long memberId = 1L;

        // expected
        this.mockMvc.perform(delete("/api/v1/members/{memberId}", memberId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
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

    @DisplayName("TODO 목록 조회 API")
    @Test
    void getMemberTodoList() throws Exception {
        // given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        List<TodoResponse> content = List.of(
                TodoResponse.builder()
                        .todoId(1L)
                        .content("오후에 헬스장 가기 2")
                        .todoStatus(TodoStatus.TODO)
                        .createDateTime(LocalDateTime.of(2023, 12, 16, 10, 0, 0))
                        .build(),
                TodoResponse.builder()
                        .todoId(1L)
                        .content("오후에 헬스장 가기 1")
                        .todoStatus(TodoStatus.TODO)
                        .createDateTime(LocalDateTime.of(2023, 12, 15, 9, 20, 0))
                        .build()
        );
        Page<TodoResponse> result = new PageImpl<>(content, pageable, 2);
        given(memberService.getMemberDodoList(any(Long.class), any(Pageable.class)))
                .willReturn(result);

        // when // then
        this.mockMvc.perform(get("/api/v1/members/{memberId}/todos", memberId)
                        .param("page", String.valueOf(pageable.getOffset()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-todo-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        requestParameters (
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("게시물 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터"),
                                fieldWithPath("data.content[].todoId").type(JsonFieldType.NUMBER).description("TODO ID"),
                                fieldWithPath("data.content[].content").type(JsonFieldType.STRING).description("할일 내용"),
                                fieldWithPath("data.content[].todoStatus").type(JsonFieldType.STRING).description("TODO 상태"),
                                fieldWithPath("data.content[].createDateTime").type(JsonFieldType.STRING).description("등록일"),

                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 안됬는지 여부"),
                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 됬는지 여부"),
                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("데이터가 비었는지 여부"),
                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("데이터 개수"),
                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("몇번째 데이터인지"),
                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 정보를 포함하는지 여부"),
                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징 정보를 안포함하는지 여부"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 안됬는지 여부"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 됬는지 여부"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("데이터가 비었는지 여부"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("요청 페이지에서 조회된 데이터 개수"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 개수"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("테이블 총 데이터 개수"),
                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지인지 여부"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫번째 페이지인지 여부"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지당 조회할 데이터 개수"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("데이터가 비었는지 여부")
                        )
                ));
    }
}
