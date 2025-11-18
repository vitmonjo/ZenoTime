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
        // Criar administrador
        criarAdministrador();
        
        // Criar empresas e estrutura
        List<Empresa> empresas = criarEmpresas();
        
        // Criar funcionários
        List<Usuario> funcionarios = criarFuncionarios(empresas);
        
        // Criar projetos, times e sprints para cada empresa
        criarProjetosTimesESprints(empresas);
        
        // Criar registros de ponto (setembro, outubro, novembro 2025)
        criarRegistrosPonto(funcionarios);
        
        // Criar solicitações
        criarSolicitacoes(funcionarios);
        
        System.out.println("Dados de teste criados com sucesso!");
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
        String[][][] projetosPorEmpresa = {
            // TechSolutions
            {
                {"Sistema ERP Cloud", "Desenvolvimento de sistema ERP na nuvem"},
                {"App Mobile Banking", "Aplicativo mobile para serviços bancários"},
                {"Plataforma E-commerce", "Plataforma completa de e-commerce"},
                {"Sistema de BI", "Sistema de Business Intelligence"},
                {"API Gateway", "Gateway centralizado para APIs"}
            },
            // HealthCare Plus
            {
                {"Sistema de Prontuário", "Sistema eletrônico de prontuário médico"},
                {"App de Agendamento", "Aplicativo para agendamento de consultas"},
                {"Telemedicina", "Plataforma de telemedicina"},
                {"Gestão de Estoque", "Sistema de gestão de estoque hospitalar"},
                {"Portal do Paciente", "Portal online para pacientes"}
            },
            // EduTech Brasil
            {
                {"Plataforma LMS", "Learning Management System"},
                {"App Educacional", "Aplicativo educacional para crianças"},
                {"Sistema de Avaliação", "Sistema de avaliação online"},
                {"Biblioteca Digital", "Plataforma de biblioteca digital"},
                {"Gamificação", "Sistema de gamificação educacional"}
            },
            // GreenEnergy
            {
                {"Monitoramento Solar", "Sistema de monitoramento de energia solar"},
                {"Gestão de Usinas", "Sistema de gestão de usinas eólicas"},
                {"App Consumo", "App para monitorar consumo energético"},
                {"Dashboard Energia", "Dashboard de análise energética"},
                {"IoT Sensores", "Sistema IoT para sensores de energia"}
            },
            // FoodDelivery Express
            {
                {"App Delivery", "Aplicativo de delivery"},
                {"Sistema de Pedidos", "Sistema de gestão de pedidos"},
                {"Rastreamento", "Sistema de rastreamento de entregas"},
                {"App Restaurante", "App para restaurantes parceiros"},
                {"Analytics", "Sistema de analytics de vendas"}
            },
            // FinanceHub
            {
                {"Sistema Bancário", "Sistema core bancário"},
                {"App Investimentos", "App de investimentos"},
                {"Fraude Detection", "Sistema de detecção de fraude"},
                {"Open Banking", "Plataforma Open Banking"},
                {"Fintech API", "API para integração fintech"}
            },
            // RetailMax
            {
                {"E-commerce B2C", "Plataforma e-commerce B2C"},
                {"E-commerce B2B", "Plataforma e-commerce B2B"},
                {"App Cliente", "Aplicativo para clientes"},
                {"Gestão Estoque", "Sistema de gestão de estoque"},
                {"Marketplace", "Plataforma marketplace"}
            },
            // Construction Pro
            {
                {"Gestão Obras", "Sistema de gestão de obras"},
                {"App Campo", "Aplicativo para campo"},
                {"Orçamentos", "Sistema de orçamentos"},
                {"Controle Qualidade", "Sistema de controle de qualidade"},
                {"Dashboard Projetos", "Dashboard de projetos"}
            },
            // MediaGroup
            {
                {"CMS", "Content Management System"},
                {"Streaming", "Plataforma de streaming"},
                {"Redes Sociais", "Plataforma de redes sociais"},
                {"Analytics Mídia", "Sistema de analytics de mídia"},
                {"App Notícias", "Aplicativo de notícias"}
            },
            // Logistics Corp
            {
                {"Rastreamento", "Sistema de rastreamento de cargas"},
                {"Gestão Frotas", "Sistema de gestão de frotas"},
                {"App Motorista", "Aplicativo para motoristas"},
                {"Otimização Rotas", "Sistema de otimização de rotas"},
                {"Warehouse", "Sistema de gestão de armazéns"}
            }
        };
        
        for (int i = 0; i < empresas.size(); i++) {
            Empresa empresa = empresas.get(i);
            String[][] projetos = projetosPorEmpresa[i];
            
            for (String[] projetoData : projetos) {
                Projeto projeto = new Projeto();
                projeto.setNome(projetoData[0]);
                projeto.setDescricao(projetoData[1]);
                projeto.setEmpresa(empresa);
                projeto.setAtivo(true);
                projeto = projetoRepository.save(projeto);
                
                // Criar 2 times para cada projeto
                for (int t = 1; t <= 2; t++) {
                    Time time = new Time();
                    time.setNome("Time " + t + " - " + projeto.getNome());
                    time.setDescricao("Time de desenvolvimento do projeto " + projeto.getNome());
                    time.setProjeto(projeto);
                    time.setAtivo(true);
                    time = timeRepository.save(time);
                    
                    // Criar 2 sprints para cada time
                    for (int s = 1; s <= 2; s++) {
                        Sprint sprint = new Sprint();
                        sprint.setNome("Sprint " + s + " - " + time.getNome());
                        LocalDate inicio = LocalDate.of(2025, 9, 1).plusWeeks((s - 1) * 2);
                        sprint.setDataInicio(inicio);
                        sprint.setDataFim(inicio.plusWeeks(2).minusDays(1));
                        sprint.setTime(time);
                        sprint.setObjetivo("Objetivos da sprint " + s);
                        sprint.setStatus(s == 1 ? Sprint.StatusSprint.EM_ANDAMENTO : Sprint.StatusSprint.PLANEJADA);
                        sprintRepository.save(sprint);
                    }
                }
            }
        }
    }
    
    private void criarRegistrosPonto(List<Usuario> funcionarios) {
        LocalDate inicio = LocalDate.of(2025, 9, 1);
        LocalDate fim = LocalDate.of(2025, 11, 30);
        
        List<Projeto> projetos = projetoRepository.findAll();
        
        for (Usuario funcionario : funcionarios) {
            LocalDate data = inicio;
            while (!data.isAfter(fim)) {
                // Pular finais de semana
                if (data.getDayOfWeek().getValue() < 6) {
                    // Criar registro de ponto
                    LocalDateTime entrada = LocalDateTime.of(data, 
                        java.time.LocalTime.of(8 + random.nextInt(2), random.nextInt(30)));
                    LocalDateTime saida = entrada.plusHours(8).plusMinutes(random.nextInt(60));
                    
                    RegistroPonto registro = new RegistroPonto();
                    registro.setFuncionario(funcionario);
                    registro.setDataHoraEntrada(entrada);
                    registro.setDataHoraSaida(saida);
                    
                    // Calcular horas trabalhadas
                    long minutos = java.time.Duration.between(entrada, saida).toMinutes();
                    registro.setHorasTrabalhadas(minutos / 60.0 + (minutos % 60) / 60.0);
                    
                    // Associar a um projeto aleatório da empresa do funcionário
                    if (!projetos.isEmpty() && random.nextDouble() > 0.3) {
                        registro.setProjeto(projetos.get(random.nextInt(projetos.size())));
                    }
                    
                    registro.setTipo(RegistroPonto.TipoRegistro.NORMAL);
                    registroPontoRepository.save(registro);
                }
                data = data.plusDays(1);
            }
        }
    }
    
    private void criarSolicitacoes(List<Usuario> funcionarios) {
        // Criar algumas solicitações de férias
        for (int i = 0; i < 5; i++) {
            Usuario funcionario = funcionarios.get(random.nextInt(funcionarios.size()));
            Solicitacao solicitacao = new Solicitacao();
            solicitacao.setFuncionario(funcionario);
            solicitacao.setTipo(Solicitacao.TipoSolicitacao.FERIAS);
            
            LocalDate inicio = LocalDate.of(2025, 10, 1).plusDays(random.nextInt(30));
            solicitacao.setDataInicio(inicio);
            solicitacao.setDataFim(inicio.plusDays(5 + random.nextInt(10)));
            solicitacao.setJustificativa("Solicitação de férias");
            solicitacao.setDataSolicitacao(LocalDateTime.now().minusDays(random.nextInt(30)));
            solicitacao.setStatus(random.nextBoolean() ? 
                Solicitacao.StatusSolicitacao.APROVADA : Solicitacao.StatusSolicitacao.PENDENTE);
            solicitacaoRepository.save(solicitacao);
        }
        
        // Criar algumas solicitações de atestado
        for (int i = 0; i < 8; i++) {
            Usuario funcionario = funcionarios.get(random.nextInt(funcionarios.size()));
            Solicitacao solicitacao = new Solicitacao();
            solicitacao.setFuncionario(funcionario);
            solicitacao.setTipo(Solicitacao.TipoSolicitacao.ATESTADO);
            
            LocalDate inicio = LocalDate.of(2025, 9, 1).plusDays(random.nextInt(90));
            solicitacao.setDataInicio(inicio);
            solicitacao.setDataFim(inicio.plusDays(1 + random.nextInt(3)));
            solicitacao.setJustificativa("Atestado médico");
            solicitacao.setDataSolicitacao(LocalDateTime.now().minusDays(random.nextInt(30)));
            solicitacao.setStatus(random.nextBoolean() ? 
                Solicitacao.StatusSolicitacao.APROVADA : Solicitacao.StatusSolicitacao.PENDENTE);
            solicitacaoRepository.save(solicitacao);
        }
    }
}
