    package com.upc.leadyourway.config;

    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    public class SecurityConfig {
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/api/leadyourway/v1/users").authenticated()
                            .requestMatchers("/api/leadyourway/v1/rents").authenticated()
                            .requestMatchers("/api/leadyourway/v1/cards").authenticated()
                            .requestMatchers("/api/leadyourway/v1/bicycles").authenticated()
                            .requestMatchers("/api/leadyourway/v1/auth/**").permitAll()
                            .anyRequest().authenticated())
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(session -> session
                            // cuando se establece en STATELESS, significa que no se creará ni
                            // mantendrá ninguna sesión HTTP en el servidor.
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider)
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }



    }
