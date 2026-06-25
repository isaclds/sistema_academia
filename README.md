# Sistema de Gerenciamento de Academia

## Sobre o Projeto

O Sistema de Gerenciamento de Academia tem como objetivo facilitar o controle das principais operações de uma academia, permitindo o gerenciamento de alunos, planos, matrículas, aulas e agendamentos.

O sistema foi desenvolvido utilizando **Java**, **Spring Boot**, **Spring Data JPA** e **MySQL**, seguindo boas práticas de desenvolvimento, arquitetura em camadas e testes unitários.

---

## Funcionalidades

### Alunos
- Cadastro de alunos
- Busca por CPF
- Busca por e-mail
- Busca por nome
- Listagem de alunos
- Consulta de alunos com matrícula ativa
- Consulta de alunos com matrícula vencida

### Planos
- Cadastro de planos
- Busca por nome
- Listagem de planos
- Consulta de planos com matrículas ativas
- Quantidade de alunos por plano

### Matrículas
- Criar matrícula
- Cancelar matrícula
- Atualizar matrículas vencidas
- Listar matrículas
- Consultar matrículas ativas
- Consultar matrículas vencidas

### Aulas
- Cadastro de aulas
- Consulta por ID
- Listagem de aulas
- Remoção de aulas

### Agendamentos
- Agendamento de aulas
- Cancelamento de agendamentos
- Histórico de agendamentos
- Próximos agendamentos do aluno

---

## Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- JUnit 5
- Mockito
- Lombok

---

## Estrutura do Projeto

```
src
├── main
│   ├── java
│   │   ├── controllers
│   │   ├── models
│   │   ├── repositories
│   │   ├── services
│   │   └── views
│   └── resources
│
└── test
    └── java
        └── services
```

---

## Arquitetura

O projeto segue uma arquitetura em camadas:

- **Model** → Representação das entidades.
- **Repository** → Acesso ao banco de dados utilizando Spring Data JPA.
- **Service** → Implementação das regras de negócio.
- **View/Menu** → Interface via terminal para interação com o usuário.

---

## Testes

Foram implementados testes unitários utilizando:

- JUnit 5
- Mockito

Os testes cobrem as principais regras de negócio dos Services, simulando o comportamento dos repositórios através de mocks.

---

## Diagramas

O projeto possui a documentação UML contendo:

- Diagrama de Classes
- Diagrama de Casos de Uso
- Diagrama de Atividades

---

## Banco de Dados

O sistema utiliza **MySQL** como banco de dados.

### Docker Compose

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: academia-db
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: academia
      MYSQL_USER: aluno
      MYSQL_PASSWORD: aluno
    ports:
      - "3306:3306"
```

Subir o banco:

```bash
docker compose up -d
```

---

## Desenvolvido por
- Bernardo Amaral Lisboa
- Daniel Luiz da Rocha Cordeiro
- Isac Lehmkuhl dos Santos

Projeto desenvolvido para a matéria de **Engenharia de Software I**.
