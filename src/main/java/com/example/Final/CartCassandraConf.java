package com.example.Final;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
@Configuration
public class CartCassandraConf extends AbstractCassandraConfiguration {

	@Value("${spring.data.cassandra.contactpoints}")
	private String contactPoints;

	@Value("${spring.data.cassandra.port}")
	private int port;

	

	@Override
    protected String getKeyspaceName() {
        return "cart";
    }

	 @Bean
	    public CqlSessionFactoryBean session() {
	        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
	        session.setContactPoints("host.docker.internal");
	        session.setPort(9043);
	        session.setLocalDatacenter("datacenter1");
	        session.setKeyspaceName(getKeyspaceName());
	        return session;
	    }

	 	@Bean(name = "cassandraSession")
	    public CqlSessionBuilderCustomizer cqlSessionBuilderConfigurer() {
	        return cqlSessionBuilder -> cqlSessionBuilder.withKeyspace("cart");
	    }

}
