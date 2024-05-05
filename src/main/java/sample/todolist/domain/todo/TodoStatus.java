package sample.todolist.domain.todo;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sample.todolist.util.EnumMapperType;

@Getter
@RequiredArgsConstructor
public enum TodoStatus implements EnumMapperType {

    TODO("할 일"), IN_PROGRESS("진행중"), DONE("완료"), PENDING("대기");

    private final String text;

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return text;
    }
}


