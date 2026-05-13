package easygym.service;

import easygym.model.Funcionario;
import easygym.repository.Database;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FuncionarioService {
    private final Database db;
    private final PasswordEncoder encoder;

    public FuncionarioService(Database db, PasswordEncoder encoder) {
        this.db = db;
        this.encoder = encoder;
    }

    public Funcionario cadastrar(String nome, String email, String senha,
                                  Funcionario.Perfil perfil, String unidade) {
        Funcionario f = new Funcionario(nome, email, encoder.encode(senha), perfil, unidade);
        db.salvarFuncionario(f);
        return f;
    }

    public List<Funcionario> listarTodos() { return db.listarFuncionarios(); }
    public Optional<Funcionario> buscarPorId(int id) { return db.buscarFuncionarioPorId(id); }

    public boolean remover(int id) { return db.removerFuncionario(id); }

    public void alterarStatus(int id, boolean ativo) {
        db.buscarFuncionarioPorId(id).ifPresent(f -> f.setAtivo(ativo));
    }

    public void alterarSenha(int id, String novaSenha) {
        db.buscarFuncionarioPorId(id).ifPresent(f -> f.setSenha(encoder.encode(novaSenha)));
    }
}
