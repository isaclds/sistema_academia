package ads.lll.academia.services;

import ads.lll.academia.models.Aluno;
import ads.lll.academia.repositories.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoService alunoService;

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno();
        aluno.setNmNome("João Silva");
        aluno.setNrCpf("123.456.789-00");
        aluno.setNmEmail("joao@email.com");
    }

    @Test
    void create_deveSalvarERetornarAluno_quandoCpfNaoExiste() {
        when(alunoRepository.findByNrCpf("123.456.789-00")).thenReturn(Optional.empty());
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        Aluno resultado = alunoService.create("João Silva", "123.456.789-00", "joao@email.com");

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNmNome());
        assertEquals("123.456.789-00", resultado.getNrCpf());
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    void create_deveLancarExcecao_quandoCpfJaCadastrado() {
        when(alunoRepository.findByNrCpf("123.456.789-00")).thenReturn(Optional.of(aluno));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> alunoService.create("João Silva", "123.456.789-00", "joao@email.com")
        );

        assertEquals("CPF já cadastrado: 123.456.789-00", ex.getMessage());
        verify(alunoRepository, never()).save(any()); // nunca deve salvar
    }

    @Test
    void findAll_deveRetornarListaDeAlunos() {
        when(alunoRepository.findAll()).thenReturn(List.of(aluno));

        List<Aluno> resultado = alunoService.findAll();

        assertEquals(1, resultado.size());
        verify(alunoRepository, times(1)).findAll();
    }

    @Test
    void findAll_deveRetornarListaVazia_quandoNaoHaAlunos() {
        when(alunoRepository.findAll()).thenReturn(List.of());

        List<Aluno> resultado = alunoService.findAll();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void findByCpf_deveRetornarAluno_quandoCpfExiste() {
        when(alunoRepository.findByNrCpf("123.456.789-00")).thenReturn(Optional.of(aluno));

        Aluno resultado = alunoService.findByCpf("123.456.789-00");

        assertNotNull(resultado);
        assertEquals("123.456.789-00", resultado.getNrCpf());
    }

    @Test
    void findByCpf_deveRetornarNull_quandoCpfNaoExiste() {
        when(alunoRepository.findByNrCpf("000.000.000-00")).thenReturn(Optional.empty());

        Aluno resultado = alunoService.findByCpf("000.000.000-00");

        assertNull(resultado);
    }

    @Test
    void findByEmail_deveRetornarAluno_quandoEmailExiste() {
        when(alunoRepository.findByNmEmail("joao@email.com")).thenReturn(Optional.of(aluno));

        Aluno resultado = alunoService.findByEmail("joao@email.com");

        assertNotNull(resultado);
        assertEquals("joao@email.com", resultado.getNmEmail());
    }

    @Test
    void findByEmail_deveRetornarNull_quandoEmailNaoExiste() {
        when(alunoRepository.findByNmEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        Aluno resultado = alunoService.findByEmail("naoexiste@email.com");

        assertNull(resultado);
    }

    @Test
    void findAlunosComMatriculaVencida_deveRetornarListaCorreta() {
        when(alunoRepository.findAlunosComMatriculaVencida()).thenReturn(List.of(aluno));

        List<Aluno> resultado = alunoService.findAlunosComMatriculaVencida();

        assertEquals(1, resultado.size());
        verify(alunoRepository, times(1)).findAlunosComMatriculaVencida();
    }

    @Test
    void findAlunosComMatriculaAtiva_deveRetornarListaCorreta() {
        when(alunoRepository.findAlunosComMatriculaAtiva()).thenReturn(List.of(aluno));

        List<Aluno> resultado = alunoService.findAlunosComMatriculaAtiva();

        assertEquals(1, resultado.size());
        verify(alunoRepository, times(1)).findAlunosComMatriculaAtiva();
    }

    @Test
    void findByNameAtivo_deveRetornarAlunos_quandoNomeExiste() {
        when(alunoRepository.findByNmNomeContainingIgnoreCaseAndFlAtivoTrue("joão"))
                .thenReturn(List.of(aluno));

        List<Aluno> resultado = alunoService.findByNameAtivo("joão");

        assertEquals(1, resultado.size());
        assertEquals("João Silva", resultado.get(0).getNmNome());
    }

    @Test
    void findByNameAtivo_deveRetornarListaVazia_quandoNomeNaoExiste() {
        when(alunoRepository.findByNmNomeContainingIgnoreCaseAndFlAtivoTrue("inexistente"))
                .thenReturn(List.of());

        List<Aluno> resultado = alunoService.findByNameAtivo("inexistente");

        assertTrue(resultado.isEmpty());
    }
}