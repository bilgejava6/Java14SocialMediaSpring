package com.muhammet.config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class JwtManager {

    private final String secretKey = "M7dN4['N%kz#Q,jQ-38H7^I!.s6BfKY'S],Lz}=JO2c5$aNMQ4";
    /**
     * 29 + 10 + 10 = 50
     * 50^50
     */
    private final Long exDate = 1000L*60*5;
    /**
     * Create Token
     * Access Token
     * Get Paramater
     */
    public String createToken(Long authId){
        Algorithm algorithm =  Algorithm.HMAC512(secretKey);
           String token =   JWT.create()
                        .withAudience()
                        .withExpiresAt(new Date(System.currentTimeMillis()+exDate))
                        .withIssuer("BILGE")
                        .withIssuedAt(new Date(System.currentTimeMillis()))
                        .withClaim("serviceName", "AUTH")
                        .withClaim("authId",authId)
                        .sign(algorithm);
         return  token;
    }


    public Optional<Long> getAuthId(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            if(Objects.isNull(decodedJWT))
                return Optional.empty();
            Long authId = decodedJWT.getClaim("authId").asLong();
            return Optional.of(authId);
        }catch (Exception exception){
            return  Optional.empty();
        }

    }

}
