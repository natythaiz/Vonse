package web.controlevacinacao.filter;

import web.controlevacinacao.model.Categoria;
import web.controlevacinacao.model.Tipo;

public class ProdutoFilter {
    private Long id;
    private String nome;
    private Categoria categoria;
    private Tipo tipo;
    private Double minimoCusto;
    private Double maximoCusto;
    private Double minimoPrecoVenda;
    private Double maximoPrecoVenda;
    private Long estoque;
    private String fornecedor;

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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Double getMinimoCusto() {
        return minimoCusto;
    }

    public void setMinimoCusto(Double minimoCusto) {
        this.minimoCusto = minimoCusto;
    }

    public Double getMaximoCusto() {
        return maximoCusto;
    }

    public void setMaximoCusto(Double maximoCusto) {
        this.maximoCusto = maximoCusto;
    }

    public Double getMinimoPrecoVenda() {
        return minimoPrecoVenda;
    }

    public void setMinimoPrecoVenda(Double minimoPrecoVenda) {
        this.minimoPrecoVenda = minimoPrecoVenda;
    }

    public Double getMaximoPrecoVenda() {
        return maximoPrecoVenda;
    }

    public void setMaximoPrecoVenda(Double maximoPrecoVenda) {
        this.maximoPrecoVenda = maximoPrecoVenda;
    }

    public Long getEstoque() {
        return estoque;
    }

    public void setEstoque(Long estoque) {
        this.estoque = estoque;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString() {
        return "id: " + id + "\nnome: " + nome + "\ncategoria: " + categoria + "\ntipo: " + tipo + "\nminimoCusto: "
                + minimoCusto + "\nmaximoCusto: " + maximoCusto + "\nminimoPrecoVenda: " + minimoPrecoVenda
                + "\nmaximoPrecoVenda: " + maximoPrecoVenda + "\nestoque: " + estoque + "\nfornecedor: " + fornecedor;
    }
}
