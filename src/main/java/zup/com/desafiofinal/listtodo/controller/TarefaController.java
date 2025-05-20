package zup.com.desafiofinal.listtodo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zup.com.desafiofinal.listtodo.dto.TarefaDTO;
import zup.com.desafiofinal.listtodo.model.Tarefa;
import zup.com.desafiofinal.listtodo.service.TarefaService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    private Tarefa toEntity(TarefaDTO dto) {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(dto.getId());
        tarefa.setNome(dto.getNome());
        tarefa.setPrioridade(dto.getPrioridade());
        tarefa.setRealizado(dto.getRealizado() != null ? dto.getRealizado() : false);
        tarefa.setDataCriacao(dto.getDataCriacao());
        tarefa.setDataConclusao(dto.getDataConclusao());
        // Conversão de anexos omitida para simplificação
        return tarefa;
    }

    // Converter Entidade para DTO
    private TarefaDTO toDTO(Tarefa tarefa) {
        TarefaDTO dto = new TarefaDTO();
        dto.setId(tarefa.getId());
        dto.setNome(tarefa.getNome());
        dto.setPrioridade(tarefa.getPrioridade());
        dto.setRealizado(tarefa.getRealizado());
        dto.setDataCriacao(tarefa.getDataCriacao());
        dto.setDataConclusao(tarefa.getDataConclusao());
        // Conversão de anexos omitida para simplificação
        return dto;
    }

    @GetMapping("/{realizado}")
    public List<TarefaDTO> buscarPorStatus(@PathVariable Boolean realizado) {
        return tarefaService.buscarPorStatus(realizado)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<TarefaDTO> listarTodas() {
        return tarefaService.listarTodas()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public TarefaDTO criar(@RequestBody TarefaDTO dto) {
        Tarefa tarefa = toEntity(dto);
        tarefa.setDataCriacao(java.time.LocalDateTime.now());
        Tarefa salva = tarefaService.salvar(tarefa);
        return toDTO(salva);
    }

    @PutMapping
    public List<TarefaDTO> atualizar(@RequestBody TarefaDTO dto) {
        List<Tarefa> atualizadas = tarefaService.atualizar(dto);
        return atualizadas.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}