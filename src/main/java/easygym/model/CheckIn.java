package easygym.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CheckIn {
    private static int contadorId = 1;
    private int id;
    private int idAluno;
    private String nomeAluno;
    private LocalDate data;
    private LocalTime horario;
    private String unidade;

    public CheckIn() {}
    public CheckIn(int idAluno, String nomeAluno, String unidade) {
        this.id = contadorId++;
        this.idAluno = idAluno; this.nomeAluno = nomeAluno;
        this.data = LocalDate.now(); this.horario = LocalTime.now();
        this.unidade = unidade;
    }

    public int getId() { return id; }
    public int getIdAluno() { return idAluno; }
    public String getNomeAluno() { return nomeAluno; }
    public LocalDate getData() { return data; }
    public LocalTime getHorario() { return horario; }
    public String getUnidade() { return unidade; }
    public String getDataFormatada() { return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); }
    public String getHorarioFormatado() { return horario.format(DateTimeFormatter.ofPattern("HH:mm")); }
}
