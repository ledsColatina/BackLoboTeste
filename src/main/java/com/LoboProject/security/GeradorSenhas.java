package com.LoboProject.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenhas {
	
	public String gerar (String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode(senha));
		return encoder.encode(senha);
	}
	
}
