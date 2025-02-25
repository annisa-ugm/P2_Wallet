import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    public void testSetOwner() {
        Wallet wallet = new Wallet();
        wallet.setOwner("Omar");
        assertEquals("Omar", wallet.getOwner(), "Owner harus sesuai dengan yang di-set");
    }

    @Test
    public void testChangeOwner() {
        Wallet wallet = new Wallet();
        wallet.setOwner("Lucas");
        assertEquals("Lucas", wallet.getOwner(), "Owner harus sesuai dengan yang di-set");

        // Perubahan owner
        wallet.setOwner("Yao");
        assertEquals("Yao", wallet.getOwner(), "Owner harus bisa diubah ke nama lain");
    }

    @Test
    public void testSetEmptyOwner() {
        Wallet wallet = new Wallet();
        wallet.setOwner("");
        assertNotEquals("", wallet.getOwner(), "Owner tidak  bisa di-set sebagai string kosong");
    }

    @Test
    public void testSetNullOwner() {
        Wallet wallet = new Wallet();
        wallet.setOwner(null);
        assertNotEquals("", wallet.getOwner(), "Owner tidak  bisa di-set sebagai null");
    }

    @Test
    public void testSetOwnerWithSpecialCharacters() {
        Wallet wallet = new Wallet();
        wallet.setOwner("Jun3 j0l!");
        assertEquals("Jun3 j0l!", wallet.getOwner(), "Owner harus bisa menerima karakter spesial");
    }

    @Test
    public void testSetLongOwnerName() {
        Wallet wallet = new Wallet();
        String longName = "B".repeat(1000);
        wallet.setOwner(longName);
        assertEquals(longName, wallet.getOwner(), "Owner harus bisa menangani nama panjang");
    }

    @Test
    public void testAddAndRemoveCard() {
        Wallet wallet = new Wallet();
        wallet.addCard("NPWP");
        wallet.addCard("SIM");

        List<String> cards = wallet.getCards();
        assertTrue(cards.contains("NPWP"));
        assertTrue(cards.contains("SIM"));

        boolean isRemoved = wallet.removeCard("NPWP");
        assertTrue(isRemoved);
        assertFalse(wallet.getCards().contains("NPWP"));
    }

    @Test
    public void testAddDuplicateCard() {
        Wallet wallet = new Wallet();
        wallet.addCard("KTP");
        wallet.addCard("KTP"); // KTP ditambahkan lagi

        List<String> cards = wallet.getCards();
        assertEquals(2, cards.size(), "Kartu duplikat harus bisa ditambahkan ke dalam dompet");
    }

    @Test
    public void testRemoveNonExistentCard() {
        Wallet wallet = new Wallet();
        wallet.addCard("ATM");

        boolean isRemoved = wallet.removeCard("KTP"); // KTP belum ditambahkan
        assertFalse(isRemoved, "Harus gagal menghapus kartu yang tidak ada di dompet");
    }

    @Test
    public void testAddAndRemoveEmptyCard() {
        Wallet wallet = new Wallet();
        wallet.addCard("");

        assertFalse(wallet.getCards().contains(""), "Kartu dengan string kosong tidak bisa ditambahkan");

        boolean isRemoved = wallet.removeCard("");
        assertFalse(isRemoved, "Kartu dengan string kosong tidak ada dapat dihapus karena tidak mungkin ada di dalam list");
        assertFalse(wallet.getCards().contains(""), "Kartu kosong tidak ada sejak awal");
    }

    @Test
    public void testRemoveAllCards() {
        Wallet wallet = new Wallet();
        wallet.addCard("ATM BCA");
        wallet.addCard("Kartu Kredit");
        wallet.addCard("SIM");

        wallet.removeCard("ATM BCA");
        wallet.removeCard("Kartu Kredit");
        wallet.removeCard("SIM");

        assertTrue(wallet.getCards().isEmpty(), "Semua kartu harus hilang setelah dihapus");
    }


    @Test
    public void testAddMoney() {
        Wallet wallet = new Wallet();
        wallet.addMoney(5000);
        wallet.addMoney(500);
        wallet.addMoney(2000);
        wallet.addMoney(100);

        assertEquals(7600, wallet.getTotalMoney(), "Total uang harus sesuai dengan yang ditambahkan");
    }

    @Test
    public void testAddMoneyCategorization() {
        Wallet wallet = new Wallet();
        wallet.addMoney(4000); // Lembaran
        wallet.addMoney(500);  // Koin
        wallet.addMoney(100);  // Koin

        assertEquals(1, wallet.getUangLembaran().size(), "Harus ada 1 uang lembaran");
        assertEquals(2, wallet.getUangKoin().size(), "Harus ada 2 uang koin");
    }

    @Test
    public void testAddZeroMoney() {
        Wallet wallet = new Wallet();
        wallet.addMoney(0);

        assertEquals(0, wallet.getTotalMoney(), "Tidak boleh menambahkan uang 0");
    }

    @Test
    public void testAddNegativeMoney() {
        Wallet wallet = new Wallet();
        wallet.addMoney(-1000);

        assertEquals(0, wallet.getTotalMoney(), "Uang negatif tidak boleh ditambahkan");
    }

    @Test
    public void testWithdrawMoney() {
        Wallet wallet = new Wallet();
        wallet.addMoney(5000);
        wallet.addMoney(2000);
        wallet.addMoney(1000);
        wallet.addMoney(500);
        wallet.addMoney(100);

        boolean isWithdrawn = wallet.withdrawMoney(600);
        assertTrue(isWithdrawn);
        assertEquals(8000, wallet.getTotalMoney(), "Total uang harus berkurang sesuai dengan jumlah yang ditarik");

    }

    @Test
    public void testWithdrawUnmatchableAmount() {
        Wallet wallet = new Wallet();
        wallet.addMoney(5000);
        wallet.addMoney(2000);

        boolean isWithdrawn = wallet.withdrawMoney(1500);
        assertFalse(isWithdrawn, "Harus gagal menarik uang karena tidak ada pecahan yang cocok");
        assertEquals(7000, wallet.getTotalMoney(), "Total uang tidak boleh berubah jika gagal menarik");
    }

    @Test
    public void testWithdrawAllMoney() {
        Wallet wallet = new Wallet();
        wallet.addMoney(5000);
        wallet.addMoney(2000);
        wallet.addMoney(1000);

        boolean isWithdrawn = wallet.withdrawMoney(8000);
        assertTrue(isWithdrawn, "Harus bisa menarik seluruh saldo");
        assertEquals(0, wallet.getTotalMoney(), "Dompet harus kosong setelah menarik semua uang");
    }

    @Test
    public void testWithdrawMoreThanAvailable() {
        Wallet wallet = new Wallet();
        wallet.addMoney(10000);

        boolean isWithdrawn = wallet.withdrawMoney(15000);
        assertFalse(isWithdrawn, "Harus gagal menarik lebih dari saldo");
        assertEquals(10000, wallet.getTotalMoney(), "Saldo tidak boleh berubah jika gagal menarik");
    }

    @Test
    public void testWithdrawZeroOrNegative() {
        Wallet wallet = new Wallet();
        wallet.addMoney(5000);

        assertFalse(wallet.withdrawMoney(0), "Harus gagal menarik uang 0");
        assertFalse(wallet.withdrawMoney(-1000), "Harus gagal menarik jumlah negatif");
        assertEquals(5000, wallet.getTotalMoney(), "Saldo tidak boleh berubah jika gagal menarik");
    }

}

