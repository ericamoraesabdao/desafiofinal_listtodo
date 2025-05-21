package zup.com.desafiofinal.listtodo.dto;

import lombok.Data;

@Data
public class ErrorDTO {
    String erro;

    public ErrorDTO(String erro) {
        this.erro = erro;
    }
}
