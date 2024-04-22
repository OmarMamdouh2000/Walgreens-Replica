package com.example.Final;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
@Configuration
public class OrderCassandraConf extends AbstractCassandraConfiguration {
	@Override
    protected String getKeyspaceName() {
        return "orders";
    }

	 @Bean
	    public CqlSessionFactoryBean session() {
	        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
	        session.setContactPoints("127.0.0.1");
	        session.setPort(9042);
	        session.setLocalDatacenter("datacenter1");
	        session.setKeyspaceName(getKeyspaceName());
	        return session;
	    }

	 	@Bean(name = "cassandraSession")
	    public CqlSessionBuilderCustomizer cqlSessionBuilderConfigurer() {
	        return cqlSessionBuilder -> cqlSessionBuilder.withKeyspace("orders");
	    }

}
