package sample.todolist;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import sample.todolist.config.CorsConfig;
import sample.todolist.controller.api.member.MemberApiController;
import sample.todolist.controller.api.todo.TodoApiController;
import sample.todolist.service.member.MemberService;
import sample.todolist.service.todo.TodoService;


@WebMvcTest(controllers = {
        MemberApiController.class,
        TodoApiController.class
})
@Import(CorsConfig.class)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected TodoService todoService;

}
