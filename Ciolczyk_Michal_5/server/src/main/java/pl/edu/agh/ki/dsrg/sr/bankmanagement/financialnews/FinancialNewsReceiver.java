package pl.edu.agh.ki.dsrg.sr.bankmanagement.financialnews;

import FinancialNews.Currency;
import FinancialNews._FinancialNewsReceiverDisp;
import Ice.Current;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michał Ciołczyk
 */
public class FinancialNewsReceiver extends _FinancialNewsReceiverDisp implements FinancialData {
    private final Map<Currency, Float> interestRates = new HashMap<>();
    private final Map<Pair<Currency, Currency>, Float> exchangeRates = new HashMap<>();

    @Override
    public synchronized void interestRate(float rate, Currency curr, Current __current) {
        interestRates.put(curr, rate);
    }

    @Override
    public synchronized void exchangeRate(float rate, Currency curr1, Currency curr2, Current __current) {
        exchangeRates.put(new Pair<>(curr1, curr2), rate);
    }


    @Override
    public synchronized float getExchangeRate(Pair<Currency, Currency> currencyPair) {
        if (exchangeRates.containsKey(currencyPair)) {
            return exchangeRates.get(currencyPair);
        }
        return 0;
    }

    @Override
    public synchronized float getInterestRate(Currency currency) {
        if (interestRates.containsKey(currency)) {
            return interestRates.get(currency);
        }
        return 0;
    }
}
