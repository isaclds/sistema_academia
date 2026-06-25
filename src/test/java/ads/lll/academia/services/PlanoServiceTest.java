package ads.lll.academia.services;

import ads.lll.academia.models.Plano;
import ads.lll.academia.repositories.PlanoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanoServiceTest {

    @Mock
    private PlanoRepository planoRepository;

    @InjectMocks
    private PlanoService planoService;

    private Plano plano;

    @BeforeEach
    void setUp() {
        plano = new Plano();
        plano.setId(1);
        plano.setNmNome("Premium");
        plano.setVlValor(BigDecimal.valueOf(99.90));
        plano.setNrDuracaoMeses(12);
    }

    @Test
    void create_deveSalvarPlano() {

        when(planoRepository.existsByNmNomeIgnoreCase("Premium"))
                .thenReturn(false);

        when(planoRepository.save(any(Plano.class)))
                .thenReturn(plano);

        Plano resultado = planoService.create(
                "Premium",
                BigDecimal.valueOf(99.90),
                12
        );

        assertNotNull(resultado);
        assertEquals("Premium", resultado.getNmNome());

        verify(planoRepository, times(1))
                .save(any(Plano.class));
    }

    @Test
    void create_deveLancarExcecaoQuandoNomeExiste() {

        when(planoRepository.existsByNmNomeIgnoreCase("Premium"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> planoService.create(
                        "Premium",
                        BigDecimal.valueOf(99.90),
                        12
                )
        );

        verify(planoRepository, never())
                .save(any());
    }

    @Test
    void findAll_deveRetornarLista() {

        when(planoRepository.findAll())
                .thenReturn(List.of(plano));

        List<Plano> resultado = planoService.findAll();

        assertEquals(1, resultado.size());

        verify(planoRepository, times(1))
                .findAll();
    }

    @Test
    void findById_deveRetornarPlano() {

        when(planoRepository.findById(1))
                .thenReturn(java.util.Optional.of(plano));

        Plano resultado = planoService.findById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void findById_deveRetornarNull() {

        when(planoRepository.findById(1))
                .thenReturn(java.util.Optional.empty());

        Plano resultado = planoService.findById(1);

        assertNull(resultado);
    }

    @Test
    void findByNome_deveRetornarPlano() {

        when(planoRepository.findByNmNomeIgnoreCase("Premium"))
                .thenReturn(java.util.Optional.of(plano));

        Plano resultado = planoService.findByNome("Premium");

        assertNotNull(resultado);
        assertEquals("Premium", resultado.getNmNome());
    }

    @Test
    void findPlanosComMatriculasAtivas_deveRetornarLista() {

        when(planoRepository.findPlanosComMatriculasAtivas())
                .thenReturn(List.of(plano));

        List<Plano> resultado =
                planoService.findPlanosComMatriculasAtivas();

        assertEquals(1, resultado.size());

        verify(planoRepository)
                .findPlanosComMatriculasAtivas();
    }
}