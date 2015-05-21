package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.financialnews;

import FinancialNews.Currency;
import FinancialNews._FinancialNewsReceiverDisp;
import Ice.Current;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.financialnews.FinancialDataReceiverCallback;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.financialnews.FinancialDataRepository;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.util.Pair;

/**
 * @author Michał Ciołczyk
 */
public class FinancialNewsReceiver extends _FinancialNewsReceiverDisp {
    FinancialDataReceiverCallback financialDataReceiverCallback = FinancialDataRepository.getInstance();

    @Override
    public void interestRate(float rate, Currency curr, Current __current) {
        financialDataReceiverCallback.putInterestRate(curr, rate);
    }

    @Override
    public void exchangeRate(float rate, Currency curr1, Currency curr2, Current __current) {
        financialDataReceiverCallback.putExchangeRate(new Pair<>(curr1, curr2), rate);
    }
}
