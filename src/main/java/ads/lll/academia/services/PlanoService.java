package ads.lll.academia.services;

import ads.lll.academia.models.Plano;
import ads.lll.academia.repositories.PlanoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class PlanoService {

    PlanoRepository planoRepository;

    public PlanoService(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    public Plano create(String nome, BigDecimal valor, Integer duracaoMeses) {
        if (planoRepository.existsByNmNomeIgnoreCase(nome)) {
            throw new IllegalArgumentException("Plano já cadastrado: " + nome);
        }
        Plano plano = new Plano();
        plano.setNmNome(nome);
        plano.setVlValor(valor);
        plano.setNrDuracaoMeses(duracaoMeses);
        return planoRepository.save(plano);
    }

    public List<Plano> findAll() {
        return (List<Plano>) planoRepository.findAll();
    }

    public Plano findById(Integer id) {
        return planoRepository.findById(id).orElse(null);
    }

    public Plano findByNome(String nome) {
        return planoRepository.findByNmNomeIgnoreCase(nome).orElse(null);
    }

    public List<Plano> findPlanosComMatriculasAtivas() {
        return planoRepository.findPlanosComMatriculasAtivas();
    }

    public List<Object[]> countAlunosPorPlano() {
        return planoRepository.countAlunosPorPlano();
    }

    public Plano update(Integer id, String nome, BigDecimal valor, Integer duracaoMeses) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado: " + id));

        if (nome != null && !nome.equalsIgnoreCase(plano.getNmNome())
                && planoRepository.existsByNmNomeIgnoreCase(nome)) {
            throw new IllegalArgumentException("Já existe um plano com o nome: " + nome);
        }

        if (nome != null)         plano.setNmNome(nome);
        if (valor != null)        plano.setVlValor(valor);
        if (duracaoMeses != null) plano.setNrDuracaoMeses(duracaoMeses);

        return planoRepository.save(plano);
    }

    public void delete(Integer id) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado: " + id));
        planoRepository.delete(plano);
    }
}