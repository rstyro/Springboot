package top.lrshuai.SpringBootmultisource.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		 return DatabaseContextHolder.getDatabaseType();
	}

}
