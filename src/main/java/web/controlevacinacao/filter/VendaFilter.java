package web.controlevacinacao.filter;

import java.time.LocalDate;

public class VendaFilter {
    private Long id;
    private LocalDate minData;
    private LocalDate maxData;
    private String nomeCliente;
    private String nomeProduto;
    private Double maxValorTotal;
    private Double minValorTotal;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getMinData() {
        return minData;
    }
    public void setMinData(LocalDate minData) {
        this.minData = minData;
    }
    public LocalDate getMaxData() {
        return maxData;
    }
    public void setMaxData(LocalDate maxData) {
        this.maxData = maxData;
    }
    public String getNomeCliente() {
        return nomeCliente;
    }
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
    public String getNomeProduto() {
        return nomeProduto;
    }
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }
    public Double getMaxValorTotal() {
        return maxValorTotal;
    }
    public void setMaxValorTotal(Double maxValorTotal) {
        this.maxValorTotal = maxValorTotal;
    }
    public Double getMinValorTotal() {
        return minValorTotal;
    }
    public void setMinValorTotal(Double minValorTotal) {
        this.minValorTotal = minValorTotal;
    }
    @Override
    public String toString() {
        return "id: " + id + "\nminData: " + minData + "\nmaxData: " + maxData + "\nnomeCliente: " + nomeCliente
                + "\nnomeProduto: " + nomeProduto + "\nmaxValorTotal: " + maxValorTotal + "\nminValorTotal: "
                + minValorTotal;
    }
}
