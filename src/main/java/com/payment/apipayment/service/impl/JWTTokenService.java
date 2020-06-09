package com.payment.apipayment.service.impl;

import com.payment.apipayment.model.UserPrincipal;
import com.payment.apipayment.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class JWTTokenService implements TokenService {
    String JWT_SECRET = "cb5xozmh:J/~=>yv1|u|Cju*L0ZN&LFhXM'BC4b}boI>a,G_Zq{)W0zC&qS5#r";

    @Override
    public UserPrincipal parseToken(String token) {
        byte[] secretBytes = JWT_SECRET.getBytes();

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(secretBytes)
                .build()
                .parseClaimsJws(token);

        String username = jwsClaims.getBody()
                .getSubject();
        Integer userId = jwsClaims.getBody()
                .get("id", Integer.class);
        boolean isAdmin = jwsClaims.getBody().get("admin", Boolean.class);

        return new UserPrincipal(userId, username, isAdmin);
    }
}
