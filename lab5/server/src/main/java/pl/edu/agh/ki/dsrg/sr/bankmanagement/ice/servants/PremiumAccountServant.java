package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.servants;

import Bank.*;
import Ice.Current;
import Ice.FloatHolder;
import Ice.IntHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.financialnews.FinancialData;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.financialnews.FinancialDataRepository;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.util.CurrencyPackageConverter;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.util.Pair;

/**
 * @author Michał Ciołczyk
 */
@RequiredArgsConstructor(staticName = "create")
public class PremiumAccountServant extends _PremiumAccountDisp {
    @Getter
    private final pl.edu.agh.ki.dsrg.sr.bankmanagement.domain.Account account;

    private final FinancialData financialData = FinancialDataRepository.getInstance();

    @Override
    public void calculateLoan(int amount, Currency currency, int period, FloatHolder interestRateHolder,
                              IntHolder totalCostHolder, Current __current) throws IncorrectData {
        if (amount < 0) {
            throw new IncorrectData("amount < 0");
        }

        if (period <= 0) {
            throw new IncorrectData("period < 0");
        }

        float interestRate = financialData.getInterestRate(
                CurrencyPackageConverter.convertBankCurrencyToFinancialCurrency(currency)
        );

        float exchangeRate = financialData.getExchangeRate(new Pair<>(
                CurrencyPackageConverter.convertBankCurrencyToFinancialCurrency(currency),
                FinancialNews.Currency.PLN
        ));

        int totalCost = (int) Math.round(amount * exchangeRate * (1 + Math.pow(interestRate, period)));

        interestRateHolder.value = interestRate;
        totalCostHolder.value = totalCost;
    }

    @Override
    public int getBalance(Current __current) {
        return account.getBalance();
    }

    @Override
    public String getAccountNumber(Current __current) {
        return account.getAccountNumber();
    }

    @Override
    public void transferMoney(String accountNumber, int amount, Current __current)
            throws IncorrectAccountNumber, IncorrectAmount {
        account.transfer(amount).transferMoneyTo(accountNumber);
    }
}
