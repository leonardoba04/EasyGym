package easygym.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Pagamento {
    private static int contadorId = 1;

    public enum MetodoPagamento {
        PIX("PIX"), CARTAO_CREDITO("Cartão de Crédito"),
        CARTAO_DEBITO("Cartão de Débito"), BOLETO("Boleto"), DINHEIRO("Dinheiro");

        private final String label;
        MetodoPagamento(String l) { this.label = l; }
        public String getLabel() { return label; }
    }

    private int id;
    private int idAluno;
    private String nomeAluno;
    private MetodoPagamento metodo;
    private LocalDate validade;
    private double valor;
    private LocalDate dataPagamento;

    public Pagamento() {}
    public Pagamento(int idAluno, String nomeAluno, MetodoPagamento metodo, LocalDate validade, double valor) {
        this.id = contadorId++;
        this.idAluno = idAluno; this.nomeAluno = nomeAluno;
        this.metodo = metodo; this.validade = validade;
        this.valor = valor; this.dataPagamento = LocalDate.now();
    }

    public int getId() { return id; }
    public int getIdAluno() { return idAluno; }
    public String getNomeAluno() { return nomeAluno; }
    public MetodoPagamento getMetodo() { return metodo; }
    public LocalDate getValidade() { return validade; }
    public double getValor() { return valor; }
    public LocalDate getDataPagamento() { return dataPagamento; }
    public boolean isAtivo() { return !LocalDate.now().isAfter(validade); }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public String getValidadeFormatada() { return validade.format(FMT); }
    public String getDataPagamentoFormatada() { return dataPagamento.format(FMT); }
}
