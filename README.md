# Projeto-1
Repositório contendo os arquivos do exercício 1 da matéria "Programação Orientada a Objetos" do 3° semestre do curso de Ciências de Dados e Negócios.

## Membros do Grupo

- [Guilherme Orlandi de Oliveira](https://github.com/carrico05/)
- [Luiz Felipe Pimenta Berrettini](https://github.com/PimentaBrrt/)
- [Luiz Fernando Pazdziora Costa]()

## Diagrama de Classes do Banco

``` mermaid
classDiagram
    class Conta {
        - String id
        # double saldo
        - Cliente cliente
        + sacar(double valor)
        + depositar(double valor)
    }
    class Cliente {
        - String id
        - String nome
        - List<Conta> contas
    }
    class PessoaFisica {
        - String cpf
    }
    class PessoaJuridica {
        - String cnpj
    }
    class ContaCorrente {
        - double limite
        + sacar(double valor)
    }
    class ContaPoupanca {
        + sacar(double valor)
    }
    Conta *-- Cliente
    Conta <|-- ContaCorrente
    Conta <|-- ContaPoupanca
    Cliente <|-- PessoaFisica
    Cliente <|-- PessoaJuridica
```

## Help

### Funções que exigem a inserção de um ID
O projeto tem a interface gráfica do JOptionPane, e algumas opções de funcionalidades exigem que seja inserido o ID de um usuário ou conta. Esses IDs são salvos utilizando o UUID4, e a interface gráfica criada pelo JOptionPane não permite a seleção e a cópia dos IDs na impressão durante a execução do programa. Então, para copiá-los para a área de input, basta utilizar as funcionalidades de listagem de usuários ou contas (o que fará os IDs serem impressos no terminal do VScode, onde é possível copiá-los) e utilizar os atalhos ctrl+c para enviar o ID para a área de transferência e ctrl+v para colocá-lo no painel do programa.
