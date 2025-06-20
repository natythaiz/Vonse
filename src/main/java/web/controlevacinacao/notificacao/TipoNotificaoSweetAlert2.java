package web.controlevacinacao.notificacao;

public enum TipoNotificaoSweetAlert2 {
	
	SUCCESS("success"),
	ERROR("error"),
	WARNING("warning"),
	INFO("info"),
	QUESTION("question");
	
	private String tipo;
	
	private TipoNotificaoSweetAlert2(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return tipo;
	}	
}