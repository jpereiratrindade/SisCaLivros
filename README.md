# SisCaLivros

O **SisCaLivros** é uma aplicação de desktop para organização de livros de uma biblioteca, desenvolvida com **Java** e **JavaFX**.

---

## Instalação do SisCaLivros

### Requisitos mínimos

- **Sistema operacional**: Fedora 41.
- **Java Development Kit (JDK)**: Versão 21.
- **JavaFX SDK**: Versão 21.0.5.
- **SQLite JDBC Driver**: Versão 3.47.1.0.
- **Git**: Para clonar o repositório.

---

### Instalação via repositório Git

Siga os passos abaixo para configurar e instalar o projeto no Fedora:

#### 1. Clone o repositório

```bash
git clone https://github.com/jpereiratrindade/SisCaLivros.git
cd SisCaLivros
```

#### 2. Execute o script de instalação

O projeto possui um script automatizado para preparar o ambiente e compilar o código:

```bash
./install.sh
```

Este script:
- Atualiza os pacotes do sistema.
- Instala o **JDK 21**, **SQLite**, e outras dependências.
- Baixa e configura o **JavaFX SDK** e o **SQLite JDBC Driver**.
- Compila o projeto.

#### 3. Execute o aplicativo

Após a instalação bem-sucedida, você pode executar o aplicativo com:

```bash
./run.sh
```

---

### Detalhes dos Scripts

#### **install.sh**
- Automatiza a instalação e configuração do ambiente.
- Instala todas as dependências necessárias.
- Prepara o ambiente para o **SisCaLivros**.

#### **compile.sh**
- Realiza a compilação do código-fonte e organiza os arquivos compilados no diretório `bin`.

Você pode usá-los diretamente caso precise repetir as etapas de instalação ou compilação.

---

### Estrutura do Projeto

```plaintext
SisCaLivros/
├── bin/               # Arquivos compilados (gerados após compilação)
├── images/            # Recursos visuais (ícones, imagens)
├── main/              # Código principal (Main.java)
├── model/             # Classes do modelo
├── Resources/         # Arquivos FXML
│   └── fxml/view.fxml # Interface do usuário
├── compile.sh         # Script para compilar o projeto
├── install.sh         # Script para instalar o projeto
├── run.sh             # Script para executar o projeto
└── README.md          # Documentação do projeto
```

---

### Problemas conhecidos

1. **Erro ao executar o `install.sh`:**
   - Verifique se o arquivo tem permissão de execução:
     ```bash
     chmod +x install.sh
     ```
   - Certifique-se de que as variáveis de ambiente foram carregadas corretamente:
     ```bash
     source ~/.bashrc
     ```

2. **Erro ao executar o `run.sh`:**
   - Verifique se o diretório `bin` contém os arquivos compilados.
   - Certifique-se de que o JavaFX SDK está configurado corretamente.

---

### Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir **issues** ou enviar **pull requests**.

---

### Licença

Este projeto está licenciado sob a [GNU General Public License v3.0](LICENSE).

