package web.controlevacinacao.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;

//o certo seria table depois de entity, mas como não criei ainda no banco, comentei
//@Table(name = "cliente")
@Entity
public class Cliente implements Serializable{
    private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="gerador3", sequenceName="aplicacao_codigo_seq", allocationSize=1)
	@GeneratedValue(generator="gerador3", strategy=GenerationType.SEQUENCE)
    private Long id; //Troquei o int por Long para evitar overflow
    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    @NotBlank(message = "O contato é obrigatório")
    private String contato;
    private LocalDate data_cadastro;
    @Enumerated(EnumType.STRING)
	private Status status = Status.ATIVO;

    @OneToMany(mappedBy = "cliente")
    private List<Venda> vendas;
    
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
    public LocalDate getData_cadastro() {
        return data_cadastro;
    }
    public void setData_cadastro(LocalDate data_cadastro) {
        this.data_cadastro = data_cadastro;
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
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((contato == null) ? 0 : contato.hashCode());
        result = prime * result + ((data_cadastro == null) ? 0 : data_cadastro.hashCode());
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
        Cliente other = (Cliente) obj;
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
        if (contato == null) {
            if (other.contato != null)
                return false;
        } else if (!contato.equals(other.contato))
            return false;
        if (data_cadastro == null) {
            if (other.data_cadastro != null)
                return false;
        } else if (!data_cadastro.equals(other.data_cadastro))
            return false;
        return true;
    }
}
