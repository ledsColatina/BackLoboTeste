package com.LoboProject.domain;

import com.LoboProject.domain.SimpleEnum.Status;
import com.LoboProject.domain.SimpleEnum.Tipo;

public class SimpleEnumStat {

	  public static String enumInIf ( Tipo tipo ) {   
	       if ( tipo == Tipo.AJUSTE) { 
	           return "AJUSTE" ; 
	       } else if(tipo == Tipo.DECREMENTO) {
	    	   return "DECREMENTO";
	       }else if(tipo == Tipo.INCREMENTO) {
	    	   return "INCREMENTO";
	       }else {
	    	   return null;
	       }
	  }
	  
	  public static String enumStatus( Status status) {
		  
		  if(status == Status.FILA) {
	    	   return "FILA";
	  	  }	else if(status == Status.EM_PRODUCAO) {
			  return "EM PRODUCAO";
		  } else if( status == Status.EMBALADO) {
			  return "EMBALADO";
		  } else if ( status == Status.DESPACHADO) {
			  return "DESPACHADO";
		  } else if ( status == Status.NOTA_EMITIDA){
			  return "NOTA EMITIDA";
		  }else {
			  return null; 
		  }
	  }
}
