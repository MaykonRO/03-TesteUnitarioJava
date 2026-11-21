package contabancaria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Testes unitários para a classe Conta.
 *
 * PARTE 1 — Testes de exemplo (Construtor) já estão prontos.
 * Observe o padrão AAA e o uso de @Test e @ParameterizedTest.
 *
 * PARTE 2 — Você deve escrever os testes para os demais métodos
 * seguindo rigorosamente o ciclo TDD: Red → Green → Refactor.
 *
 * Para cada método da classe Conta, crie testes que cubram:
 * ✅ O cenário de sucesso (caminho feliz)
 * ❌ Cada regra de validação (cenários de exceção)
 * 🔄 Casos de borda (valores limites)
 */
class ContaTest {

    @Test
    void construtor_DadosValidos_CriaContaCorretamente() {
        var conta = new Conta("Maria", 100);
        assertEquals("Maria", conta.getTitular());
        assertEquals(100, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_SemSaldoInicial_CriaContaComSaldoZero() {
        var conta = new Conta("João");
        assertEquals("João", conta.getTitular());
        assertEquals(0, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_TitularNulo_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta(null));
    }

    @Test
    void construtor_TitularVazio_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta(""));
    }

    @Test
    void construtor_SaldoNegativo_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta("Maria", -50));
    }

    @ParameterizedTest
    @CsvSource({
            "Ana,    0",
            "Carlos, 1000",
            "Beatriz, 0.01"
    })
    void construtor_VariosValoresValidos_CriaContaCorretamente(String titular, double saldo) {
        var conta = new Conta(titular, saldo);
        assertEquals(titular, conta.getTitular());
        assertEquals(saldo, conta.getSaldo(), 0.001);
        assertTrue(conta.isAtiva());
    }

    @Test
    void depositar_ValorValido_AtualizaSaldo() {
        var conta = new Conta("Maria", 100);
        conta.depositar(50);
        assertEquals(150, conta.getSaldo());
    }

    @Test
    void depositar_ValorZeroOuNegativo_LancaIllegalArgumentException() {
        var conta = new Conta("Maria", 100);
        assertThrows(IllegalArgumentException.class, () -> conta.depositar(0));
        assertThrows(IllegalArgumentException.class, () -> conta.depositar(-10));
    }

    @Test
    void sacar_ValorValido_AtualizaSaldo() {
        var conta = new Conta("Maria", 100);
        conta.sacar(40);
        assertEquals(60, conta.getSaldo());
    }

    @Test
    void sacar_ValorZeroOuNegativo_LancaIllegalArgumentException() {
        var conta = new Conta("Maria", 100);
        assertThrows(IllegalArgumentException.class, () -> conta.sacar(0));
        assertThrows(IllegalArgumentException.class, () -> conta.sacar(-10));
    }

    @Test
    void sacar_SaldoInsuficiente_LancaIllegalStateException() {
        var conta = new Conta("Maria", 100);
        assertThrows(IllegalArgumentException.class, () -> conta.sacar(101));
    }

    @Test
    void encerrar_SaldoZero_DesativaConta() {
        var conta = new Conta("Maria", 0);

        conta.encerrar();

        assertEquals(false, conta.isAtiva());
    }

    @Test
    void encerrar_SaldoPositivo_IllegalStateException() {
        var conta = new Conta("Maria", 1);

        assertThrows(IllegalStateException.class, () -> conta.encerrar());
    }

    @Test
    void encerrar_encerrarContaInativa_IllegalStateException() {
        var conta = new Conta("Maria", 0);

        conta.encerrar();
    
        assertThrows(IllegalStateException.class, () -> conta.encerrar());
    }

    @Test
    void encerrar_transferirEmContaInativa_IllegalStateException() {
        var conta = new Conta("Maria", 0);

        conta.encerrar();
    
        assertThrows(IllegalStateException.class, () -> conta.depositar(15));
        assertThrows(IllegalStateException.class, () -> conta.sacar(10));
    }

    @Test
    void transferir_enviaValorPoistivoParaOutraConta_valoresAtualizadosNasDuasContas() {
        var contaOrigem = new Conta("Maria", 200);
        var contaDestino = new Conta("João", 100);

        contaOrigem.transferir(contaDestino, 50);

        assertEquals(150, contaOrigem.getSaldo());
        assertEquals(150, contaDestino.getSaldo());
    }

    @Test
    void transferir_enviaValorNegativoOuZeradoParaOutraConta_IllegalArgumentException() {
        var contaOrigem = new Conta("Maria", 200);
        var contaDestino = new Conta("João", 100);

        assertThrows(IllegalArgumentException.class, () -> contaOrigem.transferir(contaDestino, -50));
        assertThrows(IllegalArgumentException.class, () -> contaOrigem.transferir(contaDestino, 0));
        
    }
    
    @Test
    void transferir_transferirValorAcimaDoSaldo_IllegalStateException() {
        var contaOrigem = new Conta("Maria", 200);
        var contaDestino = new Conta("João", 100);

        assertThrows(IllegalStateException.class, () -> contaOrigem.transferir(contaDestino, 201));
        assertEquals(200, contaOrigem.getSaldo());
        assertEquals(100, contaDestino.getSaldo());
        
    }

    @Test
    void transferir_transferirValorParaContaEncerrada_IllegalStateException() {
        var contaOrigem = new Conta("Maria", 0);
        var contaDestino = new Conta("João", 100);

        contaOrigem.encerrar();
        assertThrows(IllegalStateException.class, () -> contaOrigem.transferir(contaDestino, 100));
        assertEquals(0, contaOrigem.getSaldo());
        assertEquals(100, contaDestino.getSaldo());
        
    }
    

}
