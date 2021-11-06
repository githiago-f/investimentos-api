package io.github.pameladilly.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarteiraResponseDTO {

    private Long id;
    private String descricao;
    private LocalDateTime dataCadastro;
    private LocalDateTime ultimaAtualizacao;
    private Long usuario;

    private List<TransacaoResponseDTO> transacoes;

}
