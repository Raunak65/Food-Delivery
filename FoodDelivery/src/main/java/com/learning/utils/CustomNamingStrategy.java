package com.learning.utils;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.stereotype.Component;
@Component
public class CustomNamingStrategy extends PhysicalNamingStrategyStandardImpl {

private static final String POSTFIX ="_table";
	
	@Override
	public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment context) {
		if(identifier==null) {
			return null;
		}
		final String newName = identifier.getText()+POSTFIX;
		
		return Identifier.toIdentifier(newName);
	}
	
	@Override
	public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment context) {
		// TODO Auto-generated method stub
		if(identifier==null) {
			return null;
		}
		return Identifier.toIdentifier(identifier.getText().toLowerCase());
	}

}
