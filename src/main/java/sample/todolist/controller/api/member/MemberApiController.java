package sample.todolist.controller.api.member;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.todolist.dto.ApiResponse;
import sample.todolist.dto.member.request.MemberCreateRequest;
import sample.todolist.dto.member.response.MemberCreateResponse;
import sample.todolist.dto.todo.response.TodoResponse;
import sample.todolist.service.member.MemberService;


import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * @Method: createMember
     * @Description: 회원 등록
     */
    @PostMapping("/api/v1/members/new")
    public ResponseEntity<?> createMember(@RequestBody @Valid MemberCreateRequest request) {
        MemberCreateResponse response = memberService.createMember(request);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "회원 등록 성공", response), HttpStatus.CREATED);
    }

    /**
     * @Method: deleteMember
     * @Description: 회원 탈퇴
     */
    @DeleteMapping("/api/v1/members/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "회원 탈퇴 성공", null), HttpStatus.OK);
    }

    /**
     * @Method: getMemberDodoList
     * @Description: 회원 투두 목록 조회
     */
    @GetMapping("/api/v1/members/{memberId}/todos")
    public ResponseEntity<?> getMemberDodoList(@PathVariable Long memberId, Pageable pageable) {
        Page<TodoResponse> response = memberService.getMemberDodoList(memberId, pageable);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "TODO 목록 조회 성공", response), HttpStatus.OK);
    }
}
