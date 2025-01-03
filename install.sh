#!/bin/bash

# Mensagem inicial
echo "Iniciando a instalação do SisCaLivros..."

# Atualizar o sistema
echo "Atualizando pacotes do sistema..."
sudo dnf update -y

# Instalar dependências do sistema
echo "Instalando dependências do sistema..."
sudo dnf install -y java-21-openjdk java-21-openjdk-devel sqlite3 unzip wget gtk-update-icon-cache

# Configurar JavaFX
echo "Configurando o JavaFX..."
JAVAFX_VERSION="21.0.5"
JAVAFX_DIR="/usr/local/share/javafx-sdk"
JAVAFX_ZIP="javafx-sdk-$JAVAFX_VERSION.zip"

# Baixar e configurar JavaFX
if [ ! -d "$JAVAFX_DIR" ]; then
    sudo mkdir -p "$JAVAFX_DIR"
    wget -q "https://download2.gluonhq.com/openjfx/21.0.5/openjfx-21.0.5_linux-x64_bin-sdk.zip" -O "/tmp/$JAVAFX_ZIP"
    sudo unzip -q "/tmp/$JAVAFX_ZIP" -d "/usr/local/share/"
    sudo mv "/usr/local/share/javafx-sdk-$JAVAFX_VERSION" "$JAVAFX_DIR"
    rm "/tmp/$JAVAFX_ZIP"
    echo "JavaFX configurado em $JAVAFX_DIR."
else
    echo "JavaFX já configurado em $JAVAFX_DIR."
fi

# Configurar SQLite JDBC
echo "Configurando a biblioteca SQLite JDBC..."
SQLITE_JDBC_VERSION="3.47.1.0"
SQLITE_JDBC_JAR="sqlite-jdbc-$SQLITE_JDBC_VERSION.jar"
SQLITE_JDBC_DIR="/usr/local/share/sqlite"

# Baixar e configurar SQLite JDBC
if [ ! -d "$SQLITE_JDBC_DIR" ]; then
    sudo mkdir -p "$SQLITE_JDBC_DIR"
    wget -q "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/$SQLITE_JDBC_VERSION/$SQLITE_JDBC_JAR" -O "$SQLITE_JDBC_DIR/$SQLITE_JDBC_JAR"
    echo "SQLite JDBC configurado em $SQLITE_JDBC_DIR."
else
    echo "SQLite JDBC já configurado em $SQLITE_JDBC_DIR."
fi

# Compilar o projeto na pasta de clonagem
echo "Compilando o projeto..."
./compile.sh

# Verificar a compilação
if [ $? -eq 0 ]; then
    echo "Compilação concluída com sucesso!"
else
    echo "Erro na compilação. Verifique os logs acima."
    exit 1
fi

# Mover os arquivos compilados para o diretório final
APP_DIR="/usr/local/share/SisCaLivros"
BIN_DIR="$APP_DIR/bin"

echo "Instalando arquivos no diretório final..."
sudo rm -rf "$APP_DIR"
sudo mkdir -p "$BIN_DIR"
sudo cp -r bin/* "$BIN_DIR"
sudo cp -r Resources "$APP_DIR"
sudo cp -r images "$APP_DIR"
sudo cp run.sh "$APP_DIR"

# Criar link simbólico para o script de execução
echo "Criando link simbólico para execução..."
sudo ln -sf "$APP_DIR/run.sh" /usr/local/bin/SisCaLivros

# Configurar ícone no Dashboard
echo "Configurando o ícone no Dashboard..."
ICON_SRC="$APP_DIR/images/siscalivros.png"
ICON_DEST="/usr/share/icons/hicolor/48x48/apps/siscalivros.png"

if [ -f "$ICON_SRC" ]; then
    sudo cp "$ICON_SRC" "$ICON_DEST"
    sudo gtk-update-icon-cache /usr/share/icons/hicolor
    echo "Ícone configurado com sucesso!"
else
    echo "Aviso: Ícone não encontrado em '$ICON_SRC'."
fi

# Criar o arquivo .desktop
echo "Criando o arquivo SisCaLivros.desktop..."
sudo bash -c "cat <<EOL > /usr/share/applications/SisCaLivros.desktop
[Desktop Entry]
Name=SisCaLivros
Comment=Organizador de livros de biblioteca
Exec=/usr/local/bin/SisCaLivros
Icon=siscalivros
Terminal=false
Type=Application
Categories=Office;Education;Java;
EOL"

sudo update-desktop-database

# Finalização
echo "Instalação concluída com sucesso! Para executar o aplicativo, use o menu de aplicativos ou o comando 'SisCaLivros'."
