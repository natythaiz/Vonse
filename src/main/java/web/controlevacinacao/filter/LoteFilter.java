package web.controlevacinacao.filter;

import java.time.LocalDate;

public class LoteFilter {

    private Long codigo;
    private LocalDate inicioValidade;
    private LocalDate fimValidade;
    private Integer minimoDosesLote;
    private Integer maximoDosesLote;
    private Integer minimoDosesAtual;
    private Integer maximoDosesAtual;
    private String nomeVacina;
    private int text;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public LocalDate getInicioValidade() {
        return inicioValidade;
    }

    public void setInicioValidade(LocalDate inicioValidade) {
        this.inicioValidade = inicioValidade;
    }

    public LocalDate getFimValidade() {
        return fimValidade;
    }

    public void setFimValidade(LocalDate fimValidade) {
        this.fimValidade = fimValidade;
    }

    public Integer getMinimoDosesLote() {
        return minimoDosesLote;
    }

    public void setMinimoDosesLote(Integer minimoDosesLote) {
        this.minimoDosesLote = minimoDosesLote;
    }

    public Integer getMaximoDosesLote() {
        return maximoDosesLote;
    }

    public void setMaximoDosesLote(Integer maximoDosesLote) {
        this.maximoDosesLote = maximoDosesLote;
    }

    public Integer getMinimoDosesAtual() {
        return minimoDosesAtual;
    }

    public void setMinimoDosesAtual(Integer minimoDosesAtual) {
        this.minimoDosesAtual = minimoDosesAtual;
    }

    public Integer getMaximoDosesAtual() {
        return maximoDosesAtual;
    }

    public void setMaximoDosesAtual(Integer maximoDosesAtual) {
        this.maximoDosesAtual = maximoDosesAtual;
    }

    public String getNomeVacina() {
        return nomeVacina;
    }

    public void setNomeVacina(String nomeVacina) {
        this.nomeVacina = nomeVacina;
    }

    @Override
    public String toString() {
        return "LoteFilter [codigo=" + codigo + ", inicioValidade=" + inicioValidade + ", fimValidade=" + fimValidade
                + ", minimoDosesLote=" + minimoDosesLote + ", maximoDosesLote=" + maximoDosesLote
                + ", minimoDosesAtual=" + minimoDosesAtual + ", maximoDosesAtual=" + maximoDosesAtual + ", vacina="
                + nomeVacina + "]";
    }

}
