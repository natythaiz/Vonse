package web.controlevacinacao.notificacao;

public class NotificacaoSweetAlert2 {

	private String mensagem;
	private TipoNotificaoSweetAlert2 tipo;
	private int intervalo = 3000;

	public NotificacaoSweetAlert2(String mensagem) {
		this.mensagem = mensagem;
		tipo = TipoNotificaoSweetAlert2.INFO;
	}

	public NotificacaoSweetAlert2(String mensagem, TipoNotificaoSweetAlert2 tipo) {
		this.mensagem = mensagem;
		this.tipo = tipo;
	}
	
	public NotificacaoSweetAlert2(String mensagem, TipoNotificaoSweetAlert2 tipo, int intervalo) {
		this.mensagem = mensagem;
		this.tipo = tipo;
		this.intervalo = intervalo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getTipo() {
		return tipo.getTipo();
	}

	public void setTipo(TipoNotificaoSweetAlert2 tipo) {
		this.tipo = tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = TipoNotificaoSweetAlert2.valueOf(tipo);
	}

	public int getIntervalo() {
		return intervalo;
	}

	public void setIntervalo(int intervalo) {
		this.intervalo = intervalo;
	}

	@Override
	public String toString() {
		return "mensagem: " + mensagem + "\ntipo: " + tipo.getTipo() + "\nintervalo: " + intervalo;
	}

}