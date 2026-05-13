package easygym.model;

import java.util.ArrayList;
import java.util.List;

public class Academia {
    private String nome;
    private String cnpj;
    private String emailContato;
    private String telefone;
    private String logoPath;
    private String corPrimaria;
    private String corSecundaria;
    private List<Filial> filiais = new ArrayList<>();

    public Academia() {
        this.nome = "Minha Academia";
        this.corPrimaria = "#f0b429";
        this.corSecundaria = "#e05c2a";
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getEmailContato() { return emailContato; }
    public void setEmailContato(String emailContato) { this.emailContato = emailContato; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getLogoPath() { return logoPath; }
    public void setLogoPath(String logoPath) { this.logoPath = logoPath; }
    public String getCorPrimaria() { return corPrimaria; }
    public void setCorPrimaria(String corPrimaria) { this.corPrimaria = corPrimaria; }
    public String getCorSecundaria() { return corSecundaria; }
    public void setCorSecundaria(String corSecundaria) { this.corSecundaria = corSecundaria; }
    public List<Filial> getFiliais() { return filiais; }

    public void adicionarFilial(Filial f) { filiais.add(f); }
    public void removerFilial(int id) { filiais.removeIf(f -> f.getId() == id); }
}
