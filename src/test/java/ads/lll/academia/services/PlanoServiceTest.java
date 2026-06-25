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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        plano.setNmNome("Plano Mensal");
        plano.setVlValor(new BigDecimal("99.90"));
        plano.setNrDuracaoMeses(1);
    }

    @Test
    void create_deveSalvarERetornarPlano_quandoNomeNaoExiste() {
        when(planoRepository.existsByNmNomeIgnoreCase("Plano Mensal")).thenReturn(false);
        when(planoRepository.save(any(Plano.class))).thenReturn(plano);

        Plano resultado = planoService.create("Plano Mensal", new BigDecimal("99.90"), 1);

        assertNotNull(resultado);
        assertEquals("Plano Mensal", resultado.getNmNome());
        assertEquals(new BigDecimal("99.90"), resultado.getVlValor());
        assertEquals(1, resultado.getNrDuracaoMeses());
        verify(planoRepository, times(1)).save(any(Plano.class));
    }

    @Test
    void create_deveLancarExcecao_quandoNomeJaCadastrado() {
        when(planoRepository.existsByNmNomeIgnoreCase("Plano Mensal")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> planoService.create("Plano Mensal", new BigDecimal("99.90"), 1)
        );

        assertEquals("Plano já cadastrado: Plano Mensal", ex.getMessage());
        verify(planoRepository, never()).save(any());
    }

    @Test
    void findAll_deveRetornarListaDePlanos() {
        when(planoRepository.findAll()).thenReturn(List.of(plano));

        List<Plano> resultado = planoService.findAll();

        assertEquals(1, resultado.size());
        verify(planoRepository, times(1)).findAll();
    }

    @Test
    void findAll_deveRetornarListaVazia_quandoNaoHaPlanos() {
        when(planoRepository.findAll()).thenReturn(List.of());

        List<Plano> resultado = planoService.findAll();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void findById_deveRetornarPlano_quandoIdExiste() {
        plano.setId(1);
        when(planoRepository.findById(1)).thenReturn(Optional.of(plano));

        Plano resultado = planoService.findById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void findById_deveRetornarNull_quandoIdNaoExiste() {
        when(planoRepository.findById(99)).thenReturn(Optional.empty());

        Plano resultado = planoService.findById(99);

        assertNull(resultado);
    }

    @Test
    void findByNome_deveRetornarPlano_quandoNomeExiste() {
        when(planoRepository.findByNmNomeIgnoreCase("Plano Mensal")).thenReturn(Optional.of(plano));

        Plano resultado = planoService.findByNome("Plano Mensal");

        assertNotNull(resultado);
        assertEquals("Plano Mensal", resultado.getNmNome());
    }

    @Test
    void findByNome_deveRetornarNull_quandoNomeNaoExiste() {
        when(planoRepository.findByNmNomeIgnoreCase("Inexistente")).thenReturn(Optional.empty());

        Plano resultado = planoService.findByNome("Inexistente");

        assertNull(resultado);
    }

    @Test
    void findPlanosComMatriculasAtivas_deveRetornarListaCorreta() {
        when(planoRepository.findPlanosComMatriculasAtivas()).thenReturn(List.of(plano));

        List<Plano> resultado = planoService.findPlanosComMatriculasAtivas();

        assertEquals(1, resultado.size());
        verify(planoRepository, times(1)).findPlanosComMatriculasAtivas();
    }

    @Test
    void findPlanosComMatriculasAtivas_deveRetornarListaVazia_quandoNaoHaMatriculas() {
        when(planoRepository.findPlanosComMatriculasAtivas()).thenReturn(List.of());

        List<Plano> resultado = planoService.findPlanosComMatriculasAtivas();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void countAlunosPorPlano_deveRetornarListaDeResultados() {
        Object[] linha = new Object[]{"Plano Basic", 5L};
        List<Object[]> mockResultado = new ArrayList<>();
        mockResultado.add(linha);

        when(planoRepository.countAlunosPorPlano()).thenReturn(mockResultado);

        List<Object[]> resultado = planoService.countAlunosPorPlano();

        assertEquals(1, resultado.size());
        assertEquals("Plano Basic", resultado.get(0)[0]);
        assertEquals(5L, resultado.get(0)[1]);
        verify(planoRepository, times(1)).countAlunosPorPlano();
    }

    @Test
    void update_deveAtualizarERetornarPlano_quandoDadosValidos() {
        plano.setId(1);
        when(planoRepository.findById(1)).thenReturn(Optional.of(plano));
        when(planoRepository.existsByNmNomeIgnoreCase("Plano Trimestral")).thenReturn(false);
        when(planoRepository.save(any(Plano.class))).thenReturn(plano);

        Plano resultado = planoService.update(1, "Plano Trimestral", new BigDecimal("249.90"), 3);

        assertNotNull(resultado);
        verify(planoRepository, times(1)).save(any(Plano.class));
    }

    @Test
    void update_deveLancarExcecao_quandoIdNaoExiste() {
        when(planoRepository.findById(99)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> planoService.update(99, "Plano X", new BigDecimal("99.90"), 1)
        );

        assertEquals("Plano não encontrado: 99", ex.getMessage());
        verify(planoRepository, never()).save(any());
    }

    @Test
    void update_deveLancarExcecao_quandoNovoNomeJaExiste() {
        plano.setId(1);
        when(planoRepository.findById(1)).thenReturn(Optional.of(plano));
        when(planoRepository.existsByNmNomeIgnoreCase("Plano Anual")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> planoService.update(1, "Plano Anual", new BigDecimal("99.90"), 1)
        );

        assertEquals("Já existe um plano com o nome: Plano Anual", ex.getMessage());
        verify(planoRepository, never()).save(any());
    }

    @Test
    void delete_deveDeletarPlano_quandoIdExiste() {
        plano.setId(1);
        when(planoRepository.findById(1)).thenReturn(Optional.of(plano));

        planoService.delete(1);

        verify(planoRepository, times(1)).delete(plano);
    }

    @Test
    void delete_deveLancarExcecao_quandoIdNaoExiste() {
        when(planoRepository.findById(99)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> planoService.delete(99)
        );

        assertEquals("Plano não encontrado: 99", ex.getMessage());
        verify(planoRepository, never()).delete(any());
    }
}