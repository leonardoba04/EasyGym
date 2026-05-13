package easygym;

import easygym.service.DataSeeder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EasyGymApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(EasyGymApplication.class, args);
        ctx.getBean(DataSeeder.class).popular();
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║         EasyGym V2 iniciado!                       ║");
        System.out.println("║         Acesse: http://localhost:8080              ║");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.println("║  EQUIPE                                            ║");
        System.out.println("║  Admin:       admin@fitzone.com   / admin123       ║");
        System.out.println("║  Funcionario: joao@fitzone.com    / func123        ║");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.println("║  ALUNOS (matricula / senha)                        ║");
        System.out.println("║  MAT00001 (Joao Pedro)    / 123456                 ║");
        System.out.println("║  MAT00002 (Maria Clara)   / 123456                 ║");
        System.out.println("║  MAT00003 (Pedro Henrique)/ 123456                 ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");
    }
}
