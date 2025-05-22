package zup.com.desafiofinal.listtodo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import zup.com.desafiofinal.listtodo.dto.TarefaDTO;
import zup.com.desafiofinal.listtodo.model.Tarefa;
import zup.com.desafiofinal.listtodo.service.TarefaService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TarefaControllerTest {

    @InjectMocks
    private TarefaController controller;

    @Mock
    private TarefaService tarefaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Tarefa getTarefa(Long id, String nome, Integer prioridade, Boolean realizado) {
        Tarefa t = new Tarefa();
        t.setId(id);
        t.setNome(nome);
        t.setPrioridade(prioridade);
        t.setRealizado(realizado);
        t.setDataCriacao(LocalDateTime.of(2024, 1, 1, 10, 0));
        t.setDataConclusao(LocalDateTime.of(2024, 1, 2, 10, 0));
        return t;
    }

    private TarefaDTO getTarefaDTO(Long id, String nome, Integer prioridade, Boolean realizado) {
        TarefaDTO dto = new TarefaDTO();
        dto.setId(id);
        dto.setNome(nome);
        dto.setPrioridade(prioridade);
        dto.setRealizado(realizado);
        dto.setDataCriacao(LocalDateTime.of(2024, 1, 1, 10, 0));
        dto.setDataConclusao(LocalDateTime.of(2024, 1, 2, 10, 0));
        return dto;
    }

    @Test
    void buscarPorStatus_deveRetornarListaConvertida() {
        Tarefa tarefa = getTarefa(1L, "t1", 2, true);
        when(tarefaService.buscarPorStatus(true)).thenReturn(List.of(tarefa));

        List<TarefaDTO> result = controller.buscarPorStatus(true);

        assertEquals(1, result.size());
        assertEquals("t1", result.get(0).getNome());
        verify(tarefaService).buscarPorStatus(true);
    }

    @Test
    void listarTodas_deveRetornarListaConvertida() {
        Tarefa tarefa = getTarefa(2L, "t2", 3, false);
        when(tarefaService.listarTodas()).thenReturn(List.of(tarefa));

        List<TarefaDTO> result = controller.listarTodas();

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
        verify(tarefaService).listarTodas();
    }

    @Test
    void criar_deveConverterESalvarERetornarDTO() {
        TarefaDTO dto = getTarefaDTO(null, "nova", 1, false);
        Tarefa tarefaSalva = getTarefa(10L, "nova", 1, false);

        when(tarefaService.salvar(any(Tarefa.class))).thenReturn(tarefaSalva);

        TarefaDTO result = controller.criar(dto);

        assertEquals(10L, result.getId());
        assertEquals("nova", result.getNome());
        verify(tarefaService).salvar(any(Tarefa.class));
    }

    @Test
    void atualizarRealizado_deveChamarServiceEConverter() {
        Tarefa tarefaAtualizada = getTarefa(5L, "tarefa", 2, true);
        when(tarefaService.atualizarRealizado(5L, true)).thenReturn(tarefaAtualizada);

        TarefaDTO result = controller.atualizarRealizado(5L, true);

        assertEquals(5L, result.getId());
        assertTrue(result.getRealizado());
        verify(tarefaService).atualizarRealizado(5L, true);
    }

    @Test
    void atualizar_deveChamarServiceEConverterLista() {
        TarefaDTO dto = getTarefaDTO(7L, "atualizar", 4, false);
        Tarefa tarefaAtualizada = getTarefa(7L, "atualizar", 4, false);

        when(tarefaService.atualizar(dto)).thenReturn(List.of(tarefaAtualizada));

        List<TarefaDTO> result = controller.atualizar(dto);

        assertEquals(1, result.size());
        assertEquals("atualizar", result.get(0).getNome());
        verify(tarefaService).atualizar(dto);
    }

    @Test
    void deletar_deveChamarServiceERetornarNoContent() {
        ResponseEntity<Void> response = controller.deletar(99L);

        assertEquals(204, response.getStatusCodeValue());
        verify(tarefaService).deletar(99L);
    }

    // Testes dos métodos privados via reflexão (opcional, mas cobre 100%)
    @Test
    void toEntity_deveConverterDTOParaTarefa() throws Exception {
        TarefaDTO dto = getTarefaDTO(1L, "abc", 2, true);
        var method = TarefaController.class.getDeclaredMethod("toEntity", TarefaDTO.class);
        method.setAccessible(true);
        Tarefa tarefa = (Tarefa) method.invoke(controller, dto);

        assertEquals("abc", tarefa.getNome());
        assertEquals(2, tarefa.getPrioridade());
        assertTrue(tarefa.getRealizado());
        assertEquals(dto.getDataCriacao(), tarefa.getDataCriacao());
        assertEquals(dto.getDataConclusao(), tarefa.getDataConclusao());
    }

    @Test
    void toDTO_deveConverterTarefaParaDTO() throws Exception {
        Tarefa tarefa = getTarefa(2L, "xyz", 3, false);
        var method = TarefaController.class.getDeclaredMethod("toDTO", Tarefa.class);
        method.setAccessible(true);
        TarefaDTO dto = (TarefaDTO) method.invoke(controller, tarefa);

        assertEquals("xyz", dto.getNome());
        assertEquals(3, dto.getPrioridade());
        assertFalse(dto.getRealizado());
        assertEquals(tarefa.getDataCriacao(), dto.getDataCriacao());
        assertEquals(tarefa.getDataConclusao(), dto.getDataConclusao());
    }
}