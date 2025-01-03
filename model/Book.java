package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Book {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty genre;
    private final StringProperty status;
    private final StringProperty catalogCode;
    
    // Construtor principal
    public Book(int id, String title, String author, String genre, String status, String catalogCode) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleStringProperty(genre);
        this.status = new SimpleStringProperty(status);
        this.catalogCode = new SimpleStringProperty(catalogCode);
    }
    public Book(String title, String author, String genre, String status, String catalogCode) {
        this(0, title, author, genre, status, catalogCode); // ID será gerado pelo banco
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }
    
    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty authorProperty() {
        return author;
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty catalogCodeProperty() {
        return catalogCode;
    }
    
    // Getters para valores
    public String getTitle() {
        return title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getGenre() {
        return genre.get();
    }

    public String getStatus() {
        return status.get();
    }

    // Setters para valores
    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("O título não pode estar vazio.");
        }
        this.title.set(title);
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    // Método toString
    @Override
    public String toString() {
        return "Book{" +
               "title='" + getTitle() + '\'' +
               ", author='" + getAuthor() + '\'' +
               ", genre='" + getGenre() + '\'' +
               ", status='" + getStatus() + '\'' +
               '}';
    }
    public String getCatalogCode() {
        return catalogCode.get();
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode.set(catalogCode);
    }    
}
