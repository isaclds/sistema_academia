package ads.lll.academia.services;

import ads.lll.academia.models.Agendamento;
import ads.lll.academia.models.Aluno;
import ads.lll.academia.models.Aula;
import ads.lll.academia.repositories.AgendamentoRepository;
import ads.lll.academia.repositories.AlunoRepository;
import ads.lll.academia.repositories.AulaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private AulaRepository aulaRepository;

    @Mock
    private MatriculaService matriculaService;

    @InjectMocks
    private AgendamentoService agendamentoService;

    private Aluno aluno;
    private Aula aula;
    private Agendamento agendamento;

    @BeforeEach
    void setUp() {

        aluno = new Aluno();
        aluno.setId(1);
        aluno.setNmNome("João Silva");

        aula = new Aula();
        aula.setId(1);
        aula.setNmNome("Bike");
        aula.setNmProfessor("Carlos");
        aula.setHrInicio(LocalTime.of(18, 0));
        aula.setHrFim(LocalTime.of(19, 0));
        aula.setNrCapacidadeMaxima(20);

        agendamento = new Agendamento();
        agendamento.setId(1);
        agendamento.setIdAluno(aluno);
        agendamento.setIdAula(aula);
        agendamento.setDtAula(LocalDate.now().plusDays(1));
        agendamento.setTpStatus(Agendamento.TpStatus.CONFIRMADO);
    }

    @Test
    void create_deveSalvarERetornarAgendamento_quandoDadosValidos() {

        when(alunoRepository.findById(1))
                .thenReturn(Optional.of(aluno));

        when(aulaRepository.findById(1))
                .thenReturn(Optional.of(aula));

        when(matriculaService.possuiMatriculaAtiva(1))
                .thenReturn(true);

        when(agendamentoRepository.save(any(Agendamento.class)))
                .thenReturn(agendamento);

        Agendamento resultado = agendamentoService.create(1, 1, LocalDate.now().plusDays(1));

        assertNotNull(resultado);
        assertEquals(Agendamento.TpStatus.CONFIRMADO, resultado.getTpStatus());

        verify(agendamentoRepository, times(1))
                .save(any(Agendamento.class));
    }

    @Test
    void create_deveLancarExcecao_quandoAlunoNaoExiste() {

        when(alunoRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> agendamentoService.create(99, 1, LocalDate.now().plusDays(1))
        );

        verify(agendamentoRepository, never())
                .save(any());
    }

    @Test
    void create_deveLancarExcecao_quandoAulaNaoExiste() {

        when(alunoRepository.findById(1))
                .thenReturn(Optional.of(aluno));

        when(aulaRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> agendamentoService.create(1, 99, LocalDate.now().plusDays(1))
        );

        verify(agendamentoRepository, never())
                .save(any());
    }

    @Test
    void create_deveLancarExcecao_quandoAlunoNaoPossuiMatriculaAtiva() {

        when(alunoRepository.findById(1))
                .thenReturn(Optional.of(aluno));

        when(aulaRepository.findById(1))
                .thenReturn(Optional.of(aula));

        when(matriculaService.possuiMatriculaAtiva(1))
                .thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> agendamentoService.create(1, 1, LocalDate.now().plusDays(1))
        );

        verify(agendamentoRepository, never())
                .save(any());
    }

    @Test
    void findAll_deveRetornarListaDeAgendamentos() {

        when(agendamentoRepository.findAll())
                .thenReturn(List.of(agendamento));

        List<Agendamento> resultado = agendamentoService.findAll();

        assertEquals(1, resultado.size());

        verify(agendamentoRepository, times(1))
                .findAll();
    }

    @Test
    void findAll_deveRetornarListaVazia() {

        when(agendamentoRepository.findAll())
                .thenReturn(List.of());

        List<Agendamento> resultado = agendamentoService.findAll();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void findById_deveRetornarAgendamento_quandoIdExiste() {

        when(agendamentoRepository.findById(1))
                .thenReturn(Optional.of(agendamento));

        Agendamento resultado = agendamentoService.findById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void findById_deveRetornarNull_quandoIdNaoExiste() {

        when(agendamentoRepository.findById(99))
                .thenReturn(Optional.empty());

        Agendamento resultado = agendamentoService.findById(99);

        assertNull(resultado);
    }

    @Test
    void findProximosAgendamentos_deveRetornarLista_quandoAlunoExiste() {

        when(alunoRepository.findById(1))
                .thenReturn(Optional.of(aluno));

        when(agendamentoRepository
                .findByIdAlunoAndDtAulaGreaterThanEqualOrderByDtAulaAsc(
                        eq(aluno), any(LocalDate.class)))
                .thenReturn(List.of(agendamento));

        List<Agendamento> resultado = agendamentoService.findProximosAgendamentos(1);

        assertEquals(1, resultado.size());

        verify(agendamentoRepository, times(1))
                .findByIdAlunoAndDtAulaGreaterThanEqualOrderByDtAulaAsc(
                        eq(aluno), any(LocalDate.class));
    }

    @Test
    void findProximosAgendamentos_deveLancarExcecao_quandoAlunoNaoExiste() {

        when(alunoRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> agendamentoService.findProximosAgendamentos(99)
        );
    }

    @Test
    void findHistoricoAluno_deveRetornarLista_quandoAlunoExiste() {

        when(alunoRepository.findById(1))
                .thenReturn(Optional.of(aluno));

        when(agendamentoRepository.findByIdAlunoOrderByDtAulaDesc(aluno))
                .thenReturn(List.of(agendamento));

        List<Agendamento> resultado = agendamentoService.findHistoricoAluno(1);

        assertEquals(1, resultado.size());

        verify(agendamentoRepository, times(1))
                .findByIdAlunoOrderByDtAulaDesc(aluno);
    }

    @Test
    void findHistoricoAluno_deveLancarExcecao_quandoAlunoNaoExiste() {

        when(alunoRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> agendamentoService.findHistoricoAluno(99)
        );
    }

    @Test
    void cancelar_deveAlterarStatusParaCancelado() {

        when(agendamentoRepository.findById(1))
                .thenReturn(Optional.of(agendamento));

        when(agendamentoRepository.save(any(Agendamento.class)))
                .thenReturn(agendamento);

        Agendamento resultado = agendamentoService.cancelar(1);

        assertNotNull(resultado);
        assertEquals(Agendamento.TpStatus.CANCELADO, resultado.getTpStatus());

        verify(agendamentoRepository, times(1))
                .save(any(Agendamento.class));
    }

    @Test
    void cancelar_deveLancarExcecao_quandoIdNaoExiste() {

        when(agendamentoRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> agendamentoService.cancelar(99)
        );

        verify(agendamentoRepository, never())
                .save(any());
    }

    @Test
    void delete_deveRemoverAgendamento_quandoIdExiste() {

        when(agendamentoRepository.findById(1))
                .thenReturn(Optional.of(agendamento));

        agendamentoService.delete(1);

        verify(agendamentoRepository, times(1))
                .delete(agendamento);
    }

    @Test
    void delete_deveLancarExcecao_quandoIdNaoExiste() {

        when(agendamentoRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> agendamentoService.delete(99)
        );

        verify(agendamentoRepository, never())
                .delete(any());
    }
}