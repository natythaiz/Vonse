package web.controlevacinacao.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class ItemVenda implements Serializable{
    private static final long serialVersionUID = -3935828642122652510L;

    @Id
	@SequenceGenerator(name="gerador2", sequenceName="aplicacao_codigo_seq", allocationSize=1)
	@GeneratedValue(generator="gerador2", strategy=GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venda_id")
    private Venda venda; //Adicionei o relacionamento com a classe Venda

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Long quantidade;
    private double precoUnitario;
    private double subTotal;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Venda getVenda() {
        return venda;
    }
    public void setVenda(Venda venda) {
        this.venda = venda;
    }
    public Produto getProduto() {
        return produto;
    }
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    public Long getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }
    public double getPrecoUnitario() {
        return precoUnitario;
    }
    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
    public double getSubTotal() {
        return subTotal;
    }
    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((venda == null) ? 0 : venda.hashCode());
        result = prime * result + ((produto == null) ? 0 : produto.hashCode());
        result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
        long temp;
        temp = Double.doubleToLongBits(precoUnitario);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(subTotal);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        ItemVenda other = (ItemVenda) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (venda == null) {
            if (other.venda != null)
                return false;
        } else if (!venda.equals(other.venda))
            return false;
        if (produto == null) {
            if (other.produto != null)
                return false;
        } else if (!produto.equals(other.produto))
            return false;
        if (quantidade == null) {
            if (other.quantidade != null)
                return false;
        } else if (!quantidade.equals(other.quantidade))
            return false;
        if (Double.doubleToLongBits(precoUnitario) != Double.doubleToLongBits(other.precoUnitario))
            return false;
        if (Double.doubleToLongBits(subTotal) != Double.doubleToLongBits(other.subTotal))
            return false;
        return true;
    }
}
