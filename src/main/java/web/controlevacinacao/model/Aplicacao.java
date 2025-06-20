package web.controlevacinacao.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "aplicacao")
public class Aplicacao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="gerador3", sequenceName="aplicacao_codigo_seq", allocationSize=1)
	@GeneratedValue(generator="gerador3", strategy=GenerationType.SEQUENCE)
	private Long codigo;
	private LocalDate data;
	@ManyToOne
	@JoinColumn(name = "codigo_pessoa")
	private Pessoa pessoa;
	@ManyToOne
	@JoinColumn(name = "codigo_lote")
	private Lote lote;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Lote getLote() {
		return lote;
	}

	public void setLote(Lote lote) {
		this.lote = lote;
	}

	@Override
	public String toString() {
		return "codigo: " + codigo + "\ndata: " + data;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aplicacao other = (Aplicacao) obj;
		return Objects.equals(codigo, other.codigo);
	}

}
