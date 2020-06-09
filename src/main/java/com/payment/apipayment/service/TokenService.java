package com.payment.apipayment.service;

import com.payment.apipayment.model.UserPrincipal;

public interface TokenService {
    UserPrincipal parseToken(String token);
}
