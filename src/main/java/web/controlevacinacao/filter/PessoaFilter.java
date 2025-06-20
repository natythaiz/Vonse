package web.controlevacinacao.filter;

import java.time.LocalDate;

public class PessoaFilter {

    private Long codigo;
    private String nome;
    private String cpf;
    private LocalDate dataNascimentoInicial;
    private LocalDate dataNascimentoFinal;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimentoInicial() {
        return dataNascimentoInicial;
    }

    public void setDataNascimentoInicial(LocalDate dataNascimentoInicial) {
        this.dataNascimentoInicial = dataNascimentoInicial;
    }

    public LocalDate getDataNascimentoFinal() {
        return dataNascimentoFinal;
    }

    public void setDataNascimentoFinal(LocalDate dataNascimentoFinal) {
        this.dataNascimentoFinal = dataNascimentoFinal;
    }

    @Override
    public String toString() {
        return "PessoaFilter [codigo=" + codigo + ", nome=" + nome + ", cpf=" + cpf + ", dataNascimentoInicial="
                + dataNascimentoInicial + ", dataNascimentoFinal=" + dataNascimentoFinal + "]";
    }

}
