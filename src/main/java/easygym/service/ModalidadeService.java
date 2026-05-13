package easygym.service;

import easygym.model.Modalidade;
import easygym.repository.Database;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ModalidadeService {
    private final Database db;
    public ModalidadeService(Database db) { this.db = db; }
    public Modalidade cadastrar(String nome, String unidade, int capacidade) {
        Modalidade m = new Modalidade(nome, unidade, capacidade);
        db.salvarModalidade(m);
        return m;
    }
    public List<Modalidade> listarTodas() { return db.listarModalidades(); }
    public Optional<Modalidade> buscarPorId(int id) { return db.buscarModalidadePorId(id); }
    public boolean remover(int id) { return db.removerModalidade(id); }
    public void adicionarHorario(int idMod, String horario) {
        db.buscarModalidadePorId(idMod).ifPresent(m -> m.adicionarHorario(horario));
    }
    public void vincularProfessor(int idMod, int idProf) {
        db.buscarModalidadePorId(idMod).ifPresent(m -> m.adicionarProfessor(idProf));
    }
}
