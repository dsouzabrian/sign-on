package com.jio.signon.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class OAuthConfiguration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userService;

    private final PasswordEncoder passwordEncoder;

    private final DataSource dataSource;

    @Value("${jwt.clientId:android-app}")
    private String clientId;

    @Value("${jwt.client-secret:somesecret}")
    private String clientSecret;

    @Value("${jwt.signing-key:supersecret}")
    private String jwtSigningKey;

    @Value("${jwt.accessTokenValiditySeconds:2592000}") // 30 days
    private int accessTokenValiditySeconds;

    @Value("${jwt.authorizedGrantTypes:password,refresh_token}")
    private String[] authorizedGrantTypes;

    @Value("${jwt.refreshTokenValiditySeconds:7884000}") // 90 days
    private int refreshTokenValiditySeconds;


    public OAuthConfiguration(AuthenticationManager authenticationManager, UserDetailsService userService, PasswordEncoder passwordEncoder, DataSource dataSource) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
//                .withClient(clientId)
//                .secret(passwordEncoder.encode(clientSecret))
//                .accessTokenValiditySeconds(accessTokenValiditySeconds)
//                .refreshTokenValiditySeconds(refreshTokenValiditySeconds)
//                .authorizedGrantTypes(authorizedGrantTypes)
//                .scopes("read", "write")
//                .resourceIds("api").and().build();

    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                // store token in database
                .tokenStore(tokenStore())
                // convert token to JWT
                .accessTokenConverter(accessTokenConverter())
                // user service to validate token
                .userDetailsService(userService)
                .authenticationManager(authenticationManager);
    }


    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    JwtAccessTokenConverter accessTokenConverter() {
        return new JwtAccessTokenConverter();
    }
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients(); // here 
    }

}
