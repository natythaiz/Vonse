package web.controlevacinacao.filter;

import java.time.LocalDate;

public class ClienteFilter {
    private Long id; 
    private String nome;
    private String contato;
    private LocalDate minDataCadastro;
    private LocalDate maxDataCadastro;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getContato() {
        return contato;
    }
    public void setContato(String contato) {
        this.contato = contato;
    }
    public LocalDate getMinDataCadastro() {
        return minDataCadastro;
    }
    public void setMinDataCadastro(LocalDate minDataCadastro) {
        this.minDataCadastro = minDataCadastro;
    }
    public LocalDate getMaxDataCadastro() {
        return maxDataCadastro;
    }
    public void setMaxDataCadastro(LocalDate maxDataCadastro) {
        this.maxDataCadastro = maxDataCadastro;
    }

    @Override
    public String toString() {
        return "id: " + id + "\nnome: " + nome + "\ncontato: " + contato + "\nminDataCadastro: " + minDataCadastro
                + "\nmaxDataCadastro: " + maxDataCadastro;
    }
}
