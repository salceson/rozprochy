package pl.edu.agh.ki.dsrg.sr.bankmanagement.util;

/**
 * @author Michał Ciołczyk
 */
public class CurrencyPackageConverter {
    public static FinancialNews.Currency convertBankCurrencyToFinancialCurrency(Bank.Currency currency) {
        return FinancialNews.Currency.valueOf(currency.toString());
    }
}
