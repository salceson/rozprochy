package pl.edu.agh.ki.dsrg.sr.bankmanagement.util;

import lombok.Value;

/**
 * @author Michał Ciołczyk
 */
@Value
public class Pair<A, B> {
    private final A first;
    private final B second;
}
