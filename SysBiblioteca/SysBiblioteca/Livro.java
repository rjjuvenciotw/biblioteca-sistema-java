package SysBiblioteca;

import java.io.Serializable;

//implements, usuado para herança.
public class Livro implements Serializable{

    private static final long serialVersionUID = 1L; //serialização
    private int id;
    private static int contadorId = 1; //static, determinando valores pre definidos em toda a classe. como atributos A = 2

    //atributos dos livros.
    private String titulo;
    private String autor;
    private String editora;
    private int ano;
    private String isbn;

    //contrutor.
    public Livro(String titulo, String autor, String editora, int ano, String isbn){
        //atribuição de contador.
        this.id = contadorId++;

        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.ano = ano;
        this.isbn = isbn;
    }

    //metodos sobreescritos de rotrona simples de dados de livros. 
    @Override
    public String toString(){
        return titulo + " (ISBN: " + isbn + ")";
    }

    public String getDescricaoCompleta(){
        return "ID: " + id + " / " + titulo + " - " + autor + " (" + ano + ") - ISBN: " + isbn;
    }

    //sobrescrita de metodo padrão, para identificar elementos iguais.
    @Override 
    public boolean equals(Object obj){
        if (obj instanceof Livro){
            Livro outro = (Livro) obj;

            return this.isbn.equals(outro.isbn);
        }
        return false;
    }
// Métodos getter e setter para acessar e modificar os atributos privados da classe

    public int getId() { 
        return id; 
    }

    // Getter do contador de ID estático
    public static int getContadorId() { 
        return contadorId; 
    }

    // Setter do contador de ID estático (útil para restaurar o contador a partir de um estado salvo)
    public static void setContadorId(int contador) { 
        contadorId = contador; 
    }

    public String getTitulo() { 
        return titulo; 
    }

    public void setTitulo(String titulo) { 
        this.titulo = titulo; 
    }

    public String getAutor() { 
        return autor; 
    }

    public void setAutor(String autor) { 
        this.autor = autor; 
    }

    public String getEditora() { 
        return editora; 
    }

    public void setEditora(String editora) { 
        this.editora = editora; 
    }

    public int getAno() { 
        return ano; 
    }

    public void setAno(int ano) { 
        this.ano = ano; 
    }

    public String getIsbn() { 
        return isbn; 
    }

    public void setIsbn(String isbn) { 
        this.isbn = isbn; 
    }
}

