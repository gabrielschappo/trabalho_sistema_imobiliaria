package br.edu.univille.ui;

import br.edu.univille.dao.Cliente;
import br.edu.univille.dao.ClienteDAO;
import br.edu.univille.dao.ContratoAluguel;
import br.edu.univille.dao.ContratoAluguelDAO;
import br.edu.univille.dao.Imovel;
import br.edu.univille.dao.ImovelDAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class SistemaImobiliaria {

    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final ImovelDAO imovelDAO = new ImovelDAO();
    private static final ContratoAluguelDAO contratoDAO = new ContratoAluguelDAO();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            exibirMenu();
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                rotearEscolha(opcao, scanner);
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, digite um número válido.");
                opcao = -1;
            }
        }
        scanner.close();
        System.out.println("Sistema encerrado.");
    }

    private static void exibirMenu() {
        System.out.println("\n===== Sistema de Gestão Imobiliária =====");
        System.out.println("1. Cadastrar Cliente");
        System.out.println("2. Cadastrar Imóvel");
        System.out.println("3. Cadastrar Contrato de Aluguel");
        System.out.println("-----------------------------------------");
        System.out.println("4. Relatório: Listar Imóveis Disponíveis");
        System.out.println("5. Relatório: Listar Contratos Ativos");
        System.out.println("6. Relatório: Clientes com Mais Contratos");
        System.out.println("7. Relatório: Contratos Expirando (30 dias)");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void rotearEscolha(int opcao, Scanner scanner) {
        switch (opcao) {
            case 1 -> cadastrarCliente(scanner);
            case 2 -> cadastrarImovel(scanner);
            case 3 -> cadastrarContrato(scanner);
            case 4 -> relatorioImoveisDisponiveis();
            case 5 -> relatorioContratosAtivos();
            case 6 -> relatorioClientesComMaisContratos();
            case 7 -> relatorioContratosExpirando();
            case 0 -> {}
            default -> System.out.println("Opção inválida!");
        }
        if (opcao > 0 && opcao < 8) {
            pressioneEnterParaContinuar(scanner);
        }
    }

    private static void cadastrarCliente(Scanner scanner) {
        System.out.println("\n--- Cadastro de Cliente ---");
        Cliente cliente = new Cliente();
        System.out.print("Nome: ");
        cliente.setNome(scanner.nextLine());
        System.out.print("CPF: ");
        cliente.setCpf(scanner.nextLine());
        System.out.print("Telefone: ");
        cliente.setTelefone(scanner.nextLine());
        System.out.print("Email: ");
        cliente.setEmail(scanner.nextLine());

        clienteDAO.inserir(cliente);
        System.out.println("Cliente cadastrado com sucesso! ID: " + cliente.getId());
    }

    private static void cadastrarImovel(Scanner scanner) {
        System.out.println("\n--- Cadastro de Imóvel ---");
        Imovel imovel = new Imovel();
        try {
            System.out.print("Endereço completo: ");
            imovel.setEndereco(scanner.nextLine());
            System.out.print("Tipo (Casa, Apartamento, etc.): ");
            imovel.setTipo(scanner.nextLine());
            System.out.print("Número de quartos: ");
            imovel.setQuartos(Integer.parseInt(scanner.nextLine()));
            System.out.print("Número de banheiros: ");
            imovel.setBanheiros(Integer.parseInt(scanner.nextLine()));
            System.out.print("Valor do aluguel (ex: 1500.00): ");
            imovel.setValorAluguelBase(new BigDecimal(scanner.nextLine()));
            imovel.setDisponivel(true); // Novo imóvel é sempre disponível

            imovelDAO.inserir(imovel);
            System.out.println("Imóvel cadastrado com sucesso! ID: " + imovel.getId());
        } catch (NumberFormatException e) {
            System.out.println("Erro de formato: Verifique os valores numéricos inseridos.");
        }
    }

    private static void cadastrarContrato(Scanner scanner) {
        System.out.println("\n--- Cadastro de Contrato de Aluguel ---");

        // Listar e selecionar cliente
        List<Cliente> clientes = clienteDAO.obterTodos();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado. Cadastre um cliente primeiro.");
            return;
        }
        System.out.println("Selecione um cliente:");
        clientes.forEach(c -> System.out.printf("ID: %d - Nome: %s\n", c.getId(), c.getNome()));
        System.out.print("Digite o ID do cliente: ");
        long idCliente;
        try {
            idCliente = Long.parseLong(scanner.nextLine());
            if (clienteDAO.obterPeloId(idCliente).isEmpty()) {
                System.out.println("ID de cliente inválido.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        List<Imovel> imoveis = imovelDAO.obterDisponiveis();
        if (imoveis.isEmpty()) {
            System.out.println("Nenhum imóvel disponível para aluguel.");
            return;
        }
        System.out.println("\nSelecione um imóvel disponível:");
        imoveis.forEach(i -> System.out.printf("ID: %d - Endereço: %s\n", i.getId(), i.getEndereco()));
        System.out.print("Digite o ID do imóvel: ");
        long idImovel;
        try {
            idImovel = Long.parseLong(scanner.nextLine());
            if (imovelDAO.obterPeloId(idImovel).filter(Imovel::isDisponivel).isEmpty()) {
                System.out.println("ID de imóvel inválido ou imóvel não está disponível.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        ContratoAluguel contrato = new ContratoAluguel();
        contrato.setIdCliente(idCliente);
        contrato.setIdImovel(idImovel);
        contrato.setAtivo(true);
        try {
            System.out.print("Data de início (AAAA-MM-DD): ");
            contrato.setDataInicio(LocalDate.parse(scanner.nextLine()));
            System.out.print("Data de fim (AAAA-MM-DD): ");
            contrato.setDataFim(LocalDate.parse(scanner.nextLine()));
            System.out.print("Valor do aluguel pactuado: ");
            contrato.setValorAluguelPactuado(new BigDecimal(scanner.nextLine()));

            contratoDAO.inserir(contrato);
            System.out.println("Contrato cadastrado com sucesso!");

        } catch (DateTimeParseException e) {
            System.out.println("Erro: Formato de data inválido. Use AAAA-MM-DD.");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Formato de valor inválido.");
        }
    }

    private static void relatorioImoveisDisponiveis() {
        System.out.println("\n--- Relatório: Imóveis Disponíveis ---");
        List<Imovel> imoveis = imovelDAO.obterDisponiveis();
        if (imoveis.isEmpty()) {
            System.out.println("Nenhum imóvel disponível no momento.");
            return;
        }
        imoveis.forEach(i -> System.out.printf(
                "ID: %d | Tipo: %s | Endereço: %s | Quartos: %d | Aluguel: R$ %.2f\n",
                i.getId(), i.getTipo(), i.getEndereco(), i.getQuartos(), i.getValorAluguelBase()
        ));
    }

    private static void relatorioContratosAtivos() {
        System.out.println("\n--- Relatório: Contratos Ativos ---");
        List<ContratoAluguel> contratos = contratoDAO.obterAtivos();
        if (contratos.isEmpty()) {
            System.out.println("Nenhum contrato ativo no momento.");
            return;
        }
        contratos.forEach(c -> System.out.printf(
                "ID Contrato: %d | Início: %s | Fim: %s | Inquilino: %s | Imóvel: %s\n",
                c.getId(), c.getDataInicio(), c.getDataFim(), c.getCliente().getNome(), c.getImovel().getEndereco()
        ));
    }

    private static void relatorioClientesComMaisContratos() {
        System.out.println("\n--- Relatório: Clientes com Mais Contratos (Ordem Decrescente) ---");
        List<Cliente> clientes = clienteDAO.obterClientesComMaisContratos();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente com contrato encontrado.");
            return;
        }
        clientes.forEach(c -> System.out.printf(
                "ID: %d | Nome: %s | CPF: %s\n",
                c.getId(), c.getNome(), c.getCpf()
        ));
    }

    private static void relatorioContratosExpirando() {
        System.out.println("\n--- Relatório: Contratos Expirando nos Próximos 30 Dias ---");
        List<ContratoAluguel> contratos = contratoDAO.obterExpirandoEm30Dias();
        if (contratos.isEmpty()) {
            System.out.println("Nenhum contrato expirando no período.");
            return;
        }
        contratos.forEach(c -> System.out.printf(
                "Data Fim: %s | ID Contrato: %d | Inquilino: %s | Imóvel: %s\n",
                c.getDataFim(), c.getId(), c.getCliente().getNome(), c.getImovel().getEndereco()
        ));
    }

    private static void pressioneEnterParaContinuar(Scanner scanner) {
        System.out.print("\nPressione ENTER para voltar ao menu...");
        scanner.nextLine();
    }
}