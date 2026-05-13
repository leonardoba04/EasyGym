package easygym.controller;

import easygym.model.*;
import easygym.repository.Database;
import easygym.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/aluno")
public class AlunoPortalController {

    private final Database db;
    private final PagamentoService pagamentoService;
    private final CheckInService checkInService;
    private final PlanoService planoService;
    private final ModalidadeService modalidadeService;
    private final ProfessorService professorService;
    private final MensagemService mensagemService;
    private final AlunoService alunoService;

    public AlunoPortalController(Database db, PagamentoService pags, CheckInService cs,
                                  PlanoService ps, ModalidadeService ms, ProfessorService prs,
                                  MensagemService msgs, AlunoService as) {
        this.db = db; this.pagamentoService = pags; this.checkInService = cs;
        this.planoService = ps; this.modalidadeService = ms; this.professorService = prs;
        this.mensagemService = msgs; this.alunoService = as;
    }

    /** Resolve o aluno logado pela matrícula no contexto de segurança */
    private Optional<Aluno> alunoLogado(Authentication auth) {
        if (auth == null) return Optional.empty();
        return db.buscarAlunoPorMatricula(auth.getName());
    }

    private void addGlobal(Model model, Aluno aluno) {
        model.addAttribute("academia", db.getAcademia());
        model.addAttribute("aluno", aluno);
        // Garante que naoLidasMensagens nunca seja null no layout
        model.addAttribute("naoLidasMensagens",
            mensagemService.contarNaoLidasDoAluno(aluno.getId()));
    }

    // ── HOME ──────────────────────────────────────────────────────────────
    @GetMapping({"", "/", "/home"})
    public String home(Model model, Authentication auth) {
        Optional<Aluno> opt = alunoLogado(auth);
        if (opt.isEmpty()) return "redirect:/login";
        Aluno aluno = opt.get();
        addGlobal(model, aluno);

        model.addAttribute("plano", planoService.buscarPorId(aluno.getIdPlano()).orElse(null));
        model.addAttribute("pagamentoAtivo", pagamentoService.buscarAtivo(aluno.getId()));

        List<CheckIn> todos = checkInService.listarPorAluno(aluno.getId());
        long frequenciaMes = todos.stream()
            .filter(c -> c.getData().getMonth() == LocalDate.now().getMonth()
                && c.getData().getYear() == LocalDate.now().getYear())
            .count();

        model.addAttribute("frequenciaMes", frequenciaMes);
        model.addAttribute("ultimosCheckIns", todos.stream()
            .sorted(Comparator.comparing(CheckIn::getData).reversed())
            .limit(5).collect(Collectors.toList()));

        List<Modalidade> modalidades = modalidadeService.listarTodas().stream()
            .filter(m -> m.getUnidade().equalsIgnoreCase(aluno.getUnidade()))
            .collect(Collectors.toList());
        model.addAttribute("modalidades", modalidades);

        return "portal/home";
    }

    // ── PERFIL ────────────────────────────────────────────────────────────
    @GetMapping("/perfil")
    public String perfil(Model model, Authentication auth) {
        Optional<Aluno> opt = alunoLogado(auth);
        if (opt.isEmpty()) return "redirect:/login";
        Aluno aluno = opt.get();
        addGlobal(model, aluno);

        model.addAttribute("plano", planoService.buscarPorId(aluno.getIdPlano()).orElse(null));
        model.addAttribute("pagamentoAtivo", pagamentoService.buscarAtivo(aluno.getId()));
        return "portal/perfil";
    }

    @PostMapping("/perfil/senha")
    public String alterarSenha(Authentication auth,
                                @RequestParam String senhaAtual,
                                @RequestParam String novaSenha,
                                RedirectAttributes ra) {
        alunoLogado(auth).ifPresent(aluno -> {
            var enc = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
            if (enc.matches(senhaAtual, aluno.getSenha())) {
                alunoService.alterarSenha(aluno.getId(), novaSenha);
                ra.addFlashAttribute("sucesso", "Senha alterada com sucesso!");
            } else {
                ra.addFlashAttribute("erro", "Senha atual incorreta.");
            }
        });
        return "redirect:/aluno/perfil";
    }

    // ── HORÁRIOS ──────────────────────────────────────────────────────────
    @GetMapping("/horarios")
    public String horarios(Model model, Authentication auth) {
        Optional<Aluno> opt = alunoLogado(auth);
        if (opt.isEmpty()) return "redirect:/login";
        Aluno aluno = opt.get();
        addGlobal(model, aluno);

        List<Map<String, Object>> modComProfs = modalidadeService.listarTodas().stream()
            .filter(m -> m.getUnidade().equalsIgnoreCase(aluno.getUnidade()))
            .map(m -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("modalidade", m);
                map.put("professores", m.getIdProfessores().stream()
                    .map(pid -> professorService.buscarPorId(pid).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
                return map;
            }).collect(Collectors.toList());

        model.addAttribute("modComProfs", modComProfs);
        return "portal/horarios";
    }

    // ── FREQUÊNCIA ────────────────────────────────────────────────────────
    @GetMapping("/frequencia")
    public String frequencia(Model model, Authentication auth) {
        Optional<Aluno> opt = alunoLogado(auth);
        if (opt.isEmpty()) return "redirect:/login";
        Aluno aluno = opt.get();
        addGlobal(model, aluno);

        List<CheckIn> todos = checkInService.listarPorAluno(aluno.getId());
        List<CheckIn> ordenados = todos.stream()
            .sorted(Comparator.comparing(CheckIn::getData).reversed())
            .collect(Collectors.toList());

        Map<String, Long> porMes = todos.stream()
            .collect(Collectors.groupingBy(
                c -> c.getData().format(java.time.format.DateTimeFormatter.ofPattern(
                    "MMM/yyyy", new Locale("pt", "BR"))),
                LinkedHashMap::new, Collectors.counting()));

        long totalMes = todos.stream()
            .filter(c -> c.getData().getMonth() == LocalDate.now().getMonth()
                && c.getData().getYear() == LocalDate.now().getYear())
            .count();

        model.addAttribute("checkIns", ordenados);
        model.addAttribute("porMes", porMes);
        model.addAttribute("totalMes", totalMes);
        model.addAttribute("totalGeral", todos.size());
        return "portal/frequencia";
    }

    // ── MENSAGENS ─────────────────────────────────────────────────────────
    @GetMapping("/mensagens")
    public String mensagens(Model model, Authentication auth) {
        Optional<Aluno> opt = alunoLogado(auth);
        if (opt.isEmpty()) return "redirect:/login";
        Aluno aluno = opt.get();
        addGlobal(model, aluno);

        List<Professor> profs = professorService.listarTodos().stream()
            .filter(p -> p.getUnidade().equalsIgnoreCase(aluno.getUnidade()))
            .collect(Collectors.toList());
        model.addAttribute("professores", profs);
        return "portal/mensagens";
    }

    @GetMapping("/mensagens/{idProfessor}")
    public String conversa(@PathVariable int idProfessor, Model model, Authentication auth) {
        Optional<Aluno> opt = alunoLogado(auth);
        if (opt.isEmpty()) return "redirect:/login";
        Aluno aluno = opt.get();

        Optional<Professor> prof = professorService.buscarPorId(idProfessor);
        if (prof.isEmpty()) return "redirect:/aluno/mensagens";

        addGlobal(model, aluno);
        model.addAttribute("professor", prof.get());
        model.addAttribute("mensagens", mensagemService.listarConversa(aluno.getId(), idProfessor));
        return "portal/conversa";
    }

    @PostMapping("/mensagens/{idProfessor}/enviar")
    public String enviarMensagem(@PathVariable int idProfessor,
                                  @RequestParam String texto,
                                  Authentication auth) {
        alunoLogado(auth).ifPresent(aluno ->
            mensagemService.enviarComoAluno(aluno.getId(), idProfessor, texto));
        return "redirect:/aluno/mensagens/" + idProfessor;
    }
}
