package sample.todolist.domain.todo;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import sample.todolist.dto.todo.response.QTodoResponse;
import sample.todolist.dto.todo.response.TodoResponse;

import javax.persistence.EntityManager;
import java.util.List;

import static sample.todolist.domain.member.QMember.member;
import static sample.todolist.domain.todo.QTodo.todo;


@Repository
public class TodoQueryRepository {

    private final JPAQueryFactory queryFactory;

    public TodoQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<TodoResponse> searchPageTodos(Long memberId, Pageable pageable) {
        List<TodoResponse> content = queryFactory
                .select(new QTodoResponse(
                        todo.id,
                        todo.content,
                        todo.todoStatus,
                        todo.createDateTime))
                .from(todo)
                .join(todo.member, member)
                .where(todo.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(todo.id.desc())
                .fetch();

        JPAQuery<Todo> countQuery = queryFactory
                .selectFrom(todo)
                .join(todo.member, member).fetchJoin()
                .where(todo.member.id.eq(memberId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);

    }
}
