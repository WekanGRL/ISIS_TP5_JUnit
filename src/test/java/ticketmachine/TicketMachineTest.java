package ticketmachine;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

class TicketMachineTest {
	private static final int PRICE = 50; // Une constante

	private TicketMachine machine; // l'objet à tester

	@BeforeEach
	public void setUp() {
		machine = new TicketMachine(PRICE); // On initialise l'objet à tester
	}

	@Test
	// On vérifie que le prix affiché correspond au paramètre passé lors de
	// l'initialisation
	// S1 : le prix affiché correspond à l’initialisation.
	void priceIsCorrectlyInitialized() {
		// Paramètres : valeur attendue, valeur effective, message si erreur
		assertEquals(PRICE, machine.getPrice(), "Initialisation incorrecte du prix");
	}

	@Test
	// S2 : la balance change quand on insère de l’argent
	void insertingMoneyChangesBalance() {
		machine.insertMoney(10);
		machine.insertMoney(20);
		// Les montants ont été correctement additionnés
		assertEquals(10 + 20, machine.getBalance(), "La balance n'est pas correctement mise à jour");
	}

	@Test
	// S3 : on n’imprime pas le ticket si le montant inséré est insuffisant
	void ticketNotImpressedDueToInsufficientAmountInserted() {
		machine.insertMoney(40);
		// Le montant inséré est insuffisant
		assertFalse(machine.printTicket(), "Le ticket ne devrait pas être imprimé");
	}

	@Test
	// S4 : on imprime le ticket si le montant inséré est suffisant
	void ticketImpressedDueToSufficientAmountInserted() {
		machine.insertMoney(75);
		// Le montant inséré est suffisant
		assertTrue(machine.printTicket(), "Le ticket devrait être imprimé");
	}

	@Test
	// S5 : Quand on imprime un ticket la balance est décrémentée du prix du ticket
	void decrementBalanceOnTicketImpression() {
		int amountInserted = 75;
		machine.insertMoney(amountInserted);
		// Le montant inséré est suffisant
		machine.printTicket();
		// La balance est décrémentée
		assertEquals(machine.getBalance(),amountInserted - PRICE, "La balance devrait être décrémentée");
	}

	@Test
	// S6 : le montant collecté est mis à jour quand on imprime un ticket (pas avant)
	void updateTotalOnTicketImpression() {
		int amountInserted = 150;
		int ticketsInserted = 2;

		// Vérifier qu'on a inséré assez de monnaie pour imprimer notre nombre de tickets
		assertTrue(amountInserted >= PRICE * ticketsInserted, "Le montant inséré sera insuffisant");
		machine.insertMoney(amountInserted);

		// Le total est nul
		assertEquals(machine.getTotal(), 0, "Le total devrait être nul avant l'impression");

		for(int i = 0 ; i < ticketsInserted ; i++)
		{
			// Le montant inséré est suffisant
			machine.printTicket();
		}

		// Le total est mis à jour
		assertEquals(machine.getTotal(), PRICE * 2, "Le total devrait être égal au prix de 2 tickets");
	}

	@Test
	// S7 : refund() rend correctement la monnaie
	void refundGivesBackCorrectAmount() {
		int amountInserted = 75;
		machine.insertMoney(amountInserted);
		machine.printTicket();

		// Le remboursement renvoie le bon montant
		assertEquals(machine.refund(), amountInserted - PRICE, "Le remboursement devrait renvoyer le bon montant");
	}

	@Test
	// S8 : refund() remet la balance à zéro
	void refundResetsBalance() {
		int amountInserted = 75;
		machine.insertMoney(amountInserted);
		machine.printTicket();

		// Remboursement
		machine.refund();

		// Le remboursement réinitialise la balance
		assertEquals(machine.getBalance(), 0, "Le remboursement devrait réinitialiser la balance");
	}

	@Test
	// S9 : on ne peut pas insérer un montant négatif
	void onlyInsertsPositiveAmounts() {
		int amountInserted = -75;
		assertThrows(IllegalArgumentException.class, () -> { machine.insertMoney(amountInserted); }, "Le programme ne devrait pas permettre d'insérer un montant négatif");

	}
	@Test
	// S10 : on ne peut pas créer de machine qui délivre des tickets dont le prix est négatif
	void onlyCreatesMachineWithPositivePrices() {
		int price = -75;
		assertThrows(IllegalArgumentException.class, () -> { machine = new TicketMachine(price); }, "Le programme ne devrait pas permettre de créer une machine avec un prix négatif");

	}

}
