package zup.com.desafiofinal.listtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zup.com.desafiofinal.listtodo.dto.TarefaDTO;
import zup.com.desafiofinal.listtodo.model.Tarefa;
import zup.com.desafiofinal.listtodo.repository.TarefaRepository;

import java.util.List;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    public List<Tarefa> buscarPorStatus(Boolean realizado) {
        return tarefaRepository.findByRealizado(realizado);
    }

    public Tarefa salvar(Tarefa tarefa) {
        if (tarefa.getDataCriacao() == null) {
            tarefa.setDataCriacao(java.time.LocalDateTime.now());
        }
        return tarefaRepository.save(tarefa);
    }

    public void deletar(Long id) {
        tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não existe a Tarefa para excluir."));
        tarefaRepository.deleteById(id);
    }

    public List<Tarefa> atualizar(TarefaDTO dto) {
        return List.of();
    }
}
