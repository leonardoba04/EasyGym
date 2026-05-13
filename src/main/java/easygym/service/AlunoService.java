package easygym.service;

import easygym.model.*;
import easygym.repository.Database;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {
    private final Database db;
    private final PasswordEncoder encoder;

    public AlunoService(Database db, PasswordEncoder encoder) {
        this.db = db;
        this.encoder = encoder;
    }

    public Aluno cadastrar(String nome, int idade, String endereco, NivelAtividade nivel,
                           String saude, double peso, double altura, int idPlano,
                           String unidade, String email, String telefone) {
        Aluno a = new Aluno(nome, idade, endereco, nivel, saude, peso, altura, idPlano, unidade, email, telefone);
        // Senha padrão = data de nascimento fictícia ou primeiros 6 da matrícula
        a.setSenha(encoder.encode("123456"));
        db.salvarAluno(a);
        db.buscarPlanoPorId(idPlano).ifPresent(p -> p.adicionarAluno(a.getId()));
        return a;
    }

    public List<Aluno> listarTodos() { return db.listarAlunos(); }
    public Optional<Aluno> buscarPorId(int id) { return db.buscarAlunoPorId(id); }
    public Optional<Aluno> buscarPorMatricula(String matricula) { return db.buscarAlunoPorMatricula(matricula); }

    public boolean remover(int id) {
        db.buscarAlunoPorId(id).ifPresent(a ->
            db.buscarPlanoPorId(a.getIdPlano()).ifPresent(p -> p.removerAluno(id)));
        return db.removerAluno(id);
    }

    public void atualizarPlano(int idAluno, int novoIdPlano) {
        db.buscarAlunoPorId(idAluno).ifPresent(a -> {
            db.buscarPlanoPorId(a.getIdPlano()).ifPresent(p -> p.removerAluno(idAluno));
            a.setIdPlano(novoIdPlano);
            db.buscarPlanoPorId(novoIdPlano).ifPresent(p -> p.adicionarAluno(idAluno));
        });
    }

    public void alterarSenha(int idAluno, String novaSenha) {
        db.buscarAlunoPorId(idAluno).ifPresent(a -> a.setSenha(encoder.encode(novaSenha)));
    }

    public int recomendarPlano(NivelAtividade nivel, double imc) {
        List<Plano> planos = db.listarPlanos();
        if (planos.isEmpty()) return -1;
        String nivelRec = switch (nivel) {
            case SEDENTARIO, INICIANTE -> "Básico";
            case INTERMEDIARIO -> "Intermediário";
            case AVANCADO -> "Premium";
        };
        if (imc > 30 || imc < 18.5) nivelRec = "Básico";
        final String nf = nivelRec;
        return planos.stream().filter(p -> p.getNivel().equalsIgnoreCase(nf))
            .mapToInt(Plano::getId).findFirst().orElse(planos.get(0).getId());
    }
}
