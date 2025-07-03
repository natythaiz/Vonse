package web.controlevacinacao.model;

import java.io.Serializable;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//o certo seria table depois de entity, mas como não criei ainda no banco, comentei
//@Table(name="produto")
@Entity
@DynamicUpdate
public class Produto implements Serializable {

    private static final long serialVersionUID = 7562368353372595992L;

    @Id
    @SequenceGenerator(name = "gerador5", sequenceName = "vacina_codigo_seq", allocationSize = 1)
    @GeneratedValue(generator = "gerador5", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nome;
    @NotNull(message = "A categoria é obrigatória")
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    @NotNull(message = "O tipo é obrigatório")
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    @NotNull(message = "O preço de custo é obrigatório")
    private double precoCusto;
    private double precoVenda;
    @NotNull(message = "A quantidade em estoque é obrigatória")
    private Long estoque;
    private String descricao;
    @NotBlank(message = "O fonecedor é obrigatório")
    private String fornecedor;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

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

    public double getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(double precoCusto) {
        this.precoCusto = precoCusto;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Long getEstoque() {
        return estoque;
    }

    public void setEstoque(Long estoque) {
        this.estoque = estoque;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPrecoVendaFormatado() {
        return String.format("%.2f", precoVenda);
    }

    public String getPrecoCustoFormatado() {
        return String.format("%.2f", precoCusto);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
        result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
        long temp;
        temp = Double.doubleToLongBits(precoCusto);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(precoVenda);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((estoque == null) ? 0 : estoque.hashCode());
        result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
        result = prime * result + ((fornecedor == null) ? 0 : fornecedor.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Produto other = (Produto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (categoria != other.categoria)
            return false;
        if (tipo != other.tipo)
            return false;
        if (Double.doubleToLongBits(precoCusto) != Double.doubleToLongBits(other.precoCusto))
            return false;
        if (Double.doubleToLongBits(precoVenda) != Double.doubleToLongBits(other.precoVenda))
            return false;
        if (estoque == null) {
            if (other.estoque != null)
                return false;
        } else if (!estoque.equals(other.estoque))
            return false;
        if (descricao == null) {
            if (other.descricao != null)
                return false;
        } else if (!descricao.equals(other.descricao))
            return false;
        if (fornecedor == null) {
            if (other.fornecedor != null)
                return false;
        } else if (!fornecedor.equals(other.fornecedor))
            return false;
        return true;
    }
}
