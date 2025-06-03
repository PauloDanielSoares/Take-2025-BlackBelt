package com.project.Blackbelt;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.project.Blackbelt.Model.Users;
import com.project.Blackbelt.Model.Empresa;
import com.project.Blackbelt.Repository.EmpresaRepository;
import com.project.Blackbelt.Repository.UserRepository;

/**
 * Classe principal da aplicação Blackbelt.
 * <p>
 * Data de criação: 07-04-2025
 * </p>
 * @author Poopstoop1 - Paulo Daniel
 * @version 1.0
 * @since Java 21(JDK 21)
 * 
 * */

@SpringBootApplication
@EntityScan(basePackages = "com.project.Blackbelt.Model")
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories({"com.project.Blackbelt.Repository"})
@EnableTransactionManagement 
@EnableWebMvc
@RestController
@EnableAutoConfiguration
public class BlackbeltApplication {
	
	@Autowired
	 private EmpresaRepository filialRepository;
	 
	 
	 /*
	 @Autowired
	 private SequenceService sequenceService;
	 */
	 

	
	 /**
     * Método principal que inicia a aplicação.
     *
     * @param args Argumentos de linha de comando (não utilizados).
     */
	public static void main(String[] args) {
		SpringApplication.run(BlackbeltApplication.class, args);
	}
	
	  /**
     * Inicializa os dados do sistema ao iniciar a aplicação.
     * Garante que as filiais e usuários padrão sejam criados se não existirem, para que possa ser acessado no sistema.
     * 
     * @param userRepository Repositório de usuários.
     * @return Um {@code CommandLineRunner} para executar a lógica de inicialização.
     */
	 @Bean
	    public CommandLineRunner commandLineRunner(UserRepository userRepository) {
	        return args -> {
	        	/*sequenciaInicial();*/
	        	criarEmpresaSeNaoExistir(filialRepository, "666666666.0001-66", "BlackBelt", "BlackBelt LTDA");
	        	criarEmpresaSeNaoExistir(filialRepository, "444444444.0001-44", "CyberSensei", "CyberSensei LTDA");

				criarUsuarioSeNaoExistir(userRepository, "GabrielLima@gmail.com", "Gestor","Gabriel Lima", "444444444.0001-44", "1234","Usuario");
				criarUsuarioSeNaoExistir(userRepository, "AirtonRibeiro@hotmail.com", "Administrador", "AirtonRibeiro" ,"666666666.0001-66", "1234","Admin");
			
		
	        };
	    }
	 
	 /**
	     * Cria um usuário no sistema se ele ainda não existir.
	     *
	     * @param login Nome de login do usuário.
	     * @param cargo Cargo do usuário.
	     * @param cnpjEmpresa CNPJ da empresa associada.
	     * @param senha Senha do usuário.
	     */
	 
	 private void criarUsuarioSeNaoExistir(UserRepository userRepository, String login, String cargo,String nome, String cnpjEmpresa, String senha, String permissao) {
		 Optional<Empresa> empresa = filialRepository.findById(cnpjEmpresa);  // Busca a filial pelo nome
		    if (empresa.isPresent()) {  // Verifica se a filial existe
		        Users user = userRepository.findByUsername(login);
		        if (user == null) {
		            Users novoUsuario = new Users();
		            novoUsuario.setLogin(login);
		            novoUsuario.setCargo(cargo);
		            novoUsuario.setNome(nome);
		            novoUsuario.setEmpresa(empresa.get());
		            novoUsuario.setPassword(senha); 
		            novoUsuario.setPermissao(permissao);
		            userRepository.save(novoUsuario);
		            System.out.println("Usuário " + login + " criado com sucesso.");
		        }
		    } else {
		    	Empresa filiall = empresa.get(); 
		        System.out.println("A empresa " + filiall.getNome() + " não foi encontrada.");
		    }
		}
	 
	 	/**
	     * Cria uma Empresa no sistema se ela ainda não existir.
	     *
	     * @param cnpj CNPJ da filial.
	     * @param nome Nome da filial.
	     * @param razaosocial Razão social da filial.
	     */
	 private void criarEmpresaSeNaoExistir(EmpresaRepository filialrepository, String cnpj, String nome, String razaosocial) {
			Optional<Empresa> filial = filialrepository.findById(cnpj);
			if (filial.isEmpty()) {
				Empresa novofilial = new Empresa();
				novofilial.setCnpj(cnpj);
				novofilial.setNome(nome);
				novofilial.setRazaosocial(razaosocial); 
				filialrepository.save(novofilial);
				System.out.println("Filial " + nome + " criado com sucesso.");
			}
		}
	 
	 /**
	  * Reinicia a sequência de IDs caso não existam usuários cadastrados.
	  */
	 /*
	 private void sequenciaInicial() {
		 
		 Iterable<Users> users = usuario.findAll();  // Obtém todos os usuários
		    if (!users.iterator().hasNext()) {  // Verifica se a lista está vazia
		        sequenceService.restartSequence(1L);  // Reinicia a sequência se não houver usuários
		        System.out.println("Restart da Sequência Realizada");
		    }
		 }
		 */
	 }
	    


