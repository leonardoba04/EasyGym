package easygym.model;

public class Professor {
    private static int contadorId = 1;
    private int id;
    private String nome;
    private String modalidade;
    private String unidade;
    private String email;
    private String telefone;

    public Professor() {}
    public Professor(String nome, String modalidade, String unidade, String email, String telefone) {
        this.id = contadorId++;
        this.nome = nome; this.modalidade = modalidade; this.unidade = unidade;
        this.email = email; this.telefone = telefone;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String n) { this.nome = n; }
    public String getModalidade() { return modalidade; }
    public void setModalidade(String m) { this.modalidade = m; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String u) { this.unidade = u; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String t) { this.telefone = t; }
}
