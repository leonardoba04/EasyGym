package easygym.model;

public enum NivelAtividade {
    SEDENTARIO("Sedentário"),
    INICIANTE("Iniciante"),
    INTERMEDIARIO("Intermediário"),
    AVANCADO("Avançado");

    private final String descricao;
    NivelAtividade(String d) { this.descricao = d; }
    public String getDescricao() { return descricao; }
    @Override public String toString() { return descricao; }
}
