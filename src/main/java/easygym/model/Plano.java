package easygym.model;

import java.util.ArrayList;
import java.util.List;

public class Plano {
    private static int contadorId = 1;
    private int id;
    private String nome;
    private String nivel;
    private double valor;
    private String descricao;
    private List<String> beneficios = new ArrayList<>();
    private List<Integer> idAlunos = new ArrayList<>();

    public Plano() {}
    public Plano(String nome, String nivel, double valor, String descricao) {
        this.id = contadorId++;
        this.nome = nome;
        this.nivel = nivel;
        this.valor = valor;
        this.descricao = descricao;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String d) { this.descricao = d; }
    public List<String> getBeneficios() { return beneficios; }
    public List<Integer> getIdAlunos() { return idAlunos; }

    public void adicionarBeneficio(String b) { beneficios.add(b); }
    public void adicionarAluno(int id) { if (!idAlunos.contains(id)) idAlunos.add(id); }
    public void removerAluno(int id) { idAlunos.remove(Integer.valueOf(id)); }
    public int getTotalAlunos() { return idAlunos.size(); }
}
