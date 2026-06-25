package ads.lll.academia.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno", nullable = false)
    private Integer id;

    @Column(name = "nm_nome", nullable = false, length = 150)
    private String nmNome;

    @Column(name = "nr_cpf", nullable = false, unique = true, length = 11)
    private String nrCpf;

    @Column(name = "nm_email", nullable = false, unique = true, length = 150)
    private String nmEmail;

    @Column(name = "fl_ativo", nullable = false)
    private Boolean flAtivo = true;

    @PrePersist
    public void prePersist() {
        if (flAtivo == null)    flAtivo = true;
        if (nrCpf != null)      nrCpf = nrCpf.replaceAll("[^0-9]", "");
        if (nmEmail != null)    nmEmail = nmEmail.trim().toLowerCase();
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Nome: " + nmNome +
                " | CPF: " + nrCpf +
                " | Email: " + nmEmail +
                " | Ativo: " + (flAtivo ? "Sim" : "Não");
    }
}