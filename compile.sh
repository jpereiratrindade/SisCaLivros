#!/bin/bash

# Configuração de caminhos
JAVAFX_HOME="$HOME/libs/javafx-sdk/javafx-sdk-21.0.5"
SQLITE_JDBC="$HOME/libs/sqlite/sqlite-jdbc-3.47.1.0.jar"

# Diretórios do projeto
SRC_DIR="src"
BIN_DIR="bin"
MAIN_DIR="$SRC_DIR/main"
MODEL_DIR="$SRC_DIR/model"

# Certifique-se de que o diretório bin existe
mkdir -p $BIN_DIR

# Comando de compilação
javac \
    --module-path "$JAVAFX_HOME/lib" \
    --add-modules javafx.controls,javafx.fxml \
    -cp "$SQLITE_JDBC:." \
    -d $BIN_DIR \
    $MAIN_DIR/*.java \
    $MODEL_DIR/*.java

# Verificação do status da compilação
if [ $? -eq 0 ]; then
    echo "Compilação concluída com sucesso!"
else
    echo "Erro na compilação."
fi
