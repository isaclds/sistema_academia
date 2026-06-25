package ads.lll.academia.services;

import ads.lll.academia.models.Aluno;
import ads.lll.academia.repositories.AlunoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        alunoRepository.save(aluno);
        return aluno;
    }

    public List<Aluno> findAll() {
        return (List<Aluno>) alunoRepository.findAll();
    }

    public Aluno findByCpf(String cpf) {
        return alunoRepository.findByNrCpf(cpf).orElse(null);
    }

    public Aluno findByEmail(String email) {
        return alunoRepository.findByNmEmail(email).orElse(null);
    }

    public List<Aluno> findAlunosComMatriculaVencida(){
        return alunoRepository.findAlunosComMatriculaVencida();
    };

    public List<Aluno> findAlunosComMatriculaAtiva(){
        return alunoRepository.findAlunosComMatriculaAtiva();
    };

    public List<Aluno> findByNameAtivo(String name){
        return alunoRepository.findByNmNomeContainingIgnoreCaseAndFlAtivoTrue(name);
    }
}
