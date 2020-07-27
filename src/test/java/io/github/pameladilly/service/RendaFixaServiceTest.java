package io.github.pameladilly.service;

import io.github.pameladilly.domain.entity.RendaFixa;
import io.github.pameladilly.domain.entity.Usuario;
import io.github.pameladilly.domain.enums.TipoAtivo;
import io.github.pameladilly.domain.repository.RendaFixaRepository;
import io.github.pameladilly.exception.RegraNegocioException;
import io.github.pameladilly.exception.rendafixa.RendaFixaNotFound;
import io.github.pameladilly.exception.usuario.UsuarioNotFoundException;
import io.github.pameladilly.service.impl.RendaFixaImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RendaFixaServiceTest {

    RendaFixaService service;

    @MockBean
    RendaFixaRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new RendaFixaImpl( repository );
    }

    @Test
    @DisplayName("Deve salvar um ativo de renda fixa")
    public void salvarRendaFixaTest(){
        Usuario usuario = UsuarioServiceTest.createNewUsuario();

        LocalDate vencimento =  LocalDate.of(2025, 12, 1);

        RendaFixa rendaFixa = newRendaFixa(usuario, vencimento);
        RendaFixa rendaFixaMock = newRendaFixa(usuario, vencimento);
        rendaFixaMock.setIdAtivo(1L);
        rendaFixaMock.setDataCadastro(LocalDateTime.now());



        Mockito.when( repository.save(Mockito.any(RendaFixa.class))).thenReturn(rendaFixaMock);
        RendaFixa rendaFixaSalva = service.salvar(rendaFixa);

        Assertions.assertThat(rendaFixaSalva.getIdAtivo()).isNotNull();
        Assertions.assertThat(rendaFixaSalva.getDescricao()).isEqualTo(rendaFixaMock.getDescricao());
        Assertions.assertThat(rendaFixaSalva.getDataCadastro()).isEqualTo(rendaFixaMock.getDataCadastro());

        Mockito.verify( repository, Mockito.times(1)).save(rendaFixa);
        //service.salvar()
    }

    private RendaFixa newRendaFixa(Usuario usuario, LocalDate vencimento) {
        return new RendaFixa(null,
                "Minha renda fixa", null, null,
                TipoAtivo.RENDAFIXA, usuario, BigDecimal.valueOf(0.01), vencimento, BigDecimal.valueOf(5000.00), BigDecimal.valueOf(1));
    }

    @Test
    @DisplayName("Deve atualizar renda fixa")
    public void atualizarRendaFixaTest(){
        Usuario usuario = UsuarioServiceTest.createNewUsuario();
        RendaFixa rendaFixaMock = newRendaFixa(usuario, LocalDate.of(2025, 1, 1));
        rendaFixaMock.setDescricao("Tesouro Direto");
        rendaFixaMock.setIdAtivo(1L);

        Mockito.when( repository.findById(Mockito.anyLong())).thenReturn(Optional.of(rendaFixaMock));
        Mockito.when( repository.save(Mockito.any(RendaFixa.class))).thenReturn( rendaFixaMock);


        RendaFixa rendaFixaAtualizada = service.atualizar(rendaFixaMock);

        Assertions.assertThat(rendaFixaAtualizada.getIdAtivo()).isEqualTo(rendaFixaMock.getIdAtivo());
        Assertions.assertThat(rendaFixaAtualizada.getVencimento()).isEqualTo(rendaFixaMock.getVencimento());
        Assertions.assertThat(rendaFixaAtualizada.getDescricao()).isEqualTo(rendaFixaMock.getDescricao());

        Mockito.verify(repository, Mockito.times(1)).save(rendaFixaMock);


    }

    @Test
    @DisplayName("Deve excluir um ativo")
    public void excluirRendaFixaTest(){
        Usuario usuario = UsuarioServiceTest.createNewUsuario();
        RendaFixa rendaFixaMock = newRendaFixa(usuario, LocalDate.of(2025, 1, 1));
        rendaFixaMock.setIdAtivo(1L);


        Mockito.when( repository.findById(Mockito.anyLong())).thenReturn(Optional.of(rendaFixaMock));

        org.junit.jupiter.api.Assertions.assertDoesNotThrow( () -> service.excluir(rendaFixaMock));

        Mockito.verify( repository, Mockito.times(1)).delete(rendaFixaMock);

    }

    @Test
    @DisplayName("Deve gerar exceção RendaFixaNotFound ao tentar excluir")
    public void excluirRendaFixaNotFoundTest(){
        Usuario usuario = UsuarioServiceTest.createNewUsuario();
        RendaFixa rendaFixaMock = newRendaFixa(usuario, LocalDate.of(2025, 1, 1));
        rendaFixaMock.setIdAtivo(null);

        org.junit.jupiter.api.Assertions.assertThrows( RendaFixaNotFound.class, () -> service.excluir(rendaFixaMock));

        Mockito.verify( repository, Mockito.never()).delete(rendaFixaMock);
    }

    @Test
    @DisplayName("Deve gerar exceção por usuário não informado")
    public void salvarRendaFixaSemUsuarioTest(){
        RendaFixa rendaFixaMock = newRendaFixa(null, LocalDate.of(2025, 1, 1) );

        Throwable exception = Assertions.catchThrowable(() -> service.salvar(rendaFixaMock));
        Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessageStartingWith("Usuário não informado");

        Mockito.verify( repository, Mockito.never()).save(rendaFixaMock);

    }

    @Test
    @DisplayName("Deve gerar exceção ao tentar atualizar renda fixa inexistente")
    public void atualizarRendaFixaInexistenteTest(){

        Usuario usuario = UsuarioServiceTest.createNewUsuario();
        RendaFixa rendaFixaMock = newRendaFixa(usuario, LocalDate.of(2025, 1, 1));
        rendaFixaMock.setIdAtivo(1L);

        Throwable  exception = org.junit.jupiter.api.Assertions.assertThrows(RendaFixaNotFound.class, () -> service.atualizar(rendaFixaMock));

        Assertions.assertThat(exception).isInstanceOf(RendaFixaNotFound.class).hasMessage(RendaFixaNotFound.MSG);

        Mockito.verify( repository, Mockito.never()).delete(rendaFixaMock);
    }




}
