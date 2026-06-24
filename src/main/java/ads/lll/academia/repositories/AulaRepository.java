package ads.lll.academia.repositories;

import ads.lll.academia.models.Aula;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulaRepository extends CrudRepository<Aula, Integer> {
    List<Aula> findByFlAtivaTrue();

    @Query(value = """
        SELECT a.* FROM aula a
        WHERE a.fl_ativa = true
          AND (a.nr_dias_semana & :diaBitmask) > 0
          AND (
              SELECT COUNT(ag.id_agendamento)
              FROM agendamento ag
              WHERE ag.id_aula = a.id_aula
                AND ag.dt_aula = :data
                AND ag.tp_status = 'CONFIRMADO'
          ) < a.nr_capacidade_maxima
        ORDER BY a.hr_inicio
    """, nativeQuery = true)
    List<Aula> findAulasComVagasNaData(int diaBitmask, java.time.LocalDate data);
}