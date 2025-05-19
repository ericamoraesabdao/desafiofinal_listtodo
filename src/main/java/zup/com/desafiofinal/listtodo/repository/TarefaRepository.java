package zup.com.desafiofinal.listtodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zup.com.desafiofinal.listtodo.model.Tarefa;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByRealizado(Boolean realizado);
}