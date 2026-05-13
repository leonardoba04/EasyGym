package easygym.service;

import easygym.model.*;
import easygym.repository.Database;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PlanoService {
    private final Database db;
    public PlanoService(Database db) { this.db = db; }
    public Plano cadastrar(String nome, String nivel, double valor, String descricao) {
        Plano p = new Plano(nome, nivel, valor, descricao);
        db.salvarPlano(p);
        return p;
    }
    public List<Plano> listarTodos() { return db.listarPlanos(); }
    public Optional<Plano> buscarPorId(int id) { return db.buscarPlanoPorId(id); }
    public boolean remover(int id) { return db.removerPlano(id); }
}
