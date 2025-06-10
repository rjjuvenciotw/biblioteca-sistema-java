package SysBiblioteca;
import java.io.Serializable;

abstract class Usuario implements Serializable{

    private static final long serialVersionUID = 1L;

    private int id;

    private static int contadorid = 1; // gera ids automaticos.

    // itens protegidos, acessáveis por sub classes.
    protected String nome;
    protected String matricula;
    protected String email;
    protected String cpf;

    //intens protegidos de regras de emprestimo.
    protected int limiteEmprestimos;
    protected int diasEmprestimo;

    public Usuario(String nome, String matricula, String email, String cpf){
        //contador de usuários.
        this.id = contadorid++;

        this.nome = nome;
        this.matricula = matricula;
        this.email = email;
        this.cpf = cpf;
    }

// Sobrescreve o método toString() para retornar uma representação simples do usuário
    @Override
    public String toString(){
        return nome + " (Mat: " + matricula + ")";
    }

    public String getDescricaoCompleta(){
        return "ID: " + id + " | " + nome + " - Matrícula: " + matricula + " - Email: " + email + " - CPF: " + cpf;
    }

    // Retorna o ID do usuário
    public int getId() { return id; }

    // Retorna o nome do usuário
    public String getNome() { return nome; }

    // Define (modifica) o nome do usuário
    public void setNome(String nome) { this.nome = nome; }

    // Retorna a matrícula do usuário
    public String getMatricula() { return matricula; }

    // Define a matrícula do usuário
    public void setMatricula(String matricula) { this.matricula = matricula; }

    // Retorna o email do usuário
    public String getEmail() { return email; }

    // Define o email do usuário
    public void setEmail(String email) { this.email = email; }

    // Retorna o CPF do usuário
    public String getCpf() { return cpf; }

    // Define o CPF do usuário
    public void setCpf(String cpf) { this.cpf = cpf; }

    // Retorna o limite de empréstimos permitido ao usuário
    public int getLimiteEmprestimos() { return limiteEmprestimos; }

    // Retorna a quantidade de dias permitidos para cada empréstimo
    public int getDiasEmprestimo() { return diasEmprestimo; }
}





