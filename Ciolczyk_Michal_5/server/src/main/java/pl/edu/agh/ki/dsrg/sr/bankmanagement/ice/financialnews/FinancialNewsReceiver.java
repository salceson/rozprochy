package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.financialnews;

import FinancialNews.Currency;
import FinancialNews._FinancialNewsReceiverDisp;
import Ice.Current;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.util.Pair;

/**
 * @author Michał Ciołczyk
 */
public class FinancialNewsReceiver extends _FinancialNewsReceiverDisp {
    private final FinancialDataReceiverCallback financialDataReceiverCallback =
            FinancialDataRepository.getInstance();
    private final Logger LOGGER = LoggerFactory.getLogger(FinancialNewsReceiver.class);

    @Override
    public void interestRate(float rate, Currency curr, Current __current) {
        LOGGER.info("Currency: " + curr + ": interest rate: " + rate);
        financialDataReceiverCallback.putInterestRate(curr, rate);
    }

    @Override
    public void exchangeRate(float rate, Currency curr1, Currency curr2, Current __current) {
        LOGGER.info("Currencies: " + curr1 + ", " + curr2 + ": exchange rate: " + rate);
        financialDataReceiverCallback.putExchangeRate(new Pair<>(curr1, curr2), rate);
    }
}
