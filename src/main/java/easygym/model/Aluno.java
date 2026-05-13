package easygym.model;

public class Aluno {
    private static int contadorId = 1;
    private int id;
    private String matricula;
    private String senha; // bcrypt hash
    private String nome;
    private int idade;
    private String endereco;
    private NivelAtividade nivelAtividade;
    private String problemasDeSaude;
    private double peso;
    private double altura;
    private int idPlano;
    private String unidade;
    private String email;
    private String telefone;

    public Aluno() {}
    public Aluno(String nome, int idade, String endereco, NivelAtividade nivel,
                 String saude, double peso, double altura, int idPlano,
                 String unidade, String email, String telefone) {
        this.id = contadorId++;
        this.matricula = String.format("MAT%05d", this.id);
        this.nome = nome; this.idade = idade; this.endereco = endereco;
        this.nivelAtividade = nivel; this.problemasDeSaude = saude;
        this.peso = peso; this.altura = altura; this.idPlano = idPlano;
        this.unidade = unidade; this.email = email; this.telefone = telefone;
    }

    public String getMatricula() { return matricula; }
    public String getSenha() { return senha; }
    public void setSenha(String s) { this.senha = s; }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String n) { this.nome = n; }
    public int getIdade() { return idade; }
    public void setIdade(int i) { this.idade = i; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String e) { this.endereco = e; }
    public NivelAtividade getNivelAtividade() { return nivelAtividade; }
    public void setNivelAtividade(NivelAtividade n) { this.nivelAtividade = n; }
    public String getProblemasDeSaude() { return problemasDeSaude; }
    public void setProblemasDeSaude(String s) { this.problemasDeSaude = s; }
    public double getPeso() { return peso; }
    public void setPeso(double p) { this.peso = p; }
    public double getAltura() { return altura; }
    public void setAltura(double a) { this.altura = a; }
    public int getIdPlano() { return idPlano; }
    public void setIdPlano(int i) { this.idPlano = i; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String u) { this.unidade = u; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String t) { this.telefone = t; }

    public double calcularIMC() { return altura <= 0 ? 0 : peso / (altura * altura); }
    public String getClassificacaoIMC() {
        double imc = calcularIMC();
        if (imc < 18.5) return "Abaixo do peso";
        if (imc < 25.0) return "Normal";
        if (imc < 30.0) return "Sobrepeso";
        return "Obesidade";
    }
}
