package easygym.model;

public class Funcionario {
    private static int contadorId = 1;

    public enum Perfil {
        ADMIN("Administrador"),
        FUNCIONARIO("Funcionário");

        private final String label;
        Perfil(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    private int id;
    private String nome;
    private String email;
    private String senha;
    private Perfil perfil;
    private String unidade;
    private boolean ativo;

    public Funcionario() {}
    public Funcionario(String nome, String email, String senha, Perfil perfil, String unidade) {
        this.id = contadorId++;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.unidade = unidade;
        this.ativo = true;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public boolean isAdmin() { return perfil == Perfil.ADMIN; }
}
