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
        Tarefa tarefa = tarefaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada para atualização."));

        // Atualiza apenas os campos permitidos
        if (dto.getNome() != null) {
            tarefa.setNome(dto.getNome());
        }
        if (dto.getPrioridade() != null) {
            tarefa.setPrioridade(dto.getPrioridade());
        }
        if (dto.getRealizado() != null) {
            tarefa.setRealizado(dto.getRealizado());
            if (Boolean.TRUE.equals(tarefa.getRealizado())) {
                tarefa.setDataConclusao(java.time.LocalDateTime.now());
            } else {
                tarefa.setDataConclusao(null);
            }

        }

        Tarefa atualizada = tarefaRepository.save(tarefa);
        return List.of(atualizada);
    }

    public Tarefa atualizarRealizado(Long id, Boolean realizado) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada."));

        tarefa.setRealizado(realizado);

        if (Boolean.TRUE.equals(realizado)) {
            tarefa.setDataConclusao(java.time.LocalDateTime.now());
        } else {
            tarefa.setDataConclusao(null);
        }

        return tarefaRepository.save(tarefa);
    }
}
