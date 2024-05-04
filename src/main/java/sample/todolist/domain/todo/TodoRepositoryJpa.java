package sample.todolist.domain.todo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepositoryJpa extends JpaRepository<Todo, Long> {
}
