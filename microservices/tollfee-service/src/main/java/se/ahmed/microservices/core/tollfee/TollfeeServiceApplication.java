package se.ahmed.microservices.core.tollfee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.FileCopyUtils;
import se.ahmed.api.core.tollfee.TollFee;
import se.ahmed.api.core.tollfee.TollFeeDB;
import se.ahmed.api.core.tollfee.filedb.TollFeeDBUtil;
import se.ahmed.microservices.core.tollfee.persistence.TollFeeEntity;
import se.ahmed.microservices.core.tollfee.services.TollFeeMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ComponentScan("se.ahmed")
public class TollfeeServiceApplication {
	private static final Logger LOG = LoggerFactory.getLogger(TollfeeServiceApplication.class);

	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	TollFeeMapper mapper;

	@Autowired
	MongoOperations mongoTemplate;

	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = SpringApplication.run(TollfeeServiceApplication.class, args);

		String mongodDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
		String mongodDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
		LOG.info("Connected to MongoDb: " + mongodDbHost + ":" + mongodDbPort);
	}


	@EventListener(ContextRefreshedEvent.class)
	public void initIndicesAfterStartup() {
		mongoTemplate.remove(new Query(), "tollfees");
		List<TollFee> tollfees = readTollFeesFromFile();
		tollfees.forEach(tollfee ->{
			TollFeeEntity entity = mapper.apiToEntity(tollfee);

			mongoTemplate.insert(entity);
		});


		MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = mongoTemplate.getConverter().getMappingContext();
		IndexResolver resolver = new MongoPersistentEntityIndexResolver(mappingContext);

		IndexOperations indexOps = mongoTemplate.indexOps(TollFeeEntity.class);
		resolver.resolveIndexFor(TollFeeEntity.class).forEach(e -> indexOps.ensureIndex(e));

	}

	private List<TollFee> readTollFeesFromFile(){
		Resource resource = resourceLoader.getResource("classpath:data/data.json");
		List<TollFee> tollfees = new ArrayList<>();

		try
		{
			InputStream inputStream = resource.getInputStream();
			byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
			String json = new String(bdata, StandardCharsets.UTF_8);
			System.out.println(json);
			TollFeeDBUtil tollFeeDBUtil = new TollFeeDBUtil(json);

			tollfees = tollFeeDBUtil.findAll();
		}catch (IOException e)
		{
			LOG.error("IOException", e);
		}
		return tollfees;
	}
}
