package easygym.controller;

import easygym.model.*;
import easygym.repository.Database;
import easygym.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private final Database db;
    private final AlunoService alunoService;
    private final PagamentoService pagamentoService;
    private final CheckInService checkInService;
    private final PlanoService planoService;
    private final ModalidadeService modalidadeService;
    private final ProfessorService professorService;
    private final FuncionarioService funcionarioService;

    public MainController(Database db, AlunoService as, PagamentoService pags,
                          CheckInService cs, PlanoService ps, ModalidadeService ms,
                          ProfessorService prs, FuncionarioService fs) {
        this.db = db; this.alunoService = as; this.pagamentoService = pags;
        this.checkInService = cs; this.planoService = ps; this.modalidadeService = ms;
        this.professorService = prs; this.funcionarioService = fs;
    }

    // Helpers
    private void addGlobal(Model model) {
        model.addAttribute("academia", db.getAcademia());
    }

    private boolean isAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    private boolean isAluno(Authentication auth) {
        return auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ALUNO"));
    }

    private String nomeUsuario(Authentication auth) {
        if (auth == null) return "";
        return db.buscarFuncionarioPorEmail(auth.getName())
            .map(Funcionario::getNome).orElse(auth.getName());
    }

    // ── LOGIN ──
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String erro,
                        @RequestParam(required = false) String saiu,
                        Model model) {
        addGlobal(model);
        if (erro != null) model.addAttribute("erro", "Email ou senha incorretos.");
        if (saiu != null) model.addAttribute("saiu", "Você saiu do sistema.");
        return "login";
    }

    // ── DASHBOARD ──
    @GetMapping("/")
    public String dashboard(Model model, Authentication auth) {
        // Alunos vão direto para o portal deles
        if (isAluno(auth)) return "redirect:/aluno/home";
        addGlobal(model);
        model.addAttribute("nomeUsuario", nomeUsuario(auth));
        model.addAttribute("isAdmin", isAdmin(auth));

        List<Aluno> alunos = alunoService.listarTodos();
        long alunosAtivos = db.contarAlunosAtivos();

        Map<String, Long> porUnidade = alunos.stream()
            .collect(Collectors.groupingBy(Aluno::getUnidade, Collectors.counting()));

        List<Map<String, Object>> planoStats = planoService.listarTodos().stream().map(p -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("nome", p.getNome()); m.put("total", p.getTotalAlunos()); m.put("valor", p.getValor());
            return m;
        }).collect(Collectors.toList());

        List<Map<String, Object>> modStats = modalidadeService.listarTodas().stream().map(m -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("nome", m.getNome());
            map.put("ocupacao", (int) Math.min(100, m.getOcupacaoPercent()));
            map.put("alunos", m.getIdAlunos().size());
            map.put("capacidade", m.getCapacidade());
            return map;
        }).collect(Collectors.toList());

        model.addAttribute("totalAlunos", alunos.size());
        model.addAttribute("alunosAtivos", alunosAtivos);
        model.addAttribute("inadimplentes", alunos.size() - alunosAtivos);
        model.addAttribute("receita", String.format("%.2f", db.calcularReceitaTotal()).replace(".", ","));
        model.addAttribute("totalProfessores", professorService.listarTodos().size());
        model.addAttribute("totalModalidades", modalidadeService.listarTodas().size());
        model.addAttribute("checkInsHoje", checkInService.listarHoje().size());
        model.addAttribute("porUnidade", porUnidade);
        model.addAttribute("planoStats", planoStats);
        model.addAttribute("modStats", modStats);
        model.addAttribute("ultimosCheckIns", checkInService.listarHoje());
        return "dashboard";
    }

    // ── ALUNOS ──
    @GetMapping("/alunos")
    public String alunos(Model model, Authentication auth) {
        addGlobal(model);
        model.addAttribute("isAdmin", isAdmin(auth));
        List<Map<String, Object>> lista = alunoService.listarTodos().stream().map(a -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("aluno", a);
            m.put("ativo", pagamentoService.buscarAtivo(a.getId()).isPresent());
            m.put("nomePlano", planoService.buscarPorId(a.getIdPlano()).map(Plano::getNome).orElse("—"));
            return m;
        }).collect(Collectors.toList());
        model.addAttribute("alunosComStatus", lista);
        model.addAttribute("planos", planoService.listarTodos());
        model.addAttribute("niveis", NivelAtividade.values());
        model.addAttribute("filiais", db.getAcademia().getFiliais());
        return "alunos";
    }

    @PostMapping("/alunos/cadastrar")
    public String cadastrarAluno(@RequestParam String nome, @RequestParam int idade,
                                 @RequestParam String endereco, @RequestParam String nivelAtividade,
                                 @RequestParam(defaultValue = "Nenhum") String problemasDeSaude,
                                 @RequestParam double peso, @RequestParam double altura,
                                 @RequestParam int idPlano, @RequestParam String unidade,
                                 @RequestParam(defaultValue = "") String email,
                                 @RequestParam(defaultValue = "") String telefone,
                                 RedirectAttributes ra) {
        alunoService.cadastrar(nome, idade, endereco, NivelAtividade.valueOf(nivelAtividade),
            problemasDeSaude, peso, altura, idPlano, unidade, email, telefone);
        ra.addFlashAttribute("sucesso", "Aluno " + nome + " cadastrado com sucesso!");
        return "redirect:/alunos";
    }

    @PostMapping("/alunos/remover/{id}")
    public String removerAluno(@PathVariable int id, RedirectAttributes ra) {
        alunoService.remover(id);
        ra.addFlashAttribute("sucesso", "Aluno removido.");
        return "redirect:/alunos";
    }

    @GetMapping("/alunos/{id}")
    public String verAluno(@PathVariable int id, Model model, Authentication auth) {
        return alunoService.buscarPorId(id).map(a -> {
            addGlobal(model);
            model.addAttribute("isAdmin", isAdmin(auth));
            model.addAttribute("aluno", a);
            model.addAttribute("nomePlano", planoService.buscarPorId(a.getIdPlano()).map(Plano::getNome).orElse("—"));
            model.addAttribute("planoAtual", planoService.buscarPorId(a.getIdPlano()).orElse(null));
            model.addAttribute("pagamentoAtivo", pagamentoService.buscarAtivo(id));
            model.addAttribute("historicoPagamentos", pagamentoService.listarPorAluno(id));
            model.addAttribute("checkIns", checkInService.listarPorAluno(id));
            model.addAttribute("planos", planoService.listarTodos());
            model.addAttribute("metodos", Pagamento.MetodoPagamento.values());
            return "aluno-detalhe";
        }).orElse("redirect:/alunos");
    }

    @PostMapping("/alunos/{id}/plano")
    public String atualizarPlano(@PathVariable int id, @RequestParam int idPlano, RedirectAttributes ra) {
        alunoService.atualizarPlano(id, idPlano);
        ra.addFlashAttribute("sucesso", "Plano atualizado!");
        return "redirect:/alunos/" + id;
    }

    @PostMapping("/alunos/{id}/pagamento")
    public String registrarPagamento(@PathVariable int id, @RequestParam String metodo,
                                     @RequestParam int meses, RedirectAttributes ra) {
        Pagamento p = pagamentoService.registrar(id, metodo, meses);
        if (p != null) ra.addFlashAttribute("sucesso", "Pagamento registrado! Válido até " + p.getValidadeFormatada());
        else ra.addFlashAttribute("erro", "Erro ao registrar pagamento.");
        return "redirect:/alunos/" + id;
    }

    // ── PROFESSORES ──
    @GetMapping("/professores")
    public String professores(Model model, Authentication auth) {
        addGlobal(model);
        model.addAttribute("isAdmin", isAdmin(auth));
        model.addAttribute("professores", professorService.listarTodos());
        model.addAttribute("modalidades", modalidadeService.listarTodas());
        return "professores";
    }

    @PostMapping("/professores/cadastrar")
    public String cadastrarProfessor(@RequestParam String nome, @RequestParam String modalidade,
                                     @RequestParam String unidade, @RequestParam(defaultValue="") String email,
                                     @RequestParam(defaultValue="") String telefone, RedirectAttributes ra) {
        professorService.cadastrar(nome, modalidade, unidade, email, telefone);
        ra.addFlashAttribute("sucesso", "Professor " + nome + " cadastrado!");
        return "redirect:/professores";
    }

    @PostMapping("/professores/remover/{id}")
    public String removerProfessor(@PathVariable int id, RedirectAttributes ra) {
        professorService.remover(id);
        ra.addFlashAttribute("sucesso", "Professor removido.");
        return "redirect:/professores";
    }

    // ── PLANOS ──
    @GetMapping("/planos")
    public String planos(Model model, Authentication auth) {
        addGlobal(model);
        model.addAttribute("isAdmin", isAdmin(auth));
        model.addAttribute("planos", planoService.listarTodos());
        return "planos";
    }

    @PostMapping("/planos/cadastrar")
    public String cadastrarPlano(@RequestParam String nome, @RequestParam String nivel,
                                 @RequestParam double valor, @RequestParam(defaultValue="") String descricao,
                                 @RequestParam(defaultValue="") String beneficios, RedirectAttributes ra) {
        Plano p = planoService.cadastrar(nome, nivel, valor, descricao);
        if (!beneficios.isBlank()) {
            for (String b : beneficios.split("\n")) {
                String trimmed = b.trim();
                if (!trimmed.isEmpty()) p.adicionarBeneficio(trimmed);
            }
        }
        ra.addFlashAttribute("sucesso", "Plano " + nome + " cadastrado!");
        return "redirect:/planos";
    }

    @PostMapping("/planos/remover/{id}")
    public String removerPlano(@PathVariable int id, RedirectAttributes ra) {
        planoService.remover(id);
        ra.addFlashAttribute("sucesso", "Plano removido.");
        return "redirect:/planos";
    }

    // ── MODALIDADES ──
    @GetMapping("/modalidades")
    public String modalidades(Model model, Authentication auth) {
        addGlobal(model);
        model.addAttribute("isAdmin", isAdmin(auth));
        List<Map<String, Object>> lista = modalidadeService.listarTodas().stream().map(m -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("modalidade", m);
            map.put("professores", m.getIdProfessores().stream()
                .map(pid -> professorService.buscarPorId(pid).map(Professor::getNome).orElse("?"))
                .collect(Collectors.toList()));
            return map;
        }).collect(Collectors.toList());
        model.addAttribute("modalidadesComProfs", lista);
        model.addAttribute("professores", professorService.listarTodos());
        return "modalidades";
    }

    @PostMapping("/modalidades/cadastrar")
    public String cadastrarModalidade(@RequestParam String nome, @RequestParam String unidade,
                                      @RequestParam int capacidade, @RequestParam(defaultValue="") String horarios,
                                      RedirectAttributes ra) {
        var m = modalidadeService.cadastrar(nome, unidade, capacidade);
        if (!horarios.isBlank())
            for (String h : horarios.split(",")) modalidadeService.adicionarHorario(m.getId(), h.trim());
        ra.addFlashAttribute("sucesso", "Modalidade " + nome + " cadastrada!");
        return "redirect:/modalidades";
    }

    @PostMapping("/modalidades/remover/{id}")
    public String removerModalidade(@PathVariable int id, RedirectAttributes ra) {
        modalidadeService.remover(id);
        ra.addFlashAttribute("sucesso", "Modalidade removida.");
        return "redirect:/modalidades";
    }

    // ── CHECK-IN ──
    @GetMapping("/checkin")
    public String checkin(Model model, Authentication auth) {
        addGlobal(model);
        model.addAttribute("isAdmin", isAdmin(auth));
        model.addAttribute("alunos", alunoService.listarTodos());
        model.addAttribute("checkInsHoje", checkInService.listarHoje());
        return "checkin";
    }

    @PostMapping("/checkin/realizar")
    public String realizarCheckin(@RequestParam int idAluno, RedirectAttributes ra) {
        CheckIn c = checkInService.realizar(idAluno);
        if (c == null) ra.addFlashAttribute("erro", "Check-in negado: pagamento vencido ou aluno inválido.");
        else ra.addFlashAttribute("sucesso", "Check-in realizado para " + c.getNomeAluno() + "!");
        return "redirect:/checkin";
    }

    // ── ACADEMIA (somente ADMIN) ──
    @GetMapping("/academia")
    public String academia(Model model) {
        addGlobal(model);
        model.addAttribute("funcionarios", funcionarioService.listarTodos());
        model.addAttribute("perfis", Funcionario.Perfil.values());
        return "academia";
    }

    @PostMapping("/academia/salvar")
    public String salvarAcademia(@RequestParam String nome, @RequestParam(defaultValue="") String cnpj,
                                  @RequestParam(defaultValue="") String emailContato,
                                  @RequestParam(defaultValue="") String telefone,
                                  @RequestParam(defaultValue="#f0b429") String corPrimaria,
                                  @RequestParam(defaultValue="#e05c2a") String corSecundaria,
                                  @RequestParam("logo") MultipartFile logo,
                                  RedirectAttributes ra) throws IOException {
        Academia ac = db.getAcademia();
        ac.setNome(nome); ac.setCnpj(cnpj);
        ac.setEmailContato(emailContato); ac.setTelefone(telefone);
        ac.setCorPrimaria(corPrimaria); ac.setCorSecundaria(corSecundaria);

        if (!logo.isEmpty()) {
            Path uploadDir = Paths.get("uploads");
            Files.createDirectories(uploadDir);
            String filename = "logo_" + System.currentTimeMillis() + "_" + logo.getOriginalFilename();
            Files.copy(logo.getInputStream(), uploadDir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            ac.setLogoPath("/uploads/" + filename);
        }

        ra.addFlashAttribute("sucesso", "Dados da academia salvos!");
        return "redirect:/academia";
    }

    @PostMapping("/academia/filial/adicionar")
    public String adicionarFilial(@RequestParam String nome, @RequestParam String endereco,
                                   @RequestParam String cidade, @RequestParam String telefone,
                                   RedirectAttributes ra) {
        db.getAcademia().adicionarFilial(new Filial(nome, endereco, cidade, telefone));
        ra.addFlashAttribute("sucesso", "Filial adicionada!");
        return "redirect:/academia";
    }

    @PostMapping("/academia/filial/remover/{id}")
    public String removerFilial(@PathVariable int id, RedirectAttributes ra) {
        db.getAcademia().removerFilial(id);
        ra.addFlashAttribute("sucesso", "Filial removida.");
        return "redirect:/academia";
    }

    // ── FUNCIONÁRIOS (somente ADMIN) ──
    @PostMapping("/funcionarios/cadastrar")
    public String cadastrarFuncionario(@RequestParam String nome, @RequestParam String email,
                                        @RequestParam String senha, @RequestParam String perfil,
                                        @RequestParam(defaultValue="") String unidade,
                                        RedirectAttributes ra) {
        funcionarioService.cadastrar(nome, email, senha, Funcionario.Perfil.valueOf(perfil), unidade);
        ra.addFlashAttribute("sucesso", "Funcionário " + nome + " cadastrado!");
        return "redirect:/academia";
    }

    @PostMapping("/funcionarios/remover/{id}")
    public String removerFuncionario(@PathVariable int id, RedirectAttributes ra) {
        funcionarioService.remover(id);
        ra.addFlashAttribute("sucesso", "Funcionário removido.");
        return "redirect:/academia";
    }

    @PostMapping("/funcionarios/status/{id}")
    public String alterarStatus(@PathVariable int id, @RequestParam boolean ativo, RedirectAttributes ra) {
        funcionarioService.alterarStatus(id, ativo);
        ra.addFlashAttribute("sucesso", "Status atualizado.");
        return "redirect:/academia";
    }
}
