package com.zenotime.config;

import com.zenotime.entity.*;
import com.zenotime.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;
    
    @Autowired
    private ProjetoRepository projetoRepository;
    
    @Autowired
    private TimeRepository timeRepository;
    
    @Autowired
    private SprintRepository sprintRepository;
    
    @Autowired
    private RegistroPontoRepository registroPontoRepository;
    
    @Autowired
    private SolicitacaoRepository solicitacaoRepository;
    
    @Autowired
    private FuncionarioEmpresaRepository funcionarioEmpresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private final Random random = new Random();
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("DataInitializer executado!");
    }
    
    private void criarAdministrador() {
        if (usuarioRepository.findByEmail("vitmonjo@gmail.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("vitmonjo@gmail.com");
            admin.setSenha(passwordEncoder.encode("28DJp9NRN3bXE#@XxJu9"));
            admin.setTipo(Usuario.TipoUsuario.ADMINISTRADOR);
            admin.setAtivo(true);
            usuarioRepository.save(admin);
            System.out.println("Usuário administrador criado: vitmonjo@gmail.com");
        }
    }
    
    private List<Empresa> criarEmpresas() {
        String[][] empresasData = {
            {"TechSolutions", "12345678000190", "Tecnologia e Software"},
            {"HealthCare Plus", "12345678000191", "Saúde e Bem-estar"},
            {"EduTech Brasil", "12345678000192", "Educação e E-learning"},
            {"GreenEnergy", "12345678000193", "Energia Renovável"},
            {"FoodDelivery Express", "12345678000194", "Delivery e Alimentação"},
            {"FinanceHub", "12345678000195", "Serviços Financeiros"},
            {"RetailMax", "12345678000196", "Varejo e E-commerce"},
            {"Construction Pro", "12345678000197", "Construção Civil"},
            {"MediaGroup", "12345678000198", "Mídia e Comunicação"},
            {"Logistics Corp", "12345678000199", "Logística e Transporte"}
        };
        
        List<Empresa> empresas = new ArrayList<>();
        for (String[] data : empresasData) {
            if (empresaRepository.findByCnpj(data[1]).isEmpty()) {
                Empresa empresa = new Empresa();
                empresa.setNome(data[0]);
                empresa.setCnpj(data[1]);
                empresa.setAtivo(true);
                empresas.add(empresaRepository.save(empresa));
            } else {
                empresas.add(empresaRepository.findByCnpj(data[1]).get());
            }
        }
        return empresas;
    }
    
    private List<Usuario> criarFuncionarios(List<Empresa> empresas) {
        class FuncionarioData {
            String nome;
            String email;
            int empresaIndex;
            
            FuncionarioData(String nome, String email, int empresaIndex) {
                this.nome = nome;
                this.email = email;
                this.empresaIndex = empresaIndex;
            }
        }
        
        FuncionarioData[] funcionariosData = {
            // Funcionário especial para demonstração no vídeo
            new FuncionarioData("João Vítor Monteiro", "funcionario@zenotime.com", 0),
            new FuncionarioData("João Silva", "joao.silva@techsolutions.com", 0),
            new FuncionarioData("Maria Santos", "maria.santos@techsolutions.com", 0),
            new FuncionarioData("Pedro Oliveira", "pedro.oliveira@healthcare.com", 1),
            new FuncionarioData("Ana Costa", "ana.costa@healthcare.com", 1),
            new FuncionarioData("Carlos Pereira", "carlos.pereira@edutech.com", 2),
            new FuncionarioData("Juliana Lima", "juliana.lima@edutech.com", 2),
            new FuncionarioData("Roberto Alves", "roberto.alves@greenenergy.com", 3),
            new FuncionarioData("Fernanda Rocha", "fernanda.rocha@greenenergy.com", 3),
            new FuncionarioData("Lucas Martins", "lucas.martins@fooddelivery.com", 4),
            new FuncionarioData("Patricia Souza", "patricia.souza@fooddelivery.com", 4),
            new FuncionarioData("Ricardo Ferreira", "ricardo.ferreira@financehub.com", 5),
            new FuncionarioData("Camila Barbosa", "camila.barbosa@financehub.com", 5),
            new FuncionarioData("Bruno Carvalho", "bruno.carvalho@retailmax.com", 6),
            new FuncionarioData("Amanda Dias", "amanda.dias@retailmax.com", 6),
            new FuncionarioData("Felipe Ribeiro", "felipe.ribeiro@construction.com", 7)
        };
        
        List<Usuario> funcionarios = new ArrayList<>();
        for (FuncionarioData data : funcionariosData) {
            if (usuarioRepository.findByEmail(data.email).isEmpty()) {
                Usuario funcionario = new Usuario();
                funcionario.setNome(data.nome);
                funcionario.setEmail(data.email);
                funcionario.setSenha(passwordEncoder.encode("senha123"));
                funcionario.setTipo(Usuario.TipoUsuario.FUNCIONARIO);
                funcionario.setAtivo(true);
                funcionario = usuarioRepository.save(funcionario);
                
                // Associar funcionário à empresa usando a entidade intermediária
                Empresa empresa = empresas.get(data.empresaIndex);
                
                // Criar relacionamento usando a entidade intermediária
                FuncionarioEmpresa funcionarioEmpresa = new FuncionarioEmpresa();
                FuncionarioEmpresa.FuncionarioEmpresaId id = new FuncionarioEmpresa.FuncionarioEmpresaId();
                id.setFuncionarioId(funcionario.getId());
                id.setEmpresaId(empresa.getId());
                funcionarioEmpresa.setId(id);
                funcionarioEmpresa.setFuncionario(funcionario);
                funcionarioEmpresa.setEmpresa(empresa);
                funcionarioEmpresa.setDataInicio(LocalDate.now().minusMonths(3));
                funcionarioEmpresaRepository.save(funcionarioEmpresa);


                funcionarios.add(funcionario);
            } else {
                funcionarios.add(usuarioRepository.findByEmail(data.email).get());
            }
        }
        return funcionarios;
    }
    
    private void criarProjetosTimesESprints(List<Empresa> empresas) {
        for (Empresa empresa : empresas) {
            // Verificar se já existem projetos para esta empresa
            List<Projeto> projetosExistentes = projetoRepository.findByEmpresaId(empresa.getId());
            if (!projetosExistentes.isEmpty()) {
                return; // Já existem projetos para esta empresa
            }

            // Criar projetos baseado no nome da empresa
            if (empresa.getNome().contains("TechSolutions")) {
                String[][] projetos = {
                    {"Sistema ERP Cloud", "Desenvolvimento de sistema ERP na nuvem"},
                    {"App Mobile Banking", "Aplicativo mobile para serviços bancários"},
                    {"Plataforma E-commerce", "Plataforma completa de e-commerce"},
                    {"Sistema de BI", "Sistema de Business Intelligence"},
                    {"API Gateway", "Gateway centralizado para APIs"}
                };

                for (String[] projetoData : projetos) {
                    // Verificar se projeto já existe para esta empresa
                    boolean projetoExiste = projetosExistentes.stream()
                        .anyMatch(p -> p.getNome().equals(projetoData[0]));

                    if (!projetoExiste) {
                        Projeto projeto = new Projeto();
                        projeto.setNome(projetoData[0]);
                        projeto.setDescricao(projetoData[1]);
                        projeto.setEmpresa(empresa);
                        projeto.setAtivo(true);
                        projetoRepository.save(projeto);
                    }
                }
            }
        }
    }
    

}
