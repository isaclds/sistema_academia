package ads.lll.academia.repositories;

import ads.lll.academia.models.Aluno;
import ads.lll.academia.models.Matricula;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends CrudRepository<Matricula, Integer> {

    Optional<Matricula> findByIdAlunoAndTpStatus(Aluno idAluno, Matricula.TpStatus tpStatus);

    List<Matricula> findByIdAlunoOrderByDtInicioDesc(Aluno idAluno);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Matricula m
        SET m.tpStatus = 'VENCIDA'
        WHERE m.tpStatus = 'ATIVA'
          AND m.dtVencimento < CURRENT_DATE
    """)
    int atualizarMatriculasVencidas();

    List<Matricula> findByTpStatusOrderByDtVencimentoAsc(Matricula.TpStatus tpStatus);
}