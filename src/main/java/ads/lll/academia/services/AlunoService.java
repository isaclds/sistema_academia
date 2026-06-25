package ads.lll.academia.services;

import ads.lll.academia.models.Aluno;
import ads.lll.academia.repositories.AlunoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlunoService {

    AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno create(String nome, String cpf, String email) {
        alunoRepository.findByNrCpf(cpf).ifPresent(aluno -> {
            throw new IllegalArgumentException("CPF já cadastrado: " + cpf);
        });
        Aluno aluno = new Aluno();
        aluno.setNrCpf(cpf);
        aluno.setNmEmail(email);
        aluno.setNmNome(nome);
        return alunoRepository.save(aluno);
    }

    public List<Aluno> findAll() {
        return (List<Aluno>) alunoRepository.findAll();
    }

    public Aluno findById(Integer id) {
        return alunoRepository.findById(id).orElse(null);
    }

    public Aluno findByCpf(String cpf) {
        return alunoRepository.findByNrCpf(cpf).orElse(null);
    }

    public Aluno findByEmail(String email) {
        return alunoRepository.findByNmEmail(email).orElse(null);
    }

    public Aluno update(Integer id, String nome, String cpf, String email) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado: " + id));

        if (cpf != null && !cpf.equals(aluno.getNrCpf())) {
            alunoRepository.findByNrCpf(cpf).ifPresent(a -> {
                throw new IllegalArgumentException("CPF já cadastrado: " + cpf);
            });
            aluno.setNrCpf(cpf);
        }

        if (email != null && !email.equals(aluno.getNmEmail())) {
            alunoRepository.findByNmEmail(email).ifPresent(a -> {
                throw new IllegalArgumentException("Email já cadastrado: " + email);
            });
            aluno.setNmEmail(email);
        }

        if (nome != null) aluno.setNmNome(nome);

        return alunoRepository.save(aluno);
    }

    public Aluno toggleAtivo(Integer id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado: " + id));
        aluno.setFlAtivo(!aluno.getFlAtivo());
        return alunoRepository.save(aluno);
    }

    public void delete(Integer id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado: " + id));
        alunoRepository.delete(aluno);
    }

    public List<Aluno> findAlunosComMatriculaVencida() {
        return alunoRepository.findAlunosComMatriculaVencida();
    }

    public List<Aluno> findAlunosComMatriculaAtiva() {
        return alunoRepository.findAlunosComMatriculaAtiva();
    }

    public List<Aluno> findByNameAtivo(String name) {
        return alunoRepository.findByNmNomeContainingIgnoreCaseAndFlAtivoTrue(name);
    }
}