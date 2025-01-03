#!/bin/bash

# Mensagem inicial
echo "Iniciando a compilação do projeto SisCaLivros..."

# Configuração de caminhos
JAVAFX_HOME="$HOME/libs/javafx-sdk/javafx-sdk-21.0.5"
SQLITE_JDBC="$HOME/libs/sqlite/sqlite-jdbc-3.47.1.0.jar"

# Diretórios do projeto
SRC_DIR="src"
BIN_DIR="bin"
MAIN_DIR="$SRC_DIR/main"
MODEL_DIR="$SRC_DIR/model"

# Verificar se o JavaFX está configurado
if [ ! -d "$JAVAFX_HOME/lib" ]; then
    echo "Erro: O JavaFX SDK não foi encontrado em $JAVAFX_HOME."
    echo "Certifique-se de que o JavaFX está instalado e configurado corretamente."
    exit 1
fi

# Verificar se o SQLite JDBC está configurado
if [ ! -f "$SQLITE_JDBC" ]; then
    echo "Erro: A biblioteca SQLite JDBC não foi encontrada em $SQLITE_JDBC."
    echo "Certifique-se de que o SQLite JDBC está instalado e configurado corretamente."
    exit 1
fi

# Certifique-se de que o diretório bin existe
mkdir -p $BIN_DIR

# Comando de compilação
echo "Compilando os arquivos Java..."
javac \
    --module-path "$JAVAFX_HOME/lib" \
    --add-modules javafx.controls,javafx.fxml \
    -cp "$SQLITE_JDBC:." \
    -d $BIN_DIR \
    $MAIN_DIR/*.java \
    $MODEL_DIR/*.java

# Verificação do status da compilação
if [ $? -eq 0 ]; then
    echo "Compilação concluída com sucesso! Os arquivos estão em $BIN_DIR."
else
    echo "Erro na compilação. Verifique os arquivos fonte e tente novamente."
    exit 1
fi
