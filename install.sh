#!/bin/bash

# Mensagem inicial
echo "Iniciando a instalação do SisCaLivros..."

# Atualizar o sistema
echo "Atualizando pacotes do sistema..."
sudo dnf update -y

# Instalar dependências do sistema
echo "Instalando dependências do sistema..."
sudo dnf install -y java-21-openjdk java-21-openjdk-devel sqlite3 unzip wget

# Configurar JavaFX
echo "Configurando o JavaFX..."
JAVAFX_VERSION="21.0.5"
JAVAFX_DIR="$HOME/libs/javafx-sdk"
JAVAFX_ZIP="javafx-sdk-$JAVAFX_VERSION.zip"

# Criar diretório para bibliotecas, se não existir
mkdir -p "$HOME/libs"

# Baixar JavaFX, se não existir
if [ ! -f "$HOME/libs/$JAVAFX_ZIP" ]; then
    wget -q "https://download2.gluonhq.com/javafx-$JAVAFX_VERSION/$JAVAFX_ZIP" -O "$HOME/libs/$JAVAFX_ZIP"
fi

# Extrair JavaFX, se não estiver configurado
if [ ! -d "$JAVAFX_DIR/javafx-sdk-$JAVAFX_VERSION" ]; then
    unzip -q "$HOME/libs/$JAVAFX_ZIP" -d "$JAVAFX_DIR"
fi

# Configurar SQLite JDBC
echo "Configurando a biblioteca SQLite JDBC..."
SQLITE_JDBC_VERSION="3.47.1.0"
SQLITE_JDBC_JAR="sqlite-jdbc-$SQLITE_JDBC_VERSION.jar"
SQLITE_JDBC_DIR="$HOME/libs/sqlite"

# Criar diretório para a biblioteca SQLite
mkdir -p "$SQLITE_JDBC_DIR"

# Baixar SQLite JDBC, se não existir
if [ ! -f "$SQLITE_JDBC_DIR/$SQLITE_JDBC_JAR" ]; then
    wget -q "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/$SQLITE_JDBC_VERSION/$SQLITE_JDBC_JAR" -O "$SQLITE_JDBC_DIR/$SQLITE_JDBC_JAR"
fi

# Configurar variáveis de ambiente
echo "Configurando variáveis de ambiente..."
echo "export JAVAFX_HOME=$JAVAFX_DIR/javafx-sdk-$JAVAFX_VERSION" >> ~/.bashrc
echo "export SQLITE_JDBC=$SQLITE_JDBC_DIR/$SQLITE_JDBC_JAR" >> ~/.bashrc
source ~/.bashrc

# Organizar diretórios do projeto
echo "Organizando diretórios do projeto..."
mkdir -p bin

# Compilar o projeto
echo "Compilando o projeto..."
./compile.sh

# Finalização
if [ $? -eq 0 ]; then
    echo "Instalação concluída com sucesso! Para executar o projeto, use './run.sh'."
else
    echo "Erro durante a instalação. Verifique os logs acima."
fi
