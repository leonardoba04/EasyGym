package easygym.service;

import easygym.model.*;
import easygym.repository.Database;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder {
    private final Database db;
    private final PlanoService planoService;
    private final ModalidadeService modalidadeService;
    private final ProfessorService professorService;
    private final AlunoService alunoService;
    private final PagamentoService pagamentoService;
    private final CheckInService checkInService;
    private final FuncionarioService funcionarioService;
    private final MensagemService mensagemService;

    public DataSeeder(Database db, PlanoService ps, ModalidadeService ms, ProfessorService prs,
                      AlunoService as, PagamentoService pags, CheckInService cs,
                      FuncionarioService fs, MensagemService msgs) {
        this.db = db; this.planoService = ps; this.modalidadeService = ms;
        this.professorService = prs; this.alunoService = as; this.pagamentoService = pags;
        this.checkInService = cs; this.funcionarioService = fs; this.mensagemService = msgs;
    }

    public void popular() {
        // Academia demo
        Academia ac = db.getAcademia();
        ac.setNome("FitZone Academia");
        ac.setCnpj("12.345.678/0001-90");
        ac.setEmailContato("contato@fitzone.com.br");
        ac.setTelefone("(11) 3000-0000");
        ac.adicionarFilial(new Filial("Unidade Centro", "Av. Paulista, 1000", "São Paulo", "(11) 3000-0001"));
        ac.adicionarFilial(new Filial("Unidade Zona Sul", "Rua dos Pinheiros, 500", "São Paulo", "(11) 3000-0002"));

        // Funcionários
        funcionarioService.cadastrar("Admin EasyGym", "admin@fitzone.com", "admin123",
            Funcionario.Perfil.ADMIN, "Centro");
        funcionarioService.cadastrar("João Recepção", "joao@fitzone.com", "func123",
            Funcionario.Perfil.FUNCIONARIO, "Centro");
        funcionarioService.cadastrar("Maria Atendimento", "maria@fitzone.com", "func123",
            Funcionario.Perfil.FUNCIONARIO, "Zona Sul");

        // Planos
        Plano basico = planoService.cadastrar("Plano Básico", "Básico", 89.90,
            "Ideal para quem está começando");
        basico.adicionarBeneficio("Musculação livre");
        basico.adicionarBeneficio("Área cardio");
        basico.adicionarBeneficio("Vestiário");

        Plano inter = planoService.cadastrar("Plano Intermediário", "Intermediário", 139.90,
            "Para quem quer evoluir com aulas");
        inter.adicionarBeneficio("Tudo do Básico");
        inter.adicionarBeneficio("2 aulas coletivas/semana");
        inter.adicionarBeneficio("Avaliação física mensal");

        Plano premium = planoService.cadastrar("Plano Premium", "Premium", 199.90,
            "Acesso completo sem limites");
        premium.adicionarBeneficio("Tudo do Intermediário");
        premium.adicionarBeneficio("Aulas ilimitadas");
        premium.adicionarBeneficio("Personal trainer 2x/semana");
        premium.adicionarBeneficio("Acesso a todas as filiais");

        // Modalidades
        Modalidade musc = modalidadeService.cadastrar("Musculação", "Centro", 50);
        modalidadeService.adicionarHorario(musc.getId(), "06:00");
        modalidadeService.adicionarHorario(musc.getId(), "12:00");
        modalidadeService.adicionarHorario(musc.getId(), "18:00");

        Modalidade spin = modalidadeService.cadastrar("Spinning", "Centro", 20);
        modalidadeService.adicionarHorario(spin.getId(), "07:00");
        modalidadeService.adicionarHorario(spin.getId(), "19:00");

        Modalidade yoga = modalidadeService.cadastrar("Yoga", "Zona Sul", 15);
        modalidadeService.adicionarHorario(yoga.getId(), "08:00");
        modalidadeService.adicionarHorario(yoga.getId(), "17:00");

        Modalidade natacao = modalidadeService.cadastrar("Natação", "Zona Sul", 30);
        modalidadeService.adicionarHorario(natacao.getId(), "07:00");
        modalidadeService.adicionarHorario(natacao.getId(), "18:00");

        // Professores
        Professor p1 = professorService.cadastrar("Carlos Silva", "Musculação", "Centro", "carlos@fitzone.com", "(11) 99999-0001");
        Professor p2 = professorService.cadastrar("Ana Souza", "Spinning", "Centro", "ana@fitzone.com", "(11) 99999-0002");
        Professor p3 = professorService.cadastrar("Mariana Lima", "Yoga", "Zona Sul", "mariana@fitzone.com", "(11) 99999-0003");
        Professor p4 = professorService.cadastrar("Roberto Costa", "Natação", "Zona Sul", "roberto@fitzone.com", "(11) 99999-0004");

        modalidadeService.vincularProfessor(musc.getId(), p1.getId());
        modalidadeService.vincularProfessor(spin.getId(), p2.getId());
        modalidadeService.vincularProfessor(yoga.getId(), p3.getId());
        modalidadeService.vincularProfessor(natacao.getId(), p4.getId());

        // Alunos
        Aluno a1 = alunoService.cadastrar("João Pedro", 25, "Rua das Flores, 100",
            NivelAtividade.INICIANTE, "Nenhum", 80.0, 1.75, basico.getId(), "Centro", "joao.pedro@email.com", "(11) 98888-0001");
        Aluno a2 = alunoService.cadastrar("Maria Clara", 30, "Av. Paulista, 200",
            NivelAtividade.INTERMEDIARIO, "Hipertensão leve", 65.0, 1.65, inter.getId(), "Centro", "maria.clara@email.com", "(11) 98888-0002");
        Aluno a3 = alunoService.cadastrar("Pedro Henrique", 22, "Rua XV, 50",
            NivelAtividade.AVANCADO, "Nenhum", 85.0, 1.80, premium.getId(), "Zona Sul", "pedro.h@email.com", "(11) 98888-0003");
        Aluno a4 = alunoService.cadastrar("Ana Beatriz", 28, "Rua dos Pinheiros, 300",
            NivelAtividade.SEDENTARIO, "Lombalgia", 70.0, 1.68, basico.getId(), "Zona Sul", "ana.b@email.com", "(11) 98888-0004");
        Aluno a5 = alunoService.cadastrar("Lucas Ferreira", 35, "Alameda Santos, 400",
            NivelAtividade.INTERMEDIARIO, "Nenhum", 90.0, 1.82, inter.getId(), "Centro", "lucas.f@email.com", "(11) 98888-0005");

        // Pagamentos
        pagamentoService.registrar(a1.getId(), "PIX", 1);
        pagamentoService.registrar(a2.getId(), "CARTAO_CREDITO", 3);
        pagamentoService.registrar(a3.getId(), "BOLETO", 1);
        pagamentoService.registrar(a5.getId(), "PIX", 2);

        // Check-ins
        checkInService.realizar(a1.getId());
        checkInService.realizar(a2.getId());
        checkInService.realizar(a3.getId());

        // Demo messages
        mensagemService.enviarComoAluno(a1.getId(), p1.getId(),
            "Olá Professor Carlos! Tenho alguma dúvida sobre os exercícios de costas.");
        mensagemService.enviarComoProfessor(a1.getId(), p1.getId(),
            "Olá João! Claro, me diz qual exercício está com dúvida.");
        mensagemService.enviarComoAluno(a2.getId(), p2.getId(),
            "Professora Ana, posso entrar na aula de spinning de amanhã às 19h?");

        System.out.println("\n  Matrículas dos alunos demo:");
        System.out.println("  " + a1.getNome() + " → " + a1.getMatricula() + " / senha: 123456");
        System.out.println("  " + a2.getNome() + " → " + a2.getMatricula() + " / senha: 123456");
        System.out.println("  " + a3.getNome() + " → " + a3.getMatricula() + " / senha: 123456");
    }
}
