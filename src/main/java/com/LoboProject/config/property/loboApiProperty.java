package com.LoboProject.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@ConfigurationProperties("lobo")
@Data
public class loboApiProperty {
	
	private String originPermitida = "http://localhost:9098";
	
	private final Seguranca seguranca = new Seguranca();
	

	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}

	@Data
	public static class Seguranca{
		
		private boolean enableHttps;
		
	}
	
}
