package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.financialnews;

import FinancialNews.Currency;
import com.google.common.collect.Maps;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.util.Pair;

import java.util.Map;

/**
 * @author Michał Ciołczyk
 */
public class FinancialDataRepository implements FinancialData, FinancialDataReceiverCallback {
    private static final FinancialDataRepository instance = new FinancialDataRepository();

    private final Map<Currency, Float> interestRates = Maps.newConcurrentMap();
    private final Map<Pair<Currency, Currency>, Float> exchangeRates = Maps.newConcurrentMap();

    @Override
    public float getExchangeRate(Pair<Currency, Currency> currencyPair) {
        return exchangeRates.getOrDefault(currencyPair, 1.0f);
    }

    @Override
    public float getInterestRate(Currency currency) {
        return interestRates.getOrDefault(currency, 0.0f);
    }

    @Override
    public void putExchangeRate(Pair<Currency, Currency> currencyPair, float rate) {
        exchangeRates.put(currencyPair, rate);
    }

    @Override
    public void putInterestRate(Currency currency, float rate) {
        interestRates.put(currency, rate);
    }

    public static FinancialDataRepository getInstance() {
        return instance;
    }
}
