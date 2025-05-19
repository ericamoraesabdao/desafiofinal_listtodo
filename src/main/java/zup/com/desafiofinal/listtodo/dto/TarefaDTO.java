package zup.com.desafiofinal.listtodo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TarefaDTO {
    private Long id;
    private String nome;
    private Integer prioridade;
    private Boolean realizado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;
    private List<AnexoDTO> anexos;
}