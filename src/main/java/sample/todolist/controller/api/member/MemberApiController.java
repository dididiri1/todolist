package sample.todolist.controller.api.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.todolist.dto.ApiResponse;
import sample.todolist.dto.member.request.MemberCreateRequest;
import sample.todolist.dto.member.response.MemberCreateResponse;
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

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "회원 등록 성공", response), HttpStatus.CREATED);
    }
}
