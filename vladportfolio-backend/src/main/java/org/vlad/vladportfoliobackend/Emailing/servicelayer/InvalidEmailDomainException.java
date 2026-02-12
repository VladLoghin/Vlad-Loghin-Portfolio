package org.vlad.vladportfoliobackend.Emailing.servicelayer;

public class InvalidEmailDomainException extends RuntimeException {
    public InvalidEmailDomainException(String message) {
        super(message);
    }
}
