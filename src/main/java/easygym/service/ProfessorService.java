package easygym.service;

import easygym.model.Professor;
import easygym.repository.Database;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ProfessorService {
    private final Database db;
    public ProfessorService(Database db) { this.db = db; }
    public Professor cadastrar(String nome, String modalidade, String unidade, String email, String telefone) {
        Professor p = new Professor(nome, modalidade, unidade, email, telefone);
        db.salvarProfessor(p);
        return p;
    }
    public List<Professor> listarTodos() { return db.listarProfessores(); }
    public Optional<Professor> buscarPorId(int id) { return db.buscarProfessorPorId(id); }
    public boolean remover(int id) { return db.removerProfessor(id); }
}
