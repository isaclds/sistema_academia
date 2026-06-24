package ads.lll.academia.repositories;

import ads.lll.academia.models.Agendamento;
import ads.lll.academia.models.Aluno;
import ads.lll.academia.models.Aula;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends CrudRepository<Agendamento, Integer> {

    // Agendamentos futuros de um aluno
    List<Agendamento> findByIdAlunoAndDtAulaGreaterThanEqualOrderByDtAulaAsc(
            Aluno idAluno, LocalDate dtAula);

    List<Agendamento> findByIdAlunoOrderByDtAulaDesc(Aluno idAluno);
}