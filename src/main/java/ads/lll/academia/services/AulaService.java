package ads.lll.academia.services;

import ads.lll.academia.models.Aula;
import ads.lll.academia.repositories.AulaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class AulaService {

    private final AulaRepository aulaRepository;

    public AulaService(AulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    public Aula create(String nome, String professor, LocalTime hrInicio, LocalTime hrFim, Integer capacidadeMaxima) {

        if (hrInicio.isAfter(hrFim) || hrInicio.equals(hrFim)) {
            throw new IllegalArgumentException("O horário inicial deve ser anterior ao horário final.");
        }

        if (capacidadeMaxima <= 0) {
            throw new IllegalArgumentException("A capacidade máxima deve ser maior que zero.");
        }
        Aula aula = new Aula();
        aula.setNmNome(nome);
        aula.setNmProfessor(professor);
        aula.setHrInicio(hrInicio);
        aula.setHrFim(hrFim);
        aula.setNrCapacidadeMaxima(capacidadeMaxima);
        aulaRepository.save(aula);

        return aula;
    }

    public List<Aula> findAll() {
        return (List<Aula>) aulaRepository.findAll();
    }

    public Aula findById(Integer id) {
        return aulaRepository.findById(id).orElse(null);
    }

    public Aula update(Integer id, String nome, String professor, LocalTime hrInicio, LocalTime hrFim, Integer capacidadeMaxima) {
        Aula aula = aulaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Aula não encontrada."));

        aula.setNmNome(nome);
        aula.setNmProfessor(professor);
        aula.setHrInicio(hrInicio);
        aula.setHrFim(hrFim);
        aula.setNrCapacidadeMaxima(capacidadeMaxima);
        aulaRepository.save(aula);

        return aula;
    }

    public void delete(Integer id) {

        Aula aula = aulaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Aula não encontrada."));

        aulaRepository.delete(aula);
    }
}