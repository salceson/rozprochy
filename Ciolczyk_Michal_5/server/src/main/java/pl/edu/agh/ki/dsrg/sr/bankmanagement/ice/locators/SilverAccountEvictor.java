package pl.edu.agh.ki.dsrg.sr.bankmanagement.ice.locators;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.ki.dsrg.sr.bankmanagement.domain.AccountRepository;

/**
 * @author Michał Ciołczyk
 */
@RequiredArgsConstructor
public class SilverAccountEvictor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SilverAccountEvictor.class);

    private final AccountRepository accountRepository = AccountRepository.getInstance();
}
