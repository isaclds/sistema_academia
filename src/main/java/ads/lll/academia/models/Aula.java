package ads.lll.academia.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aula", schema = "academia")
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aula", nullable = false)
    private Integer id;

    @Column(name = "nm_nome", nullable = false, length = 100)
    private String nmNome;

    @Column(name = "nm_professor", nullable = false, length = 150)
    private String nmProfessor;

    @Column(name = "hr_inicio", nullable = false)
    private LocalTime hrInicio;

    @Column(name = "hr_fim", nullable = false)
    private LocalTime hrFim;

    @Column(name = "nr_capacidade_maxima", nullable = false)
    private Integer nrCapacidadeMaxima;

    @Override
    public String toString() {
        return "ID: " + id +
                " | Nome: " + nmNome +
                " | Instrutor: " +  nmProfessor +
                " | Horário: " + hrInicio + " - " + hrFim +
                " | Capacidade: " + nrCapacidadeMaxima;
    }
}