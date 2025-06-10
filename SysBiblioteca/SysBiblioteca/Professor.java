package SysBiblioteca;

import java.io.Serializable;

class Professor extends Usuario {
    private static final long serialVersionUID = 1L;
    public Professor(String nome, String matricula, String email, String cpf) {
        super(nome, matricula, email, cpf);
        this.limiteEmprestimos = 5;
        this.diasEmprestimo = 30;
    }
}