package web.controlevacinacao.model;

public enum Categoria {
    FOLHEADOS("folheados"),
    SEMIJOIA("semijoia"),
    INOX("inox"),
    ARTESANAL("artesanal");

    private String descricao;
	
	private Categoria(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}