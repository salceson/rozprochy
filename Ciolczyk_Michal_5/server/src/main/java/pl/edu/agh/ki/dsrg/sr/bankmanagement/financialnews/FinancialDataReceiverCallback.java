package pl.edu.agh.ki.dsrg.sr.bankmanagement.financialnews;

import FinancialNews.Currency;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.util.Pair;

/**
 * @author Michał Ciołczyk
 */
public interface FinancialDataReceiverCallback {
    void putExchangeRate(Pair<Currency, Currency> currencyPair, float rate);

    void putInterestRate(Currency currency, float rate);
}
