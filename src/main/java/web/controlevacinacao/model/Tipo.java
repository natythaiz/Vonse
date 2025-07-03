package web.controlevacinacao.model;

public enum Tipo {
    PULSEIRA("pulseira"),
    COLAR("colar"),
    CHOCKER("colar"),
    ANEL("anel"),
    TORNOZELEIRA("tornozeleira"),
    BRINCO("brinco"),
    PIERCING("piercing"),
    ACESSORIO("acessorio");

    private String descricao;
	
	private Tipo(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
