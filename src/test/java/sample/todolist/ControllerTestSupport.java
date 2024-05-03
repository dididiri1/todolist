package sample.todolist;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import sample.todolist.config.CorsConfig;
import sample.todolist.controller.api.member.MemberApiController;
import sample.todolist.service.member.MemberService;


@WebMvcTest(controllers = {
        MemberApiController.class
})
@Import(CorsConfig.class)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MemberService memberService;

}
