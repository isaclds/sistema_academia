package ads.lll.academia.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agendamento", schema = "academia")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno idAluno;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_aula", nullable = false)
    private Aula idAula;

    @Column(name = "dt_aula", nullable = false)
    private LocalDate dtAula;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PRESENTE'")
    @Column(name = "tp_status", nullable = false, length = 20)
    private TpStatus tpStatus = TpStatus.PRESENTE;

    public enum TpStatus {
        CONFIRMADO,
        CANCELADO,
        PRESENTE
    }

    @PrePersist
    public void prePersist() {
        if (tpStatus == null)   tpStatus = TpStatus.PRESENTE;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Aluno: " + idAluno.getNmNome() +
                " | Aula: " + idAula.getNmNome() +
                " | Data: " + dtAula +
                " | Status: " + tpStatus;
    }
}