package easygym.repository;

import easygym.model.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class Database {

    private final List<Aluno> alunos = new ArrayList<>();
    private final List<Professor> professores = new ArrayList<>();
    private final List<Plano> planos = new ArrayList<>();
    private final List<Modalidade> modalidades = new ArrayList<>();
    private final List<Pagamento> pagamentos = new ArrayList<>();
    private final List<CheckIn> checkIns = new ArrayList<>();
    private final List<Funcionario> funcionarios = new ArrayList<>();
    private final List<Mensagem> mensagens = new ArrayList<>();
    private Academia academia = new Academia();

    // ── ACADEMIA ──
    public Academia getAcademia() { return academia; }
    public void setAcademia(Academia a) { this.academia = a; }

    // ── FUNCIONARIO ──
    public void salvarFuncionario(Funcionario f) { funcionarios.add(f); }
    public List<Funcionario> listarFuncionarios() { return Collections.unmodifiableList(funcionarios); }
    public Optional<Funcionario> buscarFuncionarioPorEmail(String email) {
        return funcionarios.stream().filter(f -> f.getEmail().equalsIgnoreCase(email)).findFirst();
    }
    public Optional<Funcionario> buscarFuncionarioPorId(int id) {
        return funcionarios.stream().filter(f -> f.getId() == id).findFirst();
    }
    public boolean removerFuncionario(int id) { return funcionarios.removeIf(f -> f.getId() == id); }

    // ── ALUNO ──
    public void salvarAluno(Aluno a) { alunos.add(a); }
    public List<Aluno> listarAlunos() { return Collections.unmodifiableList(alunos); }
    public Optional<Aluno> buscarAlunoPorId(int id) {
        return alunos.stream().filter(a -> a.getId() == id).findFirst();
    }
    public Optional<Aluno> buscarAlunoPorMatricula(String matricula) {
        return alunos.stream().filter(a -> a.getMatricula().equalsIgnoreCase(matricula)).findFirst();
    }
    public boolean removerAluno(int id) { return alunos.removeIf(a -> a.getId() == id); }

    // ── PLANO ──
    public void salvarPlano(Plano p) { planos.add(p); }
    public List<Plano> listarPlanos() { return Collections.unmodifiableList(planos); }
    public Optional<Plano> buscarPlanoPorId(int id) {
        return planos.stream().filter(p -> p.getId() == id).findFirst();
    }
    public boolean removerPlano(int id) { return planos.removeIf(p -> p.getId() == id); }

    // ── PROFESSOR ──
    public void salvarProfessor(Professor p) { professores.add(p); }
    public List<Professor> listarProfessores() { return Collections.unmodifiableList(professores); }
    public Optional<Professor> buscarProfessorPorId(int id) {
        return professores.stream().filter(p -> p.getId() == id).findFirst();
    }
    public List<Professor> buscarProfessoresPorModalidade(String nomeModalidade) {
        return professores.stream()
            .filter(p -> p.getModalidade().equalsIgnoreCase(nomeModalidade))
            .collect(Collectors.toList());
    }
    public boolean removerProfessor(int id) { return professores.removeIf(p -> p.getId() == id); }

    // ── MODALIDADE ──
    public void salvarModalidade(Modalidade m) { modalidades.add(m); }
    public List<Modalidade> listarModalidades() { return Collections.unmodifiableList(modalidades); }
    public Optional<Modalidade> buscarModalidadePorId(int id) {
        return modalidades.stream().filter(m -> m.getId() == id).findFirst();
    }
    public boolean removerModalidade(int id) { return modalidades.removeIf(m -> m.getId() == id); }

    // ── PAGAMENTO ──
    public void salvarPagamento(Pagamento p) { pagamentos.add(p); }
    public List<Pagamento> listarPagamentos() { return Collections.unmodifiableList(pagamentos); }
    public List<Pagamento> listarPagamentosPorAluno(int id) {
        return pagamentos.stream().filter(p -> p.getIdAluno() == id).collect(Collectors.toList());
    }
    public Optional<Pagamento> buscarUltimoPagamentoAtivo(int idAluno) {
        return pagamentos.stream()
            .filter(p -> p.getIdAluno() == idAluno && p.isAtivo())
            .reduce((a, b) -> b);
    }

    // ── CHECKIN ──
    public void salvarCheckIn(CheckIn c) { checkIns.add(c); }
    public List<CheckIn> listarCheckIns() { return Collections.unmodifiableList(checkIns); }
    public List<CheckIn> listarCheckInsPorAluno(int id) {
        return checkIns.stream().filter(c -> c.getIdAluno() == id).collect(Collectors.toList());
    }
    public List<CheckIn> listarCheckInsHoje() {
        return checkIns.stream().filter(c -> c.getData().equals(LocalDate.now())).collect(Collectors.toList());
    }

    // ── MENSAGENS ──
    public void salvarMensagem(Mensagem m) { mensagens.add(m); }
    public List<Mensagem> listarMensagensPorConversa(int idAluno, int idProfessor) {
        return mensagens.stream()
            .filter(m -> m.getIdAluno() == idAluno && m.getIdProfessor() == idProfessor)
            .collect(Collectors.toList());
    }
    public long contarNaoLidasDoAluno(int idAluno) {
        return mensagens.stream()
            .filter(m -> m.getIdAluno() == idAluno
                && m.getRemetente() == Mensagem.Remetente.PROFESSOR
                && !m.isLida())
            .count();
    }
    public long contarNaoLidasDoProfessor(int idProfessor) {
        return mensagens.stream()
            .filter(m -> m.getIdProfessor() == idProfessor
                && m.getRemetente() == Mensagem.Remetente.ALUNO
                && !m.isLida())
            .count();
    }
    public void marcarComoLidasParaAluno(int idAluno, int idProfessor) {
        mensagens.stream()
            .filter(m -> m.getIdAluno() == idAluno && m.getIdProfessor() == idProfessor
                && m.getRemetente() == Mensagem.Remetente.PROFESSOR)
            .forEach(m -> m.setLida(true));
    }

    // ── STATS ──
    public long contarAlunosAtivos() {
        return alunos.stream().filter(a -> buscarUltimoPagamentoAtivo(a.getId()).isPresent()).count();
    }
    public double calcularReceitaTotal() {
        return pagamentos.stream().mapToDouble(Pagamento::getValor).sum();
    }
}
