package com.project.Blackbelt.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Blackbelt.Model.Documento;
import com.project.Blackbelt.Model.Empresa;
import com.project.Blackbelt.Model.Users;
import com.project.Blackbelt.Repository.DocumentoRepository;
import com.project.Blackbelt.Repository.EmpresaRepository;
import com.project.Blackbelt.Repository.UserRepository;


@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DocumentoRepository documentoRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private ObjectMapper objectMapper; 
    @RequestMapping("/")
    public String index(@RequestParam(required = false) String busca,
    	    @RequestParam(required = false) String criticidade,
    	    @RequestParam(required = false) String status, Model model) throws JsonProcessingException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users usua = userRepository.findByUsername(username);
    
        System.out.println("Filtrando documentos - Busca: '" + busca + 
                "', Criticidade: '" + criticidade + 
                "', Status: '" + status + "'");
        
        // Passar o nome formatado do usuário
        if (username != null) {
            
            model.addAttribute("message", usua.getNome());
        }
        
        List<Documento> documentos;
        
        if (auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MANAGER"))) {
        
        	documentos = documentoRepository.filtrarDocumentos(busca, criticidade, status);
        }else {
        	List<Documento> docsEmpresa = documentoRepository.findDocumentoByCnpj(usua.getCnpjEmpresa());
            documentos = docsEmpresa.stream()
                .filter(d -> (busca == null || 
                              d.getAtivo().toLowerCase().contains(busca.toLowerCase()) || 
                              d.getIp().toLowerCase().contains(busca.toLowerCase()) || 
                              d.getCves().toLowerCase().contains(busca.toLowerCase()) || 
                              d.getResponsavel().toLowerCase().contains(busca.toLowerCase()))
                         && (criticidade == null || d.getCriticidade().equals(criticidade))
                         && (status == null || d.getStatus().equals(status)))
                .toList();
        	
        }
        model.addAttribute("documentos", documentos);
        model.addAttribute("documentosJson", objectMapper.writeValueAsString(documentos));
     
    
        return "dashboard";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/editardocumentos")
	 public ModelAndView editarDocumento(@ModelAttribute Documento documento) {
	     // Busca o documento pelo id
	     Optional<Documento> documentoExistente = documentoRepository.findById(documento.getId());
	     
	     if (documentoExistente.isPresent()) {
	         // Atualiza os dados da filial existente
	         Documento existente = documentoExistente.get();
	         existente.setResponsavel(documento.getResponsavel());
	         existente.setStatus(documento.getStatus());
	         existente.setData_correcao(documento.getData_correcao());
	         existente.setObservacao(documento.getObservacao());
	         existente.setCorrecao(documento.getCorrecao());
	         
	         // Salva as alterações no banco
	         documentoRepository.save(existente);
	     } else {
	         System.out.println("Documento não encontrado: " + documento.getId());
	     }
	     
	     // Redireciona para a página de gestão de filiais
	     return new ModelAndView("redirect:/");
	 }

    @RequestMapping(method = RequestMethod.GET, value = "/removerdocumento/{iddocumento}")
    public String excluir(@PathVariable("iddocumento") Long iddocumento) {
        documentoRepository.deleteById(iddocumento);
        return "redirect:/";
    }
    
    
    @RequestMapping(value = "/importar-csv", method = RequestMethod.POST)
    public String importarCSV(@RequestParam("file") MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("UTF-8")))) {
            String line;
            boolean primeiraLinha = true;

            while ((line = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue; // pula o cabeçalho
                }

                if (line.trim().isEmpty()) {
                    continue; // pula linha vazia
                }

                String[] colunas = line.split(";", -1);

                if (colunas.length < 10) {
                    System.out.println("Linha ignorada por ter menos de 10 colunas: " + line);
                    continue;
                }

                String nomeEmpresa = colunas[1];
                if (nomeEmpresa == null) {
                    continue;
                }

                Empresa empresa = empresaRepository.findByNome(nomeEmpresa);

                if (empresa == null) {
                    System.out.println("Empresa não encontrada para o nome: " + nomeEmpresa);
                    continue;
                }

                Boolean docExistente = documentoRepository.existsDocumentoCompleto(
                    colunas[0], // data
                    colunas[1],
                    colunas[2], // ip
                    colunas[3], // cves
                    colunas[4], // criticidade
                    colunas[5], // recomendacao_correcao
                    colunas[6], // responsavel
                    empresa
                );
                if (!docExistente) {
                    Documento doc = new Documento();
                    doc.setData(colunas[0]);
                    doc.setAtivo(nomeEmpresa);
                    doc.setIp(colunas[2]);
                    doc.setCves(colunas[3]);
                    doc.setCriticidade(colunas[4]);
                    doc.setRecomendacao_correcao(colunas[5]);
                    doc.setResponsavel(colunas[6]);
                    doc.setStatus(colunas[7]);
                    doc.setData_correcao(colunas[8]);
                    doc.setObservacao(colunas[9]);
                    doc.setEmpresa(empresa);
                    documentoRepository.save(doc);
                } else {
                    System.out.println("Documento já existente: IP " + colunas[1] + " na data " + colunas[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }
    

    
    /*private String formatarNome(String username) {
        // Formata o nome para exibição (exemplo: "ErysonMoreira" -> "Eryson Moreira")
        return username.replaceAll("([a-z])([A-Z])", "$1 $2");
    }*/
}
