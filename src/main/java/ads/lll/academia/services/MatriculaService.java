package ads.lll.academia.services;

import ads.lll.academia.models.Aluno;
import ads.lll.academia.models.Matricula;
import ads.lll.academia.models.Plano;
import ads.lll.academia.repositories.AlunoRepository;
import ads.lll.academia.repositories.MatriculaRepository;
import ads.lll.academia.repositories.PlanoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final PlanoRepository planoRepository;

    public MatriculaService(MatriculaRepository matriculaRepository, AlunoRepository alunoRepository, PlanoRepository planoRepository) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.planoRepository = planoRepository;
    }

    public Matricula create(Integer idAluno, Integer idPlano) {

        Aluno aluno = alunoRepository.findById(idAluno).orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        Plano plano = planoRepository.findById(idPlano).orElseThrow(() -> new IllegalArgumentException("Plano não encontrado."));

        matriculaRepository.findByIdAlunoAndTpStatus(aluno, Matricula.TpStatus.ATIVA).ifPresent(m -> {throw new IllegalArgumentException("O aluno já possui uma matrícula ativa.");});

        Matricula matricula = new Matricula();

        matricula.setIdAluno(aluno);
        matricula.setIdPlano(plano);
        matricula.setDtInicio(LocalDate.now());
        matricula.setDtVencimento(LocalDate.now().plusMonths(plano.getNrDuracaoMeses()));
        matricula.setTpStatus(Matricula.TpStatus.ATIVA);

        matriculaRepository.save(matricula);

        return matricula;
    }

    public List<Matricula> findAll() {
        return (List<Matricula>) matriculaRepository.findAll();
    }

    public Matricula findById(Integer id) {
        return matriculaRepository.findById(id).orElse(null);
    }

    public List<Matricula> findByAluno(Integer idAluno) {

        Aluno aluno = alunoRepository.findById(idAluno).orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        return matriculaRepository.findByIdAlunoOrderByDtInicioDesc(aluno);
    }

    public List<Matricula> findAtivas() {
        return matriculaRepository.findByTpStatusOrderByDtVencimentoAsc(Matricula.TpStatus.ATIVA);
    }

    public List<Matricula> findVencidas() {
        return matriculaRepository.findByTpStatusOrderByDtVencimentoAsc(Matricula.TpStatus.VENCIDA);
    }

    public Matricula cancelar(Integer id) {

        Matricula matricula = matriculaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Matrícula não encontrada."));
        matricula.setTpStatus(Matricula.TpStatus.CANCELADA);

        matriculaRepository.save(matricula);

        return matricula;
    }

    public boolean possuiMatriculaAtiva(Integer idAluno) {

        Aluno aluno = alunoRepository.findById(idAluno).orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        return matriculaRepository.findByIdAlunoAndTpStatus(aluno, Matricula.TpStatus.ATIVA).isPresent();
    }

    public int atualizarVencidas() {
        return matriculaRepository.atualizarMatriculasVencidas();
    }
}