package ads.lll.academia.views;

import ads.lll.academia.services.*;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

@Component
public class Menu {

    private final AlunoService alunoService;
    private final PlanoService planoService;
    private final AulaService aulaService;
    private final MatriculaService matriculaService;
    private final AgendamentoService agendamentoService;

    private final Scanner sc = new Scanner(System.in);

    public Menu(
            AlunoService alunoService,
            PlanoService planoService,
            AulaService aulaService,
            MatriculaService matriculaService,
            AgendamentoService agendamentoService
    ) {
        this.alunoService = alunoService;
        this.planoService = planoService;
        this.aulaService = aulaService;
        this.matriculaService = matriculaService;
        this.agendamentoService = agendamentoService;
    }

    public void run() {

        int opcao;

        do {
            System.out.println("\n===== SISTEMA ACADEMIA =====");
            System.out.println("1 - Alunos");
            System.out.println("2 - Planos");
            System.out.println("3 - Aulas");
            System.out.println("4 - Matrículas");
            System.out.println("5 - Agendamentos");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {
                case 1 -> menuAlunos();
                case 2 -> menuPlanos();
                case 3 -> menuAulas();
                case 4 -> menuMatriculas();
                case 5 -> menuAgendamentos();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    private void menuAlunos() {

        int op;

        do {

            System.out.println("\n===== ALUNOS =====");
            System.out.println("1 - Cadastrar aluno");
            System.out.println("2 - Buscar por CPF");
            System.out.println("3 - Buscar por Email");
            System.out.println("4 - Buscar por Nome");
            System.out.println("5 - Listar todos");
            System.out.println("6 - Alunos com matrícula ativa");
            System.out.println("7 - Alunos com matrícula vencida");
            System.out.println("0 - Voltar");

            op = Integer.parseInt(sc.nextLine());

            switch (op) {

                case 1 -> {
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();

                    System.out.print("CPF: ");
                    String cpf = sc.nextLine();

                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    alunoService.create(nome, cpf, email);
                }

                case 2 -> {
                    System.out.print("CPF: ");
                    String cpf = sc.nextLine();

                    System.out.println(
                            alunoService.findByCpf(cpf)
                    );
                }

                case 3 -> {
                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    System.out.println(
                            alunoService.findByEmail(email)
                    );
                }

                case 4 -> {
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();

                    alunoService.findByNameAtivo(nome)
                            .forEach(System.out::println);
                }

                case 5 -> alunoService.findAll()
                        .forEach(System.out::println);

                case 6 -> alunoService.findAlunosComMatriculaAtiva()
                        .forEach(System.out::println);

                case 7 -> alunoService.findAlunosComMatriculaVencida()
                        .forEach(System.out::println);
            }

        } while (op != 0);
    }

    private void menuPlanos() {

        int op;

        do {

            System.out.println("\n===== PLANOS =====");
            System.out.println("1 - Cadastrar plano");
            System.out.println("2 - Buscar por nome");
            System.out.println("3 - Listar todos");
            System.out.println("4 - Planos com matrículas ativas");
            System.out.println("5 - Quantidade de alunos por plano");
            System.out.println("0 - Voltar");

            op = Integer.parseInt(sc.nextLine());

            switch (op) {

                case 1 -> {

                    System.out.print("Nome: ");
                    String nome = sc.nextLine();

                    System.out.print("Valor: ");
                    BigDecimal valor =
                            new BigDecimal(sc.nextLine());

                    System.out.print("Duração (meses): ");
                    Integer duracao =
                            Integer.parseInt(sc.nextLine());

                    planoService.create(
                            nome,
                            valor,
                            duracao
                    );
                }

                case 2 -> {

                    System.out.print("Nome: ");
                    String nome = sc.nextLine();

                    System.out.println(
                            planoService.findByNome(nome)
                    );
                }

                case 3 -> planoService.findAll()
                        .forEach(System.out::println);

                case 4 -> planoService.findPlanosComMatriculasAtivas()
                        .forEach(System.out::println);

                case 5 -> {

                    List<Object[]> dados =
                            planoService.countAlunosPorPlano();

                    for (Object[] linha : dados) {
                        System.out.println(
                                linha[0] + " -> " + linha[1]
                        );
                    }
                }
            }

        } while (op != 0);
    }

    private void menuAulas() {

        int op;

        do {

            System.out.println("\n===== AULAS =====");
            System.out.println("1 - Cadastrar aula");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Listar todas");
            System.out.println("4 - Remover aula");
            System.out.println("0 - Voltar");

            op = Integer.parseInt(sc.nextLine());

            switch (op) {

                case 1 -> {

                    System.out.print("Nome: ");
                    String nome = sc.nextLine();

                    System.out.print("Professor: ");
                    String professor = sc.nextLine();

                    System.out.print("Hora início (HH:mm): ");
                    LocalTime inicio =
                            LocalTime.parse(sc.nextLine());

                    System.out.print("Hora fim (HH:mm): ");
                    LocalTime fim =
                            LocalTime.parse(sc.nextLine());

                    System.out.print("Capacidade: ");
                    Integer capacidade =
                            Integer.parseInt(sc.nextLine());

                    aulaService.create(
                            nome,
                            professor,
                            inicio,
                            fim,
                            capacidade
                    );
                }

                case 2 -> {

                    System.out.print("ID: ");
                    Integer id =
                            Integer.parseInt(sc.nextLine());

                    System.out.println(
                            aulaService.findById(id)
                    );
                }

                case 3 -> aulaService.findAll()
                        .forEach(System.out::println);

                case 4 -> {

                    System.out.print("ID: ");
                    Integer id =
                            Integer.parseInt(sc.nextLine());

                    aulaService.delete(id);
                }
            }

        } while (op != 0);
    }


    private void menuMatriculas() {

        int op;

        do {

            System.out.println("\n===== MATRÍCULAS =====");
            System.out.println("1 - Criar matrícula");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Listar todas");
            System.out.println("4 - Listar ativas");
            System.out.println("5 - Listar vencidas");
            System.out.println("6 - Cancelar matrícula");
            System.out.println("7 - Atualizar vencidas");
            System.out.println("0 - Voltar");

            op = Integer.parseInt(sc.nextLine());

            switch (op) {

                case 1 -> {

                    System.out.print("ID aluno: ");
                    Integer aluno =
                            Integer.parseInt(sc.nextLine());

                    System.out.print("ID plano: ");
                    Integer plano =
                            Integer.parseInt(sc.nextLine());

                    matriculaService.create(
                            aluno,
                            plano
                    );
                }

                case 2 -> {

                    System.out.print("ID: ");
                    Integer id =
                            Integer.parseInt(sc.nextLine());

                    System.out.println(
                            matriculaService.findById(id)
                    );
                }

                case 3 -> matriculaService.findAll()
                        .forEach(System.out::println);

                case 4 -> matriculaService.findAtivas()
                        .forEach(System.out::println);

                case 5 -> matriculaService.findVencidas()
                        .forEach(System.out::println);

                case 6 -> {

                    System.out.print("ID: ");
                    Integer id =
                            Integer.parseInt(sc.nextLine());

                    matriculaService.cancelar(id);
                }

                case 7 -> {
                    int qtd =
                            matriculaService.atualizarVencidas();

                    System.out.println(
                            qtd + " matrículas atualizadas."
                    );
                }
            }

        } while (op != 0);
    }

    private void menuAgendamentos() {

        int op;

        do {

            System.out.println("\n===== AGENDAMENTOS =====");
            System.out.println("1 - Agendar aula");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Listar todos");
            System.out.println("4 - Próximos agendamentos do aluno");
            System.out.println("5 - Histórico do aluno");
            System.out.println("6 - Cancelar agendamento");
            System.out.println("0 - Voltar");

            op = Integer.parseInt(sc.nextLine());

            switch (op) {

                case 1 -> {

                    System.out.print("ID aluno: ");
                    Integer aluno =
                            Integer.parseInt(sc.nextLine());

                    System.out.print("ID aula: ");
                    Integer aula =
                            Integer.parseInt(sc.nextLine());

                    System.out.print("Data (AAAA-MM-DD): ");
                    LocalDate data =
                            LocalDate.parse(sc.nextLine());

                    agendamentoService.create(
                            aluno,
                            aula,
                            data
                    );
                }

                case 2 -> {

                    System.out.print("ID: ");
                    Integer id =
                            Integer.parseInt(sc.nextLine());

                    System.out.println(
                            agendamentoService.findById(id)
                    );
                }

                case 3 -> agendamentoService.findAll()
                        .forEach(System.out::println);

                case 4 -> {

                    System.out.print("ID aluno: ");
                    Integer id =
                            Integer.parseInt(sc.nextLine());

                    agendamentoService
                            .findProximosAgendamentos(id)
                            .forEach(System.out::println);
                }

                case 5 -> {

                    System.out.print("ID aluno: ");
                    Integer id =
                            Integer.parseInt(sc.nextLine());

                    agendamentoService
                            .findHistoricoAluno(id)
                            .forEach(System.out::println);
                }

                case 6 -> {

                    System.out.print("ID: ");
                    Integer id =
                            Integer.parseInt(sc.nextLine());

                    agendamentoService.cancelar(id);
                }
            }

        } while (op != 0);
    }

}