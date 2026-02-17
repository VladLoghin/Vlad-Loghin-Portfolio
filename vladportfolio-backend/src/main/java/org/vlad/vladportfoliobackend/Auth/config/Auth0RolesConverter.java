package org.vlad.vladportfoliobackend.Auth.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;

import java.util.Collection;

public class Auth0RolesConverter implements Converter<Jwt, Flux<GrantedAuthority>> {

    private static final String ROLES_CLAIM = "https://vladloghin.codes/roles";

    @Override
    public Flux<GrantedAuthority> convert(Jwt jwt) {
        Object rolesObj = jwt.getClaim(ROLES_CLAIM);

        if (rolesObj instanceof Collection<?> roles) {
            return Flux.fromIterable(roles)
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role));
        }

        return Flux.empty();
    }
}
