package com.kwakmunsu.diary.global.jwt.filter;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
import com.kwakmunsu.diary.global.exception.dto.response.ErrorResponse;
import com.kwakmunsu.diary.global.jwt.common.TokenType;
import com.kwakmunsu.diary.global.jwt.token.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private static final List<String> EXCLUDE_PATHS = List.of(
            "/", "/error", "/auth/**"
    );
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return EXCLUDE_PATHS.stream()
                .anyMatch(exclude -> pathMatcher.match(exclude, path));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = getTokenFromHeader(request);
        if (!StringUtils.hasText(token)) {
            sendErrorResponse(response, ErrorMessage.WRONG_AUTH_HEADER);
            return;
        }
        if (jwtProvider.isNotValidateToken(token)) {
            sendErrorResponse(response, ErrorMessage.INVALID_TOKEN);
            return;
        }

        Authentication authentication = jwtProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(TokenType.AUTHORIZATION_HEADER.getValue());
        if (bearerToken != null && bearerToken.startsWith(TokenType.BEARER_PREFIX.getValue())) {
            return bearerToken.substring(TokenType.BEARER_PREFIX.getValue().length());
        }

        return null;
    }

    private void sendErrorResponse(
            HttpServletResponse response,
            ErrorMessage  errorMessage
    ) throws IOException {
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(UNAUTHORIZED.value())
                .status(UNAUTHORIZED.toString())
                .message(errorMessage.getMessage())
                .build();
        log.warn("JWT 예외: " + errorMessage.getMessage());

        String jsonToString = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonToString);
    }

}