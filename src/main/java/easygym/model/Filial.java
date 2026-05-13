package easygym.model;

public class Filial {
    private static int contadorId = 1;
    private int id;
    private String nome;
    private String endereco;
    private String cidade;
    private String telefone;

    public Filial() {}
    public Filial(String nome, String endereco, String cidade, String telefone) {
        this.id = contadorId++;
        this.nome = nome;
        this.endereco = endereco;
        this.cidade = cidade;
        this.telefone = telefone;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}
