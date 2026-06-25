package ads.lll.academia.services;

import ads.lll.academia.models.Aula;
import ads.lll.academia.repositories.AulaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AulaServiceTest {

    @Mock
    private AulaRepository aulaRepository;

    @InjectMocks
    private AulaService aulaService;

    private Aula aula;

    @BeforeEach
    void setUp() {

        aula = new Aula();
        aula.setId(1);
        aula.setNmNome("Bike");
        aula.setNmProfessor("Carlos");
        aula.setHrInicio(LocalTime.of(18, 0));
        aula.setHrFim(LocalTime.of(19, 0));
        aula.setNrCapacidadeMaxima(20);
    }

    @Test
    void create_deveSalvarAula() {

        when(aulaRepository.save(any(Aula.class)))
                .thenReturn(aula);

        Aula resultado = aulaService.create(
                "Spinning",
                "Carlos",
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                20
        );

        assertNotNull(resultado);
        assertEquals("Spinning", resultado.getNmNome());

        verify(aulaRepository, times(1))
                .save(any(Aula.class));
    }

    @Test
    void findAll_deveRetornarListaDeAulas() {

        when(aulaRepository.findAll())
                .thenReturn(List.of(aula));

        List<Aula> resultado = aulaService.findAll();

        assertEquals(1, resultado.size());

        verify(aulaRepository, times(1))
                .findAll();
    }

    @Test
    void findAll_deveRetornarListaVazia() {

        when(aulaRepository.findAll())
                .thenReturn(List.of());

        List<Aula> resultado = aulaService.findAll();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void findById_deveRetornarAula() {

        when(aulaRepository.findById(1))
                .thenReturn(Optional.of(aula));

        Aula resultado = aulaService.findById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void findById_deveRetornarNull() {

        when(aulaRepository.findById(99))
                .thenReturn(Optional.empty());

        Aula resultado = aulaService.findById(99);

        assertNull(resultado);
    }

    @Test
    void update_deveAtualizarAula() {

        when(aulaRepository.findById(1))
                .thenReturn(Optional.of(aula));

        when(aulaRepository.save(any(Aula.class)))
                .thenReturn(aula);

        Aula resultado = aulaService.update(
                1,
                "Funcional",
                "Maria",
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                25
        );

        assertNotNull(resultado);

        verify(aulaRepository, times(1))
                .save(any(Aula.class));
    }

    @Test
    void update_deveLancarExcecaoQuandoNaoEncontrarAula() {

        when(aulaRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> aulaService.update(
                        1,
                        "Funcional",
                        "Maria",
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0),
                        25
                )
        );
    }

    @Test
    void delete_deveRemoverAula() {

        when(aulaRepository.findById(1))
                .thenReturn(Optional.of(aula));

        aulaService.delete(1);

        verify(aulaRepository, times(1))
                .delete(aula);
    }

    @Test
    void delete_deveLancarExcecaoQuandoNaoEncontrarAula() {

        when(aulaRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> aulaService.delete(1)
        );
    }
}