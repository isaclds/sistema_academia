package ads.lll.academia.services;

import ads.lll.academia.models.Aluno;
import ads.lll.academia.models.Matricula;
import ads.lll.academia.models.Plano;
import ads.lll.academia.repositories.AlunoRepository;
import ads.lll.academia.repositories.MatriculaRepository;
import ads.lll.academia.repositories.PlanoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private PlanoRepository planoRepository;

    @InjectMocks
    private MatriculaService matriculaService;

    private Aluno aluno;
    private Plano plano;
    private Matricula matricula;

    @BeforeEach
    void setUp() {

        aluno = new Aluno();
        aluno.setId(1);
        aluno.setNmNome("João");

        plano = new Plano();
        plano.setId(1);
        plano.setNmNome("Premium");
        plano.setNrDuracaoMeses(12);

        matricula = new Matricula();
        matricula.setId(1);
        matricula.setIdAluno(aluno);
        matricula.setIdPlano(plano);
        matricula.setTpStatus(Matricula.TpStatus.ATIVA);
        matricula.setDtVencimento(
                LocalDate.now().plusMonths(12)
        );
    }

    @Test
    void create_deveCriarMatricula() {

        when(alunoRepository.findById(1))
                .thenReturn(Optional.of(aluno));

        when(planoRepository.findById(1))
                .thenReturn(Optional.of(plano));

        when(matriculaRepository.findByIdAlunoAndTpStatus(
                aluno,
                Matricula.TpStatus.ATIVA))
                .thenReturn(Optional.empty());

        when(matriculaRepository.save(any(Matricula.class)))
                .thenReturn(matricula);

        Matricula resultado =
                matriculaService.create(1, 1);

        assertNotNull(resultado);

        verify(matriculaRepository, times(1))
                .save(any(Matricula.class));
    }

    @Test
    void create_deveLancarExcecaoQuandoAlunoNaoExiste() {

        when(alunoRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> matriculaService.create(1, 1)
        );
    }

    @Test
    void create_deveLancarExcecaoQuandoPlanoNaoExiste() {

        when(alunoRepository.findById(1))
                .thenReturn(Optional.of(aluno));

        when(planoRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> matriculaService.create(1, 1)
        );
    }

    @Test
    void create_deveLancarExcecaoQuandoJaExisteMatriculaAtiva() {

        when(alunoRepository.findById(1))
                .thenReturn(Optional.of(aluno));

        when(planoRepository.findById(1))
                .thenReturn(Optional.of(plano));

        when(matriculaRepository.findByIdAlunoAndTpStatus(
                aluno,
                Matricula.TpStatus.ATIVA))
                .thenReturn(Optional.of(matricula));

        assertThrows(
                IllegalArgumentException.class,
                () -> matriculaService.create(1, 1)
        );

        verify(matriculaRepository, never())
                .save(any());
    }

    @Test
    void findAll_deveRetornarLista() {

        when(matriculaRepository.findAll())
                .thenReturn(List.of(matricula));

        List<Matricula> resultado =
                matriculaService.findAll();

        assertEquals(1, resultado.size());
    }

    @Test
    void findById_deveRetornarMatricula() {

        when(matriculaRepository.findById(1))
                .thenReturn(Optional.of(matricula));

        Matricula resultado =
                matriculaService.findById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void findById_deveRetornarNull() {

        when(matriculaRepository.findById(99))
                .thenReturn(Optional.empty());

        Matricula resultado =
                matriculaService.findById(99);

        assertNull(resultado);
    }

    @Test
    void findByAluno_deveRetornarHistorico() {

        when(alunoRepository.findById(1))
                .thenReturn(Optional.of(aluno));

        when(matriculaRepository
                .findByIdAlunoOrderByDtInicioDesc(aluno))
                .thenReturn(List.of(matricula));

        List<Matricula> resultado =
                matriculaService.findByAluno(1);

        assertEquals(1, resultado.size());
    }

    @Test
    void findAtivas_deveRetornarLista() {

        when(matriculaRepository
                .findByTpStatusOrderByDtVencimentoAsc(
                        Matricula.TpStatus.ATIVA))
                .thenReturn(List.of(matricula));

        List<Matricula> resultado =
                matriculaService.findAtivas();

        assertEquals(1, resultado.size());
    }

    @Test
    void cancelar_deveAlterarStatus() {

        when(matriculaRepository.findById(1))
                .thenReturn(Optional.of(matricula));

        when(matriculaRepository.save(any(Matricula.class)))
                .thenReturn(matricula);

        Matricula resultado =
                matriculaService.cancelar(1);

        assertNotNull(resultado);

        verify(matriculaRepository)
                .save(any(Matricula.class));
    }

    @Test
    void atualizarVencidas_deveExecutarQuery() {

        when(matriculaRepository
                .atualizarMatriculasVencidas())
                .thenReturn(3);

        int resultado =
                matriculaService.atualizarVencidas();

        assertEquals(3, resultado);

        verify(matriculaRepository)
                .atualizarMatriculasVencidas();
    }

    @Test
    void possuiMatriculaAtiva_deveRetornarTrue() {

        when(alunoRepository.findById(1))
                .thenReturn(Optional.of(aluno));

        when(matriculaRepository.findByIdAlunoAndTpStatus(
                aluno,
                Matricula.TpStatus.ATIVA))
                .thenReturn(Optional.of(matricula));

        assertTrue(
                matriculaService.possuiMatriculaAtiva(1)
        );
    }

    @Test
    void possuiMatriculaAtiva_deveRetornarFalse() {

        when(alunoRepository.findById(1))
                .thenReturn(Optional.of(aluno));

        when(matriculaRepository.findByIdAlunoAndTpStatus(
                aluno,
                Matricula.TpStatus.ATIVA))
                .thenReturn(Optional.empty());

        assertFalse(
                matriculaService.possuiMatriculaAtiva(1)
        );
    }
}