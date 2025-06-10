package SysBiblioteca;
import java.util.ArrayList;

public class GerenciadorBiblioteca {

    private ArrayList<Livro> livros;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Emprestimo> emprestimos;

    //metodo contrutor
    public GerenciadorBiblioteca() {
        if (BibliotecaDB.existemDadosSalvos()) {
            livros = BibliotecaDB.carregarLivros();
            usuarios = BibliotecaDB.carregarUsuarios();
            emprestimos = BibliotecaDB.carregarEmprestimos();
            System.out.println("Dados carregados do banco de dados.");
        }
        else {
            //inicia lista vazia.
            this.livros = new ArrayList<>();
            this.usuarios = new ArrayList<>();
            this.emprestimos = new ArrayList<>();

            //usuarios de exemplo.
            adicionarDadosExemplo();
        }
    }
    
    private void adicionarDadosExemplo(){ 

            // Adiciona três livros de exemplo
            adicionarLivro(new Livro("Java: Como Programar", "Deitel & Deitel", "Pearson", 2016, "9788543004792"));
            adicionarLivro(new Livro("Algoritmos: Teoria e Prática", "Cormen et al.", "Elsevier", 2012, "9788535236996"));
            adicionarLivro(new Livro("Engenharia de Software", "Sommerville", "Pearson", 2011, "9788579361081"));

            // Adiciona três usuários fictícios de tipos diferentes
            adicionarUsuario(new Estudante("João Silva", "20210001", "joao@email.com", "123.456.789-00"));
            adicionarUsuario(new Professor("Maria Santos", "P20210001", "maria@email.com", "987.654.321-00"));
            adicionarUsuario(new Funcionario("Carlos Oliveira", "F20210001", "carlos@email.com", "000.111.222-33"));
    }

    //adicionar livros
    public void adicionarLivro(Livro livro){
        livros.add(livro);
    }

    //adicionar usuários
    public void adicionarUsuario(Usuario usuario){
        usuarios.add(usuario);
    }

    public void realizarEmprestimo(Emprestimo emprestimo){
        emprestimos.add(emprestimo);
    }

    public void devolverLivro(Emprestimo emprestimo){
        emprestimo.setDevolvido(true);
    }

    public ArrayList<Livro> getLivros(){
        return livros;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public ArrayList<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void salvarDados() {
        BibliotecaDB.salvarLivros(livros);
        BibliotecaDB.salvarUsuarios(usuarios);
        BibliotecaDB.salvarEmprestimos(emprestimos);
        System.out.println(">>> DADOS SALVOS NO DISCO FÍSICO <<<");
    }

    public Livro buscarLivroPorId(int id){
        for (Livro livro : livros){
            if (livro.getId() == id){
                return livro;
            }
        }
        return null;
    }

    public boolean isLivroEmprestado(Livro livro){
        for (Emprestimo e : emprestimos){
            if (e.getLivro().equals(livro) && e.isAtivo()){
                return true;
            }
        }
        return false;
    }
}
