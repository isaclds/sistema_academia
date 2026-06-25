package ads.lll.academia.services;

import ads.lll.academia.models.Agendamento;
import ads.lll.academia.models.Aluno;
import ads.lll.academia.models.Aula;
import ads.lll.academia.repositories.AgendamentoRepository;
import ads.lll.academia.repositories.AlunoRepository;
import ads.lll.academia.repositories.AulaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final MatriculaService matriculaService;
    private final AlunoRepository alunoRepository;
    private final AulaRepository aulaRepository;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            AlunoRepository alunoRepository,
            AulaRepository aulaRepository, MatriculaService matriculaService) {

        this.agendamentoRepository = agendamentoRepository;
        this.alunoRepository = alunoRepository;
        this.aulaRepository = aulaRepository;
        this.matriculaService = matriculaService;
    }

    public Agendamento create(Integer idAluno, Integer idAula, LocalDate dataAula) {

        Aluno aluno = alunoRepository.findById(idAluno).orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        Aula aula = aulaRepository.findById(idAula).orElseThrow(() -> new IllegalArgumentException("Aula não encontrada."));

        if (!matriculaService.possuiMatriculaAtiva(idAluno)) {
            throw new IllegalArgumentException("O aluno não possui matrícula ativa.");
        }

        Agendamento agendamento = new Agendamento();

        agendamento.setIdAluno(aluno);
        agendamento.setIdAula(aula);
        agendamento.setDtAula(dataAula);
        agendamento.setTpStatus(Agendamento.TpStatus.CONFIRMADO);

        agendamentoRepository.save(agendamento);

        return agendamento;
    }

    public List<Agendamento> findAll() {
        return (List<Agendamento>) agendamentoRepository.findAll();
    }

    public Agendamento findById(Integer id) {
        return agendamentoRepository.findById(id).orElse(null);
    }

    public List<Agendamento> findProximosAgendamentos(Integer idAluno) {

        Aluno aluno = alunoRepository.findById(idAluno).orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        return agendamentoRepository.findByIdAlunoAndDtAulaGreaterThanEqualOrderByDtAulaAsc(aluno, LocalDate.now());
    }

    public List<Agendamento> findHistoricoAluno(Integer idAluno) {

        Aluno aluno = alunoRepository.findById(idAluno).orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        return agendamentoRepository.findByIdAlunoOrderByDtAulaDesc(aluno);
    }

    public Agendamento cancelar(Integer id) {

        Agendamento agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado."));

        agendamento.setTpStatus(Agendamento.TpStatus.CANCELADO);

        agendamentoRepository.save(agendamento);

        return agendamento;
    }

    public void delete(Integer id) {

        Agendamento agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado."));

        agendamentoRepository.delete(agendamento);
    }
}