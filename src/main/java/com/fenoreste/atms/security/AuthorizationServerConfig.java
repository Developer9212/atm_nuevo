package com.fenoreste.atms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

import com.fenoreste.atms.entity.Tabla;
import com.fenoreste.atms.entity.TablaPK;
import com.fenoreste.atms.service.ITablaService;
import com.fenoreste.atms.service.IUserService;


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ITablaService tablasService;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception{
		TablaPK pk = new TablaPK("cajero_receptor","user-ws");
		Tabla tbSecurity = tablasService.buscarPorId(pk);
		
		clients.inMemory()
		.withClient(tbSecurity.getDato1())
		.secret(bCryptPasswordEncoder.encode(tbSecurity.getDato2()))
		.authorizedGrantTypes("password", "refresh_token")
		.scopes("read", "write")
		.accessTokenValiditySeconds(2000)
		.refreshTokenValiditySeconds(2000);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception{
		endpoints.authenticationManager(authenticationManager).userDetailsService((UserDetailsService)userService);
	}
	
}	
