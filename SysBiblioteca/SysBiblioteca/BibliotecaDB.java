package SysBiblioteca;

import java.util.ArrayList; // ArrayList para trabalhar com listas 

import java.io.File; //manipulação de arquivos
import java.io.IOException; // tratar erros de I/O
import java.io.ObjectOutputStream; //  gravar objetos em arquivos
import java.io.FileOutputStream; // abrir arquivos para gravação de dados
import java.io.ObjectInputStream; //  ler objetos de arquivos
import java.io.FileInputStream; //  abrir arquivos para leitura

class BibliotecaDB {

    //atributos
    private static final String ARQUIVO_LIVROS = "livros.dat";
    private static final String ARQUIVO_USUARIOS = "usuarios.dat";
    private static final String ARQUIVO_EMPRESTIMOS = "emprestimos.dat";

     /**
     * Salva a lista de livros em um arquivo
     * @param livros Lista de livros a ser salva
     */

    public static void salvarLivros(ArrayList<Livro> livros) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO_LIVROS))) {
            oos.writeObject(livros); // Serializa e grava a lista no arquivo
            System.out.println("Livros salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar livros: " + e.getMessage()); // Em caso de erro
        }
    }

    /**
     * Carrega a lista de livros do arquivo
     * @return Lista de livros carregada ou uma lista vazia se o arquivo não existir
     */

    @SuppressWarnings("unchecked") // Suprime o aviso de conversão genérica
    public static ArrayList<Livro> carregarLivros() {
        File arquivo = new File(ARQUIVO_LIVROS);
        if (!arquivo.exists()) { // Se o arquivo não existir, retorna lista vazia
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(arquivo))) {
            return (ArrayList<Livro>) ois.readObject(); // Lê e converte o objeto de volta para lista
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar livros: " + e.getMessage());
            return new ArrayList<>(); // Retorna lista vazia em caso de erro
        }
    }
    
    /**
     * Salva a lista de usuários em um arquivo
     * @param usuarios Lista de usuários a ser salva
     */
    public static void salvarUsuarios(ArrayList<Usuario> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO_USUARIOS))) {
            oos.writeObject(usuarios); // Serializa e grava a lista no arquivo
            System.out.println("Usuários salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }
    
    /**
     * Carrega a lista de usuários do arquivo
     * @return Lista de usuários carregada ou uma lista vazia se o arquivo não existir
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Usuario> carregarUsuarios() {
        File arquivo = new File(ARQUIVO_USUARIOS);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(arquivo))) {
            return (ArrayList<Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar usuários: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Salva a lista de empréstimos em um arquivo
     * @param emprestimos Lista de empréstimos a ser salva
     */
    public static void salvarEmprestimos(ArrayList<Emprestimo> emprestimos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO_EMPRESTIMOS))) {
            oos.writeObject(emprestimos); // Serializa e grava a lista no arquivo
            System.out.println("Empréstimos salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar empréstimos: " + e.getMessage());
        }
    }
    
    /**
     * Carrega a lista de empréstimos do arquivo
     * @return Lista de empréstimos carregada ou uma lista vazia se o arquivo não existir
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Emprestimo> carregarEmprestimos() {
        File arquivo = new File(ARQUIVO_EMPRESTIMOS);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(arquivo))) {
            return (ArrayList<Emprestimo>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar empréstimos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Verifica se existem dados salvos
     * @return true se pelo menos um dos arquivos de dados existir
     */
    public static boolean existemDadosSalvos() {
        File arquivoLivros = new File(ARQUIVO_LIVROS);
        File arquivoUsuarios = new File(ARQUIVO_USUARIOS);
        File arquivoEmprestimos = new File(ARQUIVO_EMPRESTIMOS);
        
        // Retorna true se ao menos um dos arquivos existir
        return arquivoLivros.exists() || arquivoUsuarios.exists() || arquivoEmprestimos.exists();
    }
}


