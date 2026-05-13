package easygym.service;

import easygym.model.*;
import easygym.repository.Database;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {
    private final Database db;
    public PagamentoService(Database db) { this.db = db; }

    public Pagamento registrar(int idAluno, String metodoStr, int meses) {
        Optional<Aluno> aluno = db.buscarAlunoPorId(idAluno);
        if (aluno.isEmpty()) return null;
        double valorPlano = db.buscarPlanoPorId(aluno.get().getIdPlano()).map(Plano::getValor).orElse(0.0);
        Pagamento.MetodoPagamento metodo = Pagamento.MetodoPagamento.valueOf(metodoStr);
        LocalDate validade = LocalDate.now().plusMonths(meses);
        Pagamento p = new Pagamento(idAluno, aluno.get().getNome(), metodo, validade, valorPlano * meses);
        db.salvarPagamento(p);
        return p;
    }

    public List<Pagamento> listarTodos() { return db.listarPagamentos(); }
    public List<Pagamento> listarPorAluno(int id) { return db.listarPagamentosPorAluno(id); }
    public Optional<Pagamento> buscarAtivo(int idAluno) { return db.buscarUltimoPagamentoAtivo(idAluno); }
}
