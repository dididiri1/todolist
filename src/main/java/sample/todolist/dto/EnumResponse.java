package sample.todolist.dto;

import lombok.Data;
import sample.todolist.util.EnumMapperType;

@Data
public class EnumResponse {

    private String code;
    private String title;

    public EnumResponse(EnumMapperType e){
        this.code = e.getKey();
        this.title = e.getValue();
    }
}
