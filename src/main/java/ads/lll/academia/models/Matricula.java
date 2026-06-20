package ads.lll.academia.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "matricula", schema = "academia")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_matricula", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno idAluno;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_plano", nullable = false)
    private Plano idPlano;

    @Column(name = "dt_vencimento", nullable = false)
    private LocalDate dtVencimento;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ATIVA'")
    @Column(name = "tp_status", nullable = false, length = 20)
    private TpStatus tpStatus = TpStatus.ATIVA;

    public enum TpStatus {
        ATIVA,
        VENCIDA,
        CANCELADA
    }

    @PrePersist
    public void prePersist() {
        if (tpStatus == null)     tpStatus = TpStatus.ATIVA;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Aluno: " + idAluno.getNmNome() +
                " | Plano: " + idPlano.getNmNome() +
                " | Vencimento: " + dtVencimento +
                " | Status: " + tpStatus;
    }
}