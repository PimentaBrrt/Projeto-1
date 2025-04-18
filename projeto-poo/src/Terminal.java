import java.util.List;
import static javax.swing.JOptionPane.*;
import static java.lang.Double.parseDouble;

public class Terminal {

    private Banco banco;

    public void run() {
        try {
            this.banco = new Banco("Itapecuru Mirim International Deluxe Bank");
            showMessageDialog(null, banco.getName());
            Cliente atualCliente = null;
            Conta atualConta = null;
            while (true) {

                try {

                    String opcao = "";
                    String prompt = "";
                    prompt +=
                        atualCliente == null
                        ? ""
                        : atualCliente.getName();
                    prompt +=
                        atualConta == null
                        ? ""
                        : String.format(" | %s [%s]", atualConta.getId(), atualConta.getSaldo());

                    opcao = showInputDialog(prompt + painel());

                    if (opcao == null) {

                        return;

                    }

                    if (opcao.equals("1")) {

                        atualCliente = createCustomer();
                        banco.addCliente(atualCliente);
                        atualConta = null;

                    } 
                    else if (opcao.equals("2")) {

                        listCustomers();

                    } 
                    else if (opcao.equals("3")) {

                        String id = showInputDialog("Código do cliente");
                        atualCliente = banco.getCliente(id);
                        atualConta = null;

                    } 
                    else if (opcao.equals("4")) {

                        if (atualCliente == null) {
                            throw new BancoException("D05", "Cliente não selecionado");
                        }
                        Conta conta = createAccount(atualCliente);
                        atualCliente.addConta(conta);
                        atualConta = conta;
                        // adicionar conta no banco
                        banco.addConta(atualConta);

                    } 
                    else if (opcao.equals("5")) {

                        if (atualCliente == null) {
                            throw new BancoException("D05", "Cliente não selecionado");
                        }
                        listAccounts(atualCliente.getContas());

                    } 
                    else if (opcao.equals("6")) {

                        if (atualCliente == null) {
                            throw new BancoException("D05", "Cliente não selecionado");
                        }
                        String id = showInputDialog("Código da conta: ");
                        atualConta = atualCliente.getConta(id);

                    } 
                    else if (opcao.equals("7")) {

                        // depositar
                        if (atualConta == null) {
                            throw new BancoException("D06", "Conta não selecionada");
                        }
                        double valor = inputValue();
                        atualConta.depositar(valor);

                    } 
                    else if (opcao.equals("8")) {

                        // sacar
                        if (atualConta == null) {
                            throw new BancoException("D06", "Conta não selecionada");
                        }
                        double valor = inputValue();
                        atualConta.sacar(valor);

                    } 
                    else if (opcao.equals("9")) {

                        listAccounts(banco.getContas());

                    } 
                    else if (opcao.equals("10")) {

                        int ctz = showConfirmDialog(null, "A atual conta será removida. Tem certeza?");
                        if (ctz == 0) {
                            atualCliente.removeConta(atualConta.getId());
                            banco.removeConta(atualConta.getId());
                            atualConta = null;
                        }

                    } 
                    else if (opcao.equals("11")) {

                        int ctz = showConfirmDialog(null, "O atual cliente será removido. Tem certeza?");
                        if (ctz == 0) {
                            for (Conta c : atualCliente.getContas()) {
                                atualCliente.removeConta(c.getId());
                                banco.removeConta(c.getId());
                            }
                            banco.removeCliente(atualCliente.getId());
                            atualCliente = null;
                            atualConta = null;
                        }

                    } 
                    else if (opcao.equals("12")) {

                        return;

                    } 
                    else if (opcao.equals("r")) {

                        banco.getContas().forEach(c -> {
                            if (c instanceof Rendimento) {
                                ((Rendimento) c).render();
                            }
                        });

                    } 
                    else {

                        throw new UnsupportedOperationException("Opção inválida.");

                    }
                } catch (UnsupportedOperationException e) {
                    showMessageDialog(null, e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            showMessageDialog(null, "Programa finalizado.");
        }
    }

    private void listCustomers() {

        String clientes = "";
        for (Cliente c: banco.getClientes()) {
            clientes += "\n" + c;
        }
        showMessageDialog(null, clientes);
        System.out.println(clientes);

    }

    private void listAccounts(List<Conta> contas) {

        String contas_imp = "";
        for (Conta c: contas) {
            contas_imp += "\n" + (
                c instanceof ContaCorrente ? "CC" :
                c instanceof ContaPoupanca ? "CP" :
                "CI") + " " + c;
        }
        showMessageDialog(null, contas_imp);
        System.out.println(contas_imp);

    }

    private Cliente createCustomer() {

        Cliente cliente;
        
        String name = showInputDialog("Insira o nome do cliente");
        String tipo = showInputDialog("Tipo Fisica|Juridica [f|j]: ");
        if (tipo.trim().toLowerCase().equals("f")) {
            String cpf = null;
            while (true) {
                cpf = showInputDialog("CPF");
                if (Util.isCpf(cpf)) break;
                showMessageDialog(null, "CPF inválido.");
            }
            cliente = new PessoaFisica(name, cpf);
        } else {
            String cnpj = showInputDialog("CNPJ");
            cliente = new PessoaJuridica(name, cnpj);
        }

        return cliente;
    }
    
    private String painel() {
        String painel = "";
        painel += "\n  1. Criar cliente";
        painel += "\n  2. Listar clientes";
        painel += "\n  3. Selectionar cliente";
        painel += "\n  4. Criar conta";
        painel += "\n  5. Listar contas do cliente";
        painel += "\n  6. Selecionar conta";
        painel += "\n  7. Depositar";
        painel += "\n  8. Sacar";
        painel += "\n  9. Lista todas as contas do banco";
        painel += "\n 10. Excluir conta";
        painel += "\n 11. Excluir cliente";
        painel += "\n 12. Finalizar";
        painel += "\n  r. render";
        return painel;
    }

    private Conta createAccount(Cliente cliente) {
        if (cliente == null) {
            throw new RuntimeException("Cliente nao definido");
        }
        Conta conta;
        String tipo = showInputDialog("Tipo [(P)oupanca|(C)orrente|(I)nvestimento]: ").toLowerCase();

        if (tipo.equals("p")) {
            conta = new ContaPoupanca(cliente);
        } else if (tipo.equals("c")) {
            conta = new ContaCorrente(cliente);
        } else {
            conta = new ContaInvestimento(cliente);
        }

        return conta;
    }

    private double inputValue() {
        while (true) {
            try {
                String s = showInputDialog("Valor");
                return parseDouble(s);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

}
