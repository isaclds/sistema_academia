package ads.lll.academia.repositories;

import ads.lll.academia.models.Aluno;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Integer> {

    Optional<Aluno> findByNrCpf(String nrCpf);

    boolean existsByNrCpf(String nrCpf);

    boolean existsByNmEmail(String nmEmail);

    List<Aluno> findByNmNomeContainingIgnoreCaseAndFlAtivoTrue(String nmNome);

    @Query("""
        SELECT DISTINCT a
        FROM Aluno a
        JOIN Matricula m ON m.idAluno = a
        WHERE m.tpStatus = 'ATIVA'
          AND m.dtVencimento >= CURRENT_DATE
          AND a.flAtivo = true
        ORDER BY a.nmNome
    """)
    List<Aluno> findAlunosComMatriculaAtiva();

    @Query("""
        SELECT DISTINCT a
        FROM Aluno a
        JOIN Matricula m ON m.idAluno = a
        WHERE m.dtVencimento < CURRENT_DATE
          AND m.tpStatus = 'ATIVA'
          AND a.flAtivo = true
        ORDER BY a.nmNome
    """)
    List<Aluno> findAlunosComMatriculaVencida();
}