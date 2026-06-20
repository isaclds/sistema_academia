package ads.lll.academia.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plano", schema = "academia")
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plano", nullable = false)
    private Integer id;

    @Column(name = "nm_nome", nullable = false, length = 100)
    private String nmNome;

    @Column(name = "vl_valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal vlValor;

    @Column(name = "nr_duracao_meses", nullable = false)
    private Integer nrDuracaoMeses;

    @Override
    public String toString() {
        return "ID: " + id +
                " | Nome: " + nmNome +
                " | Valor: R$ " + vlValor +
                " | Duração: " + nrDuracaoMeses + " meses";
    }
}