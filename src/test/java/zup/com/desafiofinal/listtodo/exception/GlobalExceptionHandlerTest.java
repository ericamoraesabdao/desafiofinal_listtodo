package zup.com.desafiofinal.listtodo.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import zup.com.desafiofinal.listtodo.dto.ErrorDTO;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleTypeMismatch_returnsBadRequestAndMessage() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        ResponseEntity<ErrorDTO> response = handler.handleTypeMismatch(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody().getErro());
    }

    @Test
    void handleNegocio_returnsBadRequestAndCustomMessage() {
        NegocioException ex = new NegocioException("msg de negócio");
        ResponseEntity<ErrorDTO> response = handler.handleNegocio(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("msg de negócio", response.getBody().getErro());
    }

    @Test
    void handleDataIntegrityViolation_returnsBadRequestAndDefaultMessage() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("erro");
        ResponseEntity<ErrorDTO> response = handler.handleDataIntegrityViolation(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Campos obrigatórios não preenchidos ou violação de restrição de dados.", response.getBody().getErro());
    }

    @Test
    void handleConstraintViolation_returnsBadRequestAndAggregatedMessages() {
        // Mock ConstraintViolation
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("campo");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("mensagem");
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException ex = new ConstraintViolationException(violations);
        ResponseEntity<ErrorDTO> response = handler.handleConstraintViolation(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getErro().contains("campo: mensagem"));
    }

    @Test
    void handleConstraintViolation_returnsBadRequestAndDefaultMessageWhenEmpty() {
        ConstraintViolationException ex = new ConstraintViolationException(Collections.emptySet());
        ResponseEntity<ErrorDTO> response = handler.handleConstraintViolation(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody().getErro());
    }

    @Test
    void handleInvalidJson_returnsBadRequestAndMessage() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("erro", (Throwable) null);
        ResponseEntity<ErrorDTO> response = handler.handleInvalidJson(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody().getErro());
    }

    @Test
    void handleGenericException_returnsInternalServerErrorAndExceptionMessage() {
        Exception ex = new Exception("erro genérico");
        ResponseEntity<ErrorDTO> response = handler.handleGenericException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("erro genérico", response.getBody().getErro());
    }
}