package easygym.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensagem {
    private static int contadorId = 1;

    public enum Remetente { ALUNO, PROFESSOR }

    private int id;
    private int idAluno;
    private int idProfessor;
    private String nomeRemetente;
    private Remetente remetente;
    private String texto;
    private LocalDateTime dataHora;
    private boolean lida;

    public Mensagem() {}
    public Mensagem(int idAluno, int idProfessor, String nomeRemetente, Remetente remetente, String texto) {
        this.id = contadorId++;
        this.idAluno = idAluno;
        this.idProfessor = idProfessor;
        this.nomeRemetente = nomeRemetente;
        this.remetente = remetente;
        this.texto = texto;
        this.dataHora = LocalDateTime.now();
        this.lida = false;
    }

    public int getId() { return id; }
    public int getIdAluno() { return idAluno; }
    public int getIdProfessor() { return idProfessor; }
    public String getNomeRemetente() { return nomeRemetente; }
    public Remetente getRemetente() { return remetente; }
    public String getTexto() { return texto; }
    public LocalDateTime getDataHora() { return dataHora; }
    public boolean isLida() { return lida; }
    public void setLida(boolean lida) { this.lida = lida; }

    public boolean isDoAluno() { return remetente == Remetente.ALUNO; }

    public String getDataHoraFormatada() {
        return dataHora.format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
    }
}
