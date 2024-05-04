package sample.todolist.domain.todo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum TodoStatus {

    TODO("할 일"), IN_PROGRESS("진행중"), DONE("완료"), PENDING("대기");

    private final String text;

    public static boolean containsStockType(TodoStatus type) {
        return List.of(TODO, IN_PROGRESS, DONE, PENDING).contains(type);
    }

}


