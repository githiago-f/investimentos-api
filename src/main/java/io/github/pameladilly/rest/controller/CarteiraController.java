package io.github.pameladilly.rest.controller;

import io.github.pameladilly.domain.entity.Carteira;
import io.github.pameladilly.rest.dto.CarteiraRequestDTO;
import io.github.pameladilly.rest.dto.CarteiraResponseDTO;
import io.github.pameladilly.service.CarteiraService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/carteira")
public class CarteiraController {

    private final CarteiraService service;
    private final ModelMapper modelMapper;

    public CarteiraController(CarteiraService service) {
        this.service = service;
        this.modelMapper = new ModelMapper();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarteiraResponseDTO salvar(@RequestBody @Valid CarteiraRequestDTO carteiraRequestDTO) {
        Carteira carteira = modelMapper.map(carteiraRequestDTO, Carteira.class);
        carteira.setIdCarteira(null);
        carteira = service.salvar(carteira);

        CarteiraResponseDTO carteiraRespDTO = modelMapper.map(carteira, CarteiraResponseDTO.class);
        return carteiraRespDTO;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CarteiraResponseDTO> find(CarteiraRequestDTO carteiraRequestDTO, Pageable pageRequest) {
        Carteira filter = modelMapper.map(carteiraRequestDTO, Carteira.class);
        Page<Carteira> result = service.pesquisar(filter, pageRequest);

        List<CarteiraResponseDTO> list = result
                .getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, CarteiraResponseDTO.class)).collect(Collectors.toList());
        return new PageImpl<CarteiraResponseDTO>( list, pageRequest, result.getTotalElements());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarteiraResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid CarteiraRequestDTO carteiraRequestDTO){
        Carteira carteira = modelMapper.map(carteiraRequestDTO, Carteira.class);
        carteira = service.atualizar(id, carteira);
        return modelMapper.map(carteira, CarteiraResponseDTO.class);
    }
}
