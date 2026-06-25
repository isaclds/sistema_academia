package ads.lll.academia.repositories;

import ads.lll.academia.models.Plano;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanoRepository extends CrudRepository<Plano, Integer> {

    boolean existsByNmNomeIgnoreCase(String nmNome);

    @Query("""
        SELECT DISTINCT p
        FROM Plano p
        JOIN Matricula m ON m.idPlano = p
        WHERE m.tpStatus = 'ATIVA'
        ORDER BY p.nmNome
    """)
    List<Plano> findPlanosComMatriculasAtivas();

    @Query("""
        SELECT p.nmNome, COUNT(m.id)
        FROM Plano p
        LEFT JOIN Matricula m ON m.idPlano = p AND m.tpStatus = 'ATIVA'
        GROUP BY p.id, p.nmNome
        ORDER BY COUNT(m.id) DESC
    """)
    List<Object[]> countAlunosPorPlano();

    Optional<Plano> findByNmNomeIgnoreCase(String nmNome);
}