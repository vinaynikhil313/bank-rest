package com.vinaypabba.bankrest.security;

import com.vinaypabba.bankrest.service.UserAuthService;
import com.vinaypabba.bankrest.util.CustomAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private AuthenticationErrorHandler unauthorizedHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = httpServletRequest.getHeader("Authorization");
        String token = "";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7, bearerToken.length());
        }
        if(!StringUtils.isEmpty(token)) {
            String[] tokenContents = token.split("_");
            LocalDateTime expiry = LocalDateTime.parse(tokenContents[1], DateTimeFormatter.ISO_DATE_TIME);
            if(expiry.isBefore(LocalDateTime.now())) {
                throw new CustomAuthenticationException("Token is expired");
            } else {
                UserDetails userDetails = userAuthService.loadUserByUsername(tokenContents[0]);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String loanNumber = userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
                        .get(0);
                log.info("Request URI is {}", httpServletRequest.getRequestURI());
                if(httpServletRequest.getRequestURI().contains("account")
                        && !httpServletRequest.getRequestURI().contains(loanNumber)
                        && !httpServletRequest.getRequestURI().contains("add")) {
                    SecurityContextHolder.clearContext();
                    unauthorizedHandler.commence(httpServletRequest, httpServletResponse, new CustomAuthenticationException("This user is not authorized to access this account"));
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
