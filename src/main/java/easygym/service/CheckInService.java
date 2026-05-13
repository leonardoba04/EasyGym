package easygym.service;

import easygym.model.*;
import easygym.repository.Database;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CheckInService {
    private final Database db;
    public CheckInService(Database db) { this.db = db; }
    public CheckIn realizar(int idAluno) {
        Optional<Aluno> aluno = db.buscarAlunoPorId(idAluno);
        if (aluno.isEmpty() || db.buscarUltimoPagamentoAtivo(idAluno).isEmpty()) return null;
        CheckIn c = new CheckIn(idAluno, aluno.get().getNome(), aluno.get().getUnidade());
        db.salvarCheckIn(c);
        return c;
    }
    public List<CheckIn> listarTodos() { return db.listarCheckIns(); }
    public List<CheckIn> listarHoje() { return db.listarCheckInsHoje(); }
    public List<CheckIn> listarPorAluno(int id) { return db.listarCheckInsPorAluno(id); }
}
