package web.controlevacinacao.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

//o certo seria table depois de entity, mas como não criei ainda no banco, comentei
//@Table(name = "venda")
@Entity
public class Venda implements Serializable{
    private static final long serialVersionUID = -3935828642122652510L;

    @Id
	@SequenceGenerator(name="gerador4", sequenceName="aplicacao_codigo_seq", allocationSize=1)
	@GeneratedValue(generator="gerador4", strategy=GenerationType.SEQUENCE)
    private Long id;
    private LocalDate data;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    // Mapeado por venda com cascade que significa que se a venda for removida,
    // os itens também serão removidos e orphanRemoval que significa que se um item for removido da venda,
    // ele também será removido do banco de dados e não será mais associado à venda.
    private List<ItemVenda> itensVendidos;
    private double valorTotal;
    @Enumerated(EnumType.STRING)
	private Status status = Status.ATIVO;

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public List<ItemVenda> getItensVendidos() {
        return itensVendidos;
    }
    public void setItensVendidos(List<ItemVenda> itensVendidos) {
        this.itensVendidos = itensVendidos;
    }
    public double getValorTotal() {
        return valorTotal;
    }
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
    public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
        result = prime * result + ((itensVendidos == null) ? 0 : itensVendidos.hashCode());
        long temp;
        temp = Double.doubleToLongBits(valorTotal);
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
        Venda other = (Venda) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (cliente == null) {
            if (other.cliente != null)
                return false;
        } else if (!cliente.equals(other.cliente))
            return false;
        if (itensVendidos == null) {
            if (other.itensVendidos != null)
                return false;
        } else if (!itensVendidos.equals(other.itensVendidos))
            return false;
        if (Double.doubleToLongBits(valorTotal) != Double.doubleToLongBits(other.valorTotal))
            return false;
        return true;
    }
}
