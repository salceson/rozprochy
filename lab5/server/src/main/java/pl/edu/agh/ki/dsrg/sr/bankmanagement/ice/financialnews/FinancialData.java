package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.financialnews;

import FinancialNews.Currency;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.util.Pair;

/**
 * @author Michał Ciołczyk
 */
public interface FinancialData {

    float getExchangeRate(Pair<Currency, Currency> currencyPair);

    float getInterestRate(Currency currency);
}
