'''# Gerenciamento de Tarefas

Este projeto é um sistema de gerenciamento de tarefas desenvolvido em Java, utilizando Gradle como ferramenta de build e Liquibase para gerenciamento de migrações de banco de dados. O sistema permite criar, consultar, atualizar e excluir tarefas, interagindo com um banco de dados MySQL.

## Tecnologias Utilizadas

*   **Linguagem:** Java
*   **Build:** Gradle
*   **Banco de Dados:** MySQL
*   **Migrações:** Liquibase
*   **Bibliotecas:**
    *   Lombok (para redução de código boilerplate)
    *   JUnit (para testes unitários)
    *   MySQL Connector/J (para conexão com o banco de dados)

## Funcionalidades

O sistema oferece as seguintes funcionalidades básicas para o gerenciamento de tarefas (inferido pela estrutura do projeto):

*   Criação de novas tarefas.
*   Listagem de tarefas existentes.
*   Atualização de informações de tarefas.
*   Exclusão de tarefas.
*   Possivelmente, funcionalidades adicionais como busca por status, prioridade, etc.

## Pré-requisitos

Antes de começar, certifique-se de ter instalado em seu ambiente:

*   Java Development Kit (JDK) (versão compatível com o projeto)
*   Gradle
*   Um servidor de banco de dados MySQL

## Instalação e Execução

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/wellinbraga/Gerenciamento_de_Tarefas.git
    cd Gerenciamento_de_Tarefas
    ```

2.  **Configure o Banco de Dados:**
    *   Crie um banco de dados MySQL para a aplicação.
    *   Configure as credenciais de acesso ao banco de dados. (Verificar onde a configuração está localizada, provavelmente em `src/main/resources` ou requer configuração via variáveis de ambiente/arquivo de propriedades específico - *Nota: A localização exata precisa ser confirmada no código*).

3.  **Execute as Migrações:**
    O Liquibase é utilizado para gerenciar o schema do banco de dados. Execute as migrações para criar as tabelas necessárias. Geralmente, isso pode ser feito através de um comando Gradle específico (ex: `./gradlew update`) ou integrado à inicialização da aplicação. (*Nota: Verificar a configuração do Liquibase no `build.gradle.kts` ou arquivos de configuração*).

4.  **Compile e Execute:**
    Utilize o Gradle para compilar e executar a aplicação:
    ```bash
    ./gradlew build
    ./gradlew run 
    ```
    (Os comandos exatos podem variar dependendo da configuração do projeto).

## Estrutura do Projeto

O projeto segue uma estrutura padrão de projetos Gradle e Java, com uma arquitetura em camadas:

```
Gerenciamento_de_Tarefas/
├── .gradle/               # Arquivos do Gradle
├── .idea/                 # Arquivos de configuração do IntelliJ IDEA
├── gradle/wrapper/        # Gradle Wrapper
├── src/
│   ├── main/
│   │   ├── java/          # Código fonte Java
│   │   │   └── br/com/stackmob/
│   │   │       ├── dto/         # Data Transfer Objects
│   │   │       ├── exception/   # Classes de exceção personalizadas
│   │   │       ├── persistence/ # Camada de acesso a dados (DAO, Repositórios)
│   │   │       ├── service/     # Camada de lógica de negócios
│   │   │       └── ui/          # Camada de interface com o usuário (console, web, etc.)
│   │   └── resources/     # Arquivos de recursos (configurações, scripts SQL/Liquibase)
│   └── test/              # Código de testes
├── build.gradle.kts       # Script de build do Gradle
├── gradlew                # Gradle Wrapper (Linux/macOS)
├── gradlew.bat            # Gradle Wrapper (Windows)
├── liquibase.log          # Log do Liquibase
├── settings.gradle.kts    # Configurações do Gradle
└── README.md              # Este arquivo
```

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.

## Autor

*   **Wellington Braga** - [wellinbraga](https://github.com/wellinbraga)
'''
