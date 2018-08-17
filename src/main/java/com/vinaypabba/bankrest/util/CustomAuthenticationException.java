package com.vinaypabba.bankrest.util;

public class CustomAuthenticationException extends org.springframework.security.core.AuthenticationException {

    public CustomAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public CustomAuthenticationException(String msg) {
        super(msg);
    }

}
