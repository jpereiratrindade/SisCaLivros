package main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import model.Book;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class LibraryController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> categoryList;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> genreColumn;

    @FXML
    private TableColumn<Book, String> statusColumn;

    @FXML
    private TableColumn<Book, String> catalogCodeColumn;
    
    @FXML
    private Button newBookButton;

    @FXML
    private Button exportButton;

    private Connection connection;
    // Inicialização
    @FXML
    public void initialize() {
        // Configura categorias no ListView
        categoryList.getItems().addAll("Todos", "Emprestados", "Disponíveis", "Ficção", "Não Ficção");

        // Configura colunas da TableView
        titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());
        authorColumn.setCellValueFactory(data -> data.getValue().authorProperty());
        genreColumn.setCellValueFactory(data -> data.getValue().genreProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
        catalogCodeColumn.setCellValueFactory(data -> data.getValue().catalogCodeProperty());

        // Solicitar que o usuário selecione ou crie um banco de dados
        selectOrCreateDatabase();
        
        // Listener para filtro por categoria
        categoryList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                filterBooksByCategory(newValue);
            }
        });
        
        // Carregar todos os livros assim que o banco for lido
        loadBooks(""); // String vazia para carregar todos os livros        
    }
    private void selectOrCreateDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione ou crie um arquivo de banco de dados");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite Database (*.db)", "*.db"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            connectToDatabase(file.getAbsolutePath());
            initializeDatabase();
        }
    }
    private void connectToDatabase(String dbPath) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            System.out.println("Conectado ao banco de dados: " + dbPath);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erro ao conectar ao banco de dados.");
        }
    }    
    private void initializeDatabase() {
        try (Statement statement = connection.createStatement()) {
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS books (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    author TEXT NOT NULL,
                    genre TEXT NOT NULL,
                    status TEXT NOT NULL,
                    catalog_code TEXT UNIQUE
                )
            """;
            statement.execute(createTableSQL);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erro ao inicializar o banco de dados.");
        }
    }    
    // Método de busca
    @FXML
    private void onSearch() {
        String query = searchField.getText();
        loadBooks(query);
    }

    private void loadBooks(String searchQuery) {
        try {
            bookTable.getItems().clear();
            String sql = "SELECT * FROM books";
            if (!searchQuery.isEmpty()) {
                sql += " WHERE title LIKE ?";
            }

            PreparedStatement statement = connection.prepareStatement(sql);

            if (!searchQuery.isEmpty()) {
                statement.setString(1, "%" + searchQuery + "%");
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                bookTable.getItems().add(new Book(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getString("genre"),
                    resultSet.getString("status"),
                    resultSet.getString("catalog_code")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erro ao carregar os livros.");
        }
    }

    // Método para adicionar novo livros
    @FXML
    private void onNewBook() {
        // Criar um diálogo para entrada de dados
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Novo Livro");
        dialog.setHeaderText("Preencha as informações do novo livro");

        // Criar os campos de entrada
        TextField titleField = new TextField();
        titleField.setPromptText("Título");

        TextField authorField = new TextField();
        authorField.setPromptText("Autor");

        ComboBox<String> genreBox = new ComboBox<>();
        genreBox.getItems().addAll("Ficção", "Não Ficção", "Biografia", "Fantasia", "Ciência");
        genreBox.setPromptText("Gênero");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Disponível", "Emprestado");
        statusBox.setPromptText("Status");

        TextField catalogCodeField = new TextField();
        catalogCodeField.setPromptText("Código de Catalogação");

        // Configurar o layout do diálogo
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Título:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Autor:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Gênero:"), 0, 2);
        grid.add(genreBox, 1, 2);
        grid.add(new Label("Status:"), 0, 3);
        grid.add(statusBox, 1, 3);
        grid.add(new Label("Código de Catalogação:"), 0, 4);
        grid.add(catalogCodeField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Adicionar botões de ação
        ButtonType addButtonType = new ButtonType("Adicionar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Verificar dados ao clicar no botão "Adicionar"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                if (titleField.getText().isEmpty() || authorField.getText().isEmpty() ||
                    genreBox.getValue() == null || statusBox.getValue() == null || catalogCodeField.getText().isEmpty()) {
                    showError("Todos os campos são obrigatórios!");
                    return null;
                }
                return new Book(
                    titleField.getText(),
                    authorField.getText(),
                    genreBox.getValue(),
                    statusBox.getValue(),
                    catalogCodeField.getText()
                );
            }
            return null;
        });

        // Mostrar o diálogo e processar o resultado
        dialog.showAndWait().ifPresent(book -> {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO books (title, author, genre, status, catalog_code) VALUES (?, ?, ?, ?, ?)")) {

                statement.setString(1, book.getTitle());
                statement.setString(2, book.getAuthor());
                statement.setString(3, book.getGenre());
                statement.setString(4, book.getStatus());
                statement.setString(5, book.getCatalogCode());
                statement.executeUpdate();

                loadBooks(""); // Recarregar a tabela
            } catch (Exception e) {
                e.printStackTrace();
                showError("Erro ao adicionar novo livro.");
            }
        });
    }

    // Método para exportar dados
    @FXML
    private void onExport() {
        System.out.println("Exportar dados");
        // Lógica para exportar os dados para CSV ou outro formato
    }
    @FXML
    private void handleQuit() {
        Platform.exit();
    }
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
    @FXML
    private Button editBookButton;

    @FXML
    private void onEditBook() {
        // Verificar se um livro está selecionado
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showError("Selecione um livro para editar.");
            return;
        }

        // Criar um diálogo para edição
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Editar Livro");
        dialog.setHeaderText("Altere as informações do livro");

        // Criar os campos de entrada preenchidos com os dados do livro selecionado
        TextField titleField = new TextField(selectedBook.getTitle());
        titleField.setPromptText("Título");

        TextField authorField = new TextField(selectedBook.getAuthor());
        authorField.setPromptText("Autor");

        ComboBox<String> genreBox = new ComboBox<>();
        genreBox.getItems().addAll("Ficção", "Não Ficção", "Biografia", "Fantasia", "Ciência");
        genreBox.setValue(selectedBook.getGenre());
        genreBox.setPromptText("Gênero");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Disponível", "Emprestado");
        statusBox.setValue(selectedBook.getStatus());
        statusBox.setPromptText("Status");

        TextField catalogCodeField = new TextField(selectedBook.getCatalogCode());
        catalogCodeField.setPromptText("Código de Catalogação");

        // Configurar o layout do diálogo
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Título:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Autor:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Gênero:"), 0, 2);
        grid.add(genreBox, 1, 2);
        grid.add(new Label("Status:"), 0, 3);
        grid.add(statusBox, 1, 3);
        grid.add(new Label("Código de Catalogação:"), 0, 4);
        grid.add(catalogCodeField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Adicionar botões de ação
        ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Verificar dados ao clicar no botão "Salvar"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (titleField.getText().isEmpty() || authorField.getText().isEmpty() ||
                    genreBox.getValue() == null || statusBox.getValue() == null || catalogCodeField.getText().isEmpty()) {
                    showError("Todos os campos são obrigatórios!");
                    return null;
                }
                // Atualizar os valores no objeto selecionado
                selectedBook.setTitle(titleField.getText());
                selectedBook.setAuthor(authorField.getText());
                selectedBook.setGenre(genreBox.getValue());
                selectedBook.setStatus(statusBox.getValue());
                selectedBook.setCatalogCode(catalogCodeField.getText());
                return selectedBook;
            }
            return null;
        });

        // Mostrar o diálogo e processar o resultado
        dialog.showAndWait().ifPresent(book -> {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE books SET title = ?, author = ?, genre = ?, status = ?, catalog_code = ? WHERE id = ?")) {

                // Atualizar os valores no banco de dados
                statement.setString(1, book.getTitle());
                statement.setString(2, book.getAuthor());
                statement.setString(3, book.getGenre());
                statement.setString(4, book.getStatus());
                statement.setString(5, book.getCatalogCode());
                statement.setInt(6, book.getId());
                statement.executeUpdate();

                // Recarregar a tabela para refletir as alterações
                loadBooks(""); 
            } catch (Exception e) {
                e.printStackTrace();
                showError("Erro ao editar o livro.");
            }
        });
    }
    @FXML
    private void handleOpenDatabase() {
        // Abrir diálogo para selecionar um novo arquivo de banco de dados
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir Banco de Dados");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite Database (*.db)", "*.db"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // Fechar a conexão atual, se houver
            handleCloseDatabase();

            // Conectar ao novo banco de dados
            connectToDatabase(file.getAbsolutePath());
            initializeDatabase();

            // Recarregar os dados
            loadBooks("");
            System.out.println("Banco de dados alterado para: " + file.getAbsolutePath());
        }
    }
    @FXML
    private void handleCloseDatabase() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                bookTable.getItems().clear();
                System.out.println("Conexão com o banco de dados fechada.");
            } catch (Exception e) {
                e.printStackTrace();
                showError("Erro ao fechar a conexão com o banco de dados.");
            }
        }
    }
    @FXML
    private void handleAbout() {
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
        aboutAlert.setTitle("Sobre o SisCaLivros");
        aboutAlert.setHeaderText("SisCaLivros - Organizador de Biblioteca");
        aboutAlert.setContentText("""
            Versão: 1.0.0
            Desenvolvedor: José Pedro Trindade
            Descrição: Este software ajuda a organizar e gerenciar livros em uma biblioteca,
            permitindo adicionar, editar, e pesquisar livros facilmente.
        """);
        aboutAlert.showAndWait();
    }
    @FXML
    private void handleDelete() {
        // Verificar se um livro está selecionado
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showError("Selecione um livro para excluir.");
            return;
        }

        // Confirmar a exclusão com o usuário
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmação de Exclusão");
        confirmAlert.setHeaderText("Deseja realmente excluir este livro?");
        confirmAlert.setContentText("Título: " + selectedBook.getTitle());

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try (PreparedStatement statement = connection.prepareStatement("DELETE FROM books WHERE id = ?")) {
                    statement.setInt(1, selectedBook.getId());
                    statement.executeUpdate();

                    // Remover o livro da tabela
                    bookTable.getItems().remove(selectedBook);
                    System.out.println("Livro excluído: " + selectedBook.getTitle());
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Erro ao excluir o livro.");
                }
            }
        });
    }
    private void filterBooksByCategory(String category) {
        try {
            bookTable.getItems().clear();
            String sql = "SELECT * FROM books";

            switch (category) {
                case "Emprestados":
                    sql += " WHERE status = 'Emprestado'";
                    break;
                case "Disponíveis":
                    sql += " WHERE status = 'Disponível'";
                    break;
                case "Ficção":
                    sql += " WHERE genre = 'Ficção'";
                    break;
                case "Não Ficção":
                    sql += " WHERE genre = 'Não Ficção'";
                    break;
                default: // "Todos"
                    break;
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                bookTable.getItems().add(new Book(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getString("genre"),
                    resultSet.getString("status"),
                    resultSet.getString("catalog_code")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erro ao filtrar os livros.");
        }
    }
}
