package easygym.model;

import java.util.ArrayList;
import java.util.List;

public class Modalidade {
    private static int contadorId = 1;
    private int id;
    private String nome;
    private String unidade;
    private int capacidade;
    private List<String> horarios = new ArrayList<>();
    private List<Integer> idProfessores = new ArrayList<>();
    private List<Integer> idAlunos = new ArrayList<>();

    public Modalidade() {}
    public Modalidade(String nome, String unidade, int capacidade) {
        this.id = contadorId++;
        this.nome = nome; this.unidade = unidade; this.capacidade = capacidade;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String n) { this.nome = n; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String u) { this.unidade = u; }
    public int getCapacidade() { return capacidade; }
    public void setCapacidade(int c) { this.capacidade = c; }
    public List<String> getHorarios() { return horarios; }
    public List<Integer> getIdProfessores() { return idProfessores; }
    public List<Integer> getIdAlunos() { return idAlunos; }

    public void adicionarHorario(String h) { if (!horarios.contains(h)) horarios.add(h); }
    public void adicionarProfessor(int id) { if (!idProfessores.contains(id)) idProfessores.add(id); }
    public void adicionarAluno(int id) { if (!idAlunos.contains(id)) idAlunos.add(id); }
    public void removerAluno(int id) { idAlunos.remove(Integer.valueOf(id)); }

    public double getOcupacaoPercent() {
        return capacidade == 0 ? 0 : Math.min(100.0, (idAlunos.size() * 100.0) / capacidade);
    }
}
