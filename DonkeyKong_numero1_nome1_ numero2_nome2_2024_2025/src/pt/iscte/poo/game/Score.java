package pt.iscte.poo.game;

public class Score {

	private String jogador;
	private int score;
	
	public Score(String jogador, int score) {
		this.jogador = jogador;
		this.score = score;
	}

	public Score(String score) {
		String [] array = score.split(":");
		this.jogador = array[0];
		this.score = Integer.parseInt(array[1]);
	}
	
	public String getJogador() {
		return this.jogador;
	}
	
	public int getScore() {
		return this.score;
	}
	
	@Override
	public String toString() {
		return this.jogador + ":" + this.score;
	}
}