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
        aluno.setId(1);
        aluno.setNmNome("João Silva");
        aluno.setNrCpf("123.456.789-00");
        aluno.setNmEmail("joao@email.com");
        aluno.setFlAtivo(true);
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
        verify(alunoRepository, never()).save(any());
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
    void findById_deveRetornarAluno_quandoIdExiste() {
        when(alunoRepository.findById(1)).thenReturn(Optional.of(aluno));

        Aluno resultado = alunoService.findById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void findById_deveRetornarNull_quandoIdNaoExiste() {
        when(alunoRepository.findById(99)).thenReturn(Optional.empty());

        Aluno resultado = alunoService.findById(99);

        assertNull(resultado);
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
    void update_deveAtualizarERetornarAluno_quandoDadosValidos() {
        when(alunoRepository.findById(1)).thenReturn(Optional.of(aluno));
        when(alunoRepository.findByNrCpf("999.999.999-99")).thenReturn(Optional.empty());
        when(alunoRepository.findByNmEmail("novo@email.com")).thenReturn(Optional.empty());
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        Aluno resultado = alunoService.update(1, "João Atualizado", "999.999.999-99", "novo@email.com");

        assertNotNull(resultado);
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    void update_deveLancarExcecao_quandoIdNaoExiste() {
        when(alunoRepository.findById(99)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> alunoService.update(99, "João", "123.456.789-00", "joao@email.com")
        );

        assertEquals("Aluno não encontrado: 99", ex.getMessage());
        verify(alunoRepository, never()).save(any());
    }

    @Test
    void update_deveLancarExcecao_quandoNovoCpfJaCadastrado() {
        Aluno outro = new Aluno();
        outro.setId(2);
        outro.setNrCpf("999.999.999-99");

        when(alunoRepository.findById(1)).thenReturn(Optional.of(aluno));
        when(alunoRepository.findByNrCpf("999.999.999-99")).thenReturn(Optional.of(outro));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> alunoService.update(1, null, "999.999.999-99", null)
        );

        assertEquals("CPF já cadastrado: 999.999.999-99", ex.getMessage());
        verify(alunoRepository, never()).save(any());
    }

    @Test
    void update_deveLancarExcecao_quandoNovoEmailJaCadastrado() {
        Aluno outro = new Aluno();
        outro.setId(2);
        outro.setNmEmail("outro@email.com");

        when(alunoRepository.findById(1)).thenReturn(Optional.of(aluno));
        when(alunoRepository.findByNmEmail("outro@email.com")).thenReturn(Optional.of(outro));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> alunoService.update(1, null, null, "outro@email.com")
        );

        assertEquals("Email já cadastrado: outro@email.com", ex.getMessage());
        verify(alunoRepository, never()).save(any());
    }

    @Test
    void toggleAtivo_deveDesativarAluno_quandoEstaAtivo() {
        when(alunoRepository.findById(1)).thenReturn(Optional.of(aluno));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        Aluno resultado = alunoService.toggleAtivo(1);

        assertFalse(resultado.getFlAtivo());
        verify(alunoRepository, times(1)).save(aluno);
    }

    @Test
    void toggleAtivo_deveAtivarAluno_quandoEstaInativo() {
        aluno.setFlAtivo(false);
        when(alunoRepository.findById(1)).thenReturn(Optional.of(aluno));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        Aluno resultado = alunoService.toggleAtivo(1);

        assertTrue(resultado.getFlAtivo());
        verify(alunoRepository, times(1)).save(aluno);
    }

    @Test
    void toggleAtivo_deveLancarExcecao_quandoIdNaoExiste() {
        when(alunoRepository.findById(99)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> alunoService.toggleAtivo(99)
        );

        assertEquals("Aluno não encontrado: 99", ex.getMessage());
        verify(alunoRepository, never()).save(any());
    }

    @Test
    void delete_deveDeletarAluno_quandoIdExiste() {
        when(alunoRepository.findById(1)).thenReturn(Optional.of(aluno));

        alunoService.delete(1);

        verify(alunoRepository, times(1)).delete(aluno);
    }

    @Test
    void delete_deveLancarExcecao_quandoIdNaoExiste() {
        when(alunoRepository.findById(99)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> alunoService.delete(99)
        );

        assertEquals("Aluno não encontrado: 99", ex.getMessage());
        verify(alunoRepository, never()).delete(any());
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