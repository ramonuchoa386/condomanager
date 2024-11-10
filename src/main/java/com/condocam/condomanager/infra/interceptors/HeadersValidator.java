package com.condocam.condomanager.infra.interceptors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collection;

import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;

import com.condocam.condomanager.infra.config.annotations.AllowAnnonymous;

import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class HeadersValidator implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        final AllowAnnonymous allowAnnonymous = ((HandlerMethod)handler).getMethod().getAnnotation(AllowAnnonymous.class);
        String sessionId = request.getSession().getId();

        if(allowAnnonymous != null){
            return true;
        }
        
        log.info("profile access sessionId: " + sessionId);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication instanceof JwtAuthenticationToken) {
            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            String username = jwt.getClaimAsString("preferred_username");
            String id_condominio = jwt.getClaimAsString("id_condominio");
            String id_administradora = jwt.getClaimAsString("id_administradora");

            log.info("Authenticated user: " + username);
            log.info("ID do condominio: " + id_condominio);
            log.info("ID da administradora: " + id_administradora);
            log.info("User authorities: " + authorities.toString());

            return true;
        }

        log.warn("User is not authenticated");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuário não autenticado.");
        
        return false;
    }
}











