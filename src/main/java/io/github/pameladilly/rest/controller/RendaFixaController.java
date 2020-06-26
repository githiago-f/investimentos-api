package io.github.pameladilly.rest.controller;

import io.github.pameladilly.domain.entity.RendaFixa;
import io.github.pameladilly.domain.entity.Usuario;
import io.github.pameladilly.domain.enums.TipoAtivo;
import io.github.pameladilly.rest.dto.RendaFixaRequestDTO;
import io.github.pameladilly.rest.dto.RendaFixaResponseDTO;
import io.github.pameladilly.service.RendaFixaService;
import io.github.pameladilly.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/rendafixa")
@RequiredArgsConstructor
public class RendaFixaController {

    private final RendaFixaService service;
    private final ModelMapper modelMapper;
    private final UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RendaFixaResponseDTO salvar( @RequestBody @Valid RendaFixaRequestDTO rendaFixaRequestDTO){


        RendaFixa rendaFixa = rendaFixaRequestDTOToRendaFixa(rendaFixaRequestDTO);

        rendaFixa = service.salvar(rendaFixa);

        return rendaFixaToRendaFixaResponseDTO(rendaFixa);
    }

    private RendaFixa rendaFixaRequestDTOToRendaFixa(RendaFixaRequestDTO rendaFixaRequestDTO){

        Usuario idUsuario = usuarioService.getUsuarioById(rendaFixaRequestDTO.getUsuario());


        return new RendaFixa(null, rendaFixaRequestDTO.getDescricao(), null, TipoAtivo.RENDAFIXA,
            idUsuario, rendaFixaRequestDTO.getRentabilidadeDiaria(),
                rendaFixaRequestDTO.getVencimento(),
                rendaFixaRequestDTO.getPreco(),
                rendaFixaRequestDTO.getRentabilidadeMensal());
    }

    private RendaFixaResponseDTO rendaFixaToRendaFixaResponseDTO(RendaFixa rendaFixa){

        return RendaFixaResponseDTO
                .builder()
                .dataCadastro(rendaFixa.getDataCadastro())
                .preco(rendaFixa.getPreco())
                .rentabilidadeDiaria(rendaFixa.getRentabilidadeDiaria())
                .rentabilidadeMensal(rendaFixa.getRentabilidadeMensal())
                .descricao(rendaFixa.getDescricao())
                .tipoAtivo(TipoAtivo.RENDAFIXA.toString())
                .id(rendaFixa.getIdAtivo())
                .usuario(rendaFixa.getUsuario().getIdUsuario())
                .build();

    }
}
