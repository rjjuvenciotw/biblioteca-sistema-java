package SysBiblioteca;

import java.io.Serializable;

class Estudante extends Usuario {
    private static final long serialVersionUID = 1L;
    public Estudante(String nome, String matricula, String email, String cpf) {
        super(nome, matricula, email, cpf);
        this.limiteEmprestimos = 3;
        this.diasEmprestimo = 15;
    }
}