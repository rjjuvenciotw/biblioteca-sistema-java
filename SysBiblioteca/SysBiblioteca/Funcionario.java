package SysBiblioteca;

import java.io.Serializable;

class Funcionario extends Usuario {
    private static final long serialVersionUID = 1L;
    public Funcionario(String nome, String matricula, String email, String cpf) {
        super(nome, matricula, email, cpf);
        this.limiteEmprestimos = 4;
        this.diasEmprestimo = 20;
    }
}