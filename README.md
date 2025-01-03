# SisCaLivros

O **SisCaLivros** é uma aplicação de desktop para organização de livros de uma biblioteca, desenvolvida com Java e JavaFX.

---

## Instalação do SisCaLivros

Você pode instalar o SisCaLivros de duas maneiras:

1. **Clonando o repositório do GitHub.**
2. **A partir dos arquivos fonte.**

### Requisitos gerais

- **Sistema operacional**: Linux ou Windows 10.
- **Java Development Kit (JDK)**: Versão 21 ou superior.
- **JavaFX SDK**: Versão 21.0.5.
- **SQLite JDBC Driver**: Versão 3.47.1.0.

---

### Opção 1: Instalação a partir do GitHub

#### Passos para Linux

1. **Instale o JDK 21**
   ```bash
   sudo apt update
   sudo apt install openjdk-21-jdk
   ```

2. **Baixe e configure o JavaFX SDK**
   - Faça o download do [JavaFX SDK 21.0.5](https://openjfx.io).
   - Extraia o arquivo em `/opt/javafx-sdk`:
     ```bash
     export PATH_TO_FX=/opt/javafx-sdk/lib
     ```

3. **Clone o repositório**
   ```bash
   git clone https://github.com/jpereiratrindade/SisCaLivros.git
   cd SisCaLivros
   ```

4. **Configure o SQLite JDBC Driver**
   - Baixe o [SQLite JDBC Driver](https://bitbucket.org/xerial/sqlite-jdbc/downloads/sqlite-jdbc-3.47.1.0.jar).
   - Coloque-o no diretório `libs`:
     ```bash
     mkdir -p libs
     mv sqlite-jdbc-3.47.1.0.jar libs/
     ```

5. **Compile e execute**
   ```bash
   javac --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -cp libs/sqlite-jdbc-3.47.1.0.jar -d bin src/**/*.java
   java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -cp bin:libs/sqlite-jdbc-3.47.1.0.jar Main
   ```

#### Passos para Windows 10

1. **Baixe e instale o JDK 21**
   - Faça o download do [JDK 21](https://www.oracle.com/java/technologies/javase-downloads.html) e instale-o.
   - Configure as variáveis `JAVA_HOME` e `PATH`:
     ```cmd
     set JAVA_HOME=C:\Program Files\Java\jdk-21
     set PATH=%JAVA_HOME%\bin;%PATH%
     ```

2. **Baixe e configure o JavaFX SDK**
   - Faça o download do [JavaFX SDK 21.0.5](https://openjfx.io).
   - Extraia o arquivo para `C:\javafx-sdk`:
     ```cmd
     set PATH_TO_FX=C:\javafx-sdk\lib
     ```

3. **Clone o repositório**
   ```cmd
   git clone https://github.com/jpereiratrindade/SisCaLivros.git
   cd SisCaLivros
   ```

4. **Configure o SQLite JDBC Driver**
   - Baixe o [SQLite JDBC Driver](https://bitbucket.org/xerial/sqlite-jdbc/downloads/sqlite-jdbc-3.47.1.0.jar).
   - Coloque-o no diretório `libs`:
     ```cmd
     mkdir libs
     move sqlite-jdbc-3.47.1.0.jar libs\
     ```

5. **Compile e execute**
   ```cmd
   javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp libs/sqlite-jdbc-3.47.1.0.jar -d bin src/**/*.java
   java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp bin;libs/sqlite-jdbc-3.47.1.0.jar Main
   ```

---

### Opção 2: Instalação a partir dos arquivos fonte

Se você possui apenas os arquivos fonte e não deseja clonar o repositório, siga estas instruções:

1. **Organize os arquivos**
   - Coloque os arquivos fonte em um diretório organizado da seguinte maneira:
     ```
     SisCaLivros/
     ├── src/
     │   └── (arquivos .java)
     ├── libs/
     │   └── sqlite-jdbc-3.47.1.0.jar
     ```

2. **Baixe o JavaFX SDK e configure o `PATH_TO_FX`**
   - Siga os passos descritos acima para Linux ou Windows.

3. **Compile e execute**
   - No terminal (Linux) ou prompt de comando (Windows), compile e execute o projeto:
     ```bash
     javac --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -cp libs/sqlite-jdbc-3.47.1.0.jar -d bin src/**/*.java
     java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -cp bin:libs/sqlite-jdbc-3.47.1.0.jar Main
     ```

---

### Problemas conhecidos

- Certifique-se de que as variáveis de ambiente `JAVA_HOME` e `PATH_TO_FX` estão configuradas corretamente.
- Para evitar problemas de compatibilidade, utilize as versões recomendadas de Java e JavaFX.

---

### Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou enviar pull requests.

---

### Licença

Este projeto está licenciado sob a [MIT License](LICENSE).
