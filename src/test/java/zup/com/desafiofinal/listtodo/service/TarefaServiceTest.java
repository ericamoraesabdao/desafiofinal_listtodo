package zup.com.desafiofinal.listtodo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import zup.com.desafiofinal.listtodo.dto.TarefaDTO;
import zup.com.desafiofinal.listtodo.exception.NegocioException;
import zup.com.desafiofinal.listtodo.model.Tarefa;
import zup.com.desafiofinal.listtodo.repository.TarefaRepository;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TarefaServiceTest {

    @InjectMocks
    private TarefaService tarefaService;

    @Mock
    private TarefaRepository tarefaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void atualizar_deveAtualizarCamposQuandoDtoValido() {
        TarefaDTO dto = new TarefaDTO();
        dto.setId(1L);
        dto.setNome("Nova Tarefa");
        dto.setPrioridade(2);
        dto.setRealizado(true);

        Tarefa tarefa = new Tarefa();
        tarefa.setId(1L);

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Tarefa> result = tarefaService.atualizar(dto);

        assertEquals("Nova Tarefa", result.get(0).getNome());
        assertEquals(2, result.get(0).getPrioridade());
        assertTrue(result.get(0).getRealizado());
        assertNotNull(result.get(0).getDataConclusao());
        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void atualizar_deveSetarDataConclusaoNullQuandoRealizadoFalse() {
        TarefaDTO dto = new TarefaDTO();
        dto.setId(1L);
        dto.setRealizado(false);

        Tarefa tarefa = new Tarefa();
        tarefa.setId(1L);
        tarefa.setRealizado(true); // estava true antes

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Tarefa> result = tarefaService.atualizar(dto);

        assertFalse(result.get(0).getRealizado());
        assertNull(result.get(0).getDataConclusao());
        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void atualizar_deveLancarExcecaoQuandoIdNulo() {
        TarefaDTO dto = new TarefaDTO();
        dto.setId(null);

        NegocioException ex = assertThrows(NegocioException.class, () -> tarefaService.atualizar(dto));
        assertEquals("O ID não pode ser nulo.", ex.getMessage());
    }

    @Test
    void atualizar_deveLancarExcecaoQuandoTarefaNaoEncontrada() {
        TarefaDTO dto = new TarefaDTO();
        dto.setId(1L);

        when(tarefaRepository.findById(1L)).thenReturn(Optional.empty());

        NegocioException ex = assertThrows(NegocioException.class, () -> tarefaService.atualizar(dto));
        assertEquals("Tarefa não encontrada para atualização.", ex.getMessage());
    }

    @Test
    void atualizarRealizado_deveSetarDataConclusaoNullQuandoRealizadoFalse() {
        // Arrange
        Tarefa tarefa = new Tarefa();
        tarefa.setId(1L);
        tarefa.setRealizado(true); // valor anterior não importa para o else
        tarefa.setDataConclusao(java.time.LocalDateTime.now());

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Tarefa result = tarefaService.atualizarRealizado(1L, false);

        // Assert
        assertFalse(result.getRealizado());
        assertNull(result.getDataConclusao());
        verify(tarefaRepository).save(tarefa);
    }

}