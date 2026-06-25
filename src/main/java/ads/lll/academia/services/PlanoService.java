package ads.lll.academia.services;

import ads.lll.academia.models.Plano;
import ads.lll.academia.repositories.PlanoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PlanoService {

    private final PlanoRepository planoRepository;

    public PlanoService(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    public Plano create(String nome, BigDecimal valor, Integer duracaoMeses) {

        if (planoRepository.existsByNmNomeIgnoreCase(nome)) {
            throw new IllegalArgumentException("Já existe um plano com esse nome.");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do plano deve ser maior que zero.");
        }

        if (duracaoMeses <= 0) {
            throw new IllegalArgumentException("A duração deve ser maior que zero.");
        }

        Plano plano = new Plano();

        plano.setNmNome(nome);
        plano.setVlValor(valor);
        plano.setNrDuracaoMeses(duracaoMeses);

        planoRepository.save(plano);

        return plano;
    }

    public List<Plano> findAll() {
        return (List<Plano>) planoRepository.findAll();
    }

    public Plano findById(Integer id) {
        return planoRepository.findById(id).orElse(null);
    }

    public Plano findByNome(String nome) {
        return planoRepository.findByNmNomeIgnoreCase(nome)
                .orElse(null);
    }

    public Plano update(Integer id, String nome, BigDecimal valor, Integer duracaoMeses) {

        Plano plano = planoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Plano não encontrado."));

        plano.setNmNome(nome);
        plano.setVlValor(valor);
        plano.setNrDuracaoMeses(duracaoMeses);

        planoRepository.save(plano);

        return plano;
    }

    public void delete(Integer id) {

        Plano plano = planoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Plano não encontrado."));

        planoRepository.delete(plano);
    }

    public List<Plano> findPlanosComMatriculasAtivas() {
        return planoRepository.findPlanosComMatriculasAtivas();
    }

    public List<Object[]> countAlunosPorPlano() {
        return planoRepository.countAlunosPorPlano();
    }
}