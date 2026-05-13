package easygym.service;

import easygym.model.*;
import easygym.repository.Database;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensagemService {
    private final Database db;
    public MensagemService(Database db) { this.db = db; }

    public Mensagem enviarComoAluno(int idAluno, int idProfessor, String texto) {
        String nome = db.buscarAlunoPorId(idAluno).map(Aluno::getNome).orElse("Aluno");
        Mensagem m = new Mensagem(idAluno, idProfessor, nome, Mensagem.Remetente.ALUNO, texto);
        db.salvarMensagem(m);
        return m;
    }

    public Mensagem enviarComoProfessor(int idAluno, int idProfessor, String texto) {
        String nome = db.buscarProfessorPorId(idProfessor).map(Professor::getNome).orElse("Professor");
        Mensagem m = new Mensagem(idAluno, idProfessor, nome, Mensagem.Remetente.PROFESSOR, texto);
        db.salvarMensagem(m);
        return m;
    }

    public List<Mensagem> listarConversa(int idAluno, int idProfessor) {
        db.marcarComoLidasParaAluno(idAluno, idProfessor);
        return db.listarMensagensPorConversa(idAluno, idProfessor);
    }

    public long contarNaoLidasDoAluno(int idAluno) {
        return db.contarNaoLidasDoAluno(idAluno);
    }

    public long contarNaoLidasDoProfessor(int idProfessor) {
        return db.contarNaoLidasDoProfessor(idProfessor);
    }
}
