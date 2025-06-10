package SysBiblioteca;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

class Emprestimo implements Serializable{

    //serialização de id 
    private static final long serialVersionUID = 1L; //final para valores que não podem ser redefinidos.

    //gerar id para emprestimos de livros.
    private static int contadorEmprestimos = 1;

    //atributos de emprestimos
    private int id;                       
    private Livro livro;                
    private Usuario usuario;              
    private String dataEmprestimo;         
    private String dataDevolucao;         
    private boolean devolvido;

    // Construtor da classe emprestimos;
    public Emprestimo(Livro livro, Usuario usuario, String dataDevolucao){
        this.id = contadorEmprestimos++;   // Gera ID único automaticamente
        this.livro = livro;                // Define o livro emprestado
        this.usuario = usuario;            // Define o usuário que está pegando emprestado
        
        this.dataEmprestimo = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        this.dataDevolucao = dataDevolucao;
        this.devolvido = false;
    }

    //estruturas de acesso mais facil de dados.
    @Override
    public String toString() {
      return "ID " + id + ": " + livro.getTitulo() + " -> " + usuario.getNome() + 
               (devolvido ? " (Devolvido)" : " (Pendente)");  
    }

    public String getDescricaoCompleta(){
        return "Nº " + id + " - Livro: " + livro.getTitulo() +
               " - Emprestado para: " + usuario.getNome() +
               " - Empréstimo: " + dataEmprestimo +
               " - Devolução Prevista: " + dataDevolucao +
               (devolvido ? " (Status: Devolvido)" : " (Status: Pendente)");
    }

    // Retorna verdadeiro se o livro ainda estiver emprestado
    public boolean isAtivo(){return !devolvido;}

    // Métodos de acesso (getters e setter) para os atributos

    public int getId() { return id; }

    public Livro getLivro() { return livro; }

    public Usuario getUsuario() { return usuario; }

    public String getDataEmprestimo() { return dataEmprestimo; }

    public String getDataDevolucao() { return dataDevolucao; }

    public boolean isDevolvido() { return devolvido; }

    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido; // Permite marcar o empréstimo como devolvido
    }
}


