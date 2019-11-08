package com.LoboProject.Protocols;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyFilter extends OncePerRequestFilter {
	
	/*@Autowired
	private loboApiProperty loboApiproperty;*/
	
	@Override
	public void destroy() {
	
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	
	    //final String origin = "http://localhost:3000";
	
	    response.addHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
	    response.setHeader("Access-Control-Allow-Credentials", "true");
	    response.setHeader("Access-Control-Allow-Headers",
	            "content-type, x-gwt-module-base, x-gwt-permutation, clientid, longpush");
	
	    filterChain.doFilter(request, response);
	}

}