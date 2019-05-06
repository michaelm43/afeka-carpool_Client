package smartspace.rest;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.infra.ActionService;
import smartspace.layout.ElementBoundary;;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties= {"spring.profiles.active=default"})


public class ActionControllerIntegrationTest {
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedActionDao<String> actionDao;
	private EnhancedUserDao<String> userDao;
	private ActionService actionService;

	
	@Value("${smartspace.name}")
	private String appSmartSpace;
	private String adminEmail = "admin@admin";
	
	
	@Autowired
	public void setActionService(ActionService actionService) {
		this.actionService = actionService;
	}
	
	@Autowired
	public void setActionDao(EnhancedActionDao<String> actionDao) {
		this.actionDao = actionDao;
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/actions";
	}
	
	@Before
	public void initAdmin() {
		this.userDao
			.create(new UserEntity("appSmartSpace",adminEmail,"admin","adminAvatar",UserRole.ADMIN,0));
	}
	
	@After
	public void tearDown() {
		this.actionDao
			.deleteAll();
	}

	@Test
	public void postNewAction() throws Exception{
		
		//GIVEN the action data base is empty 
		//AND user data base has 1 use which is admin
		
		//WHEN admin POST new action
		Map<String,Double> latlng = new HashMap<>();
		latlng.put("lat", 3.5);
		latlng.put("lng",2.5);
		Map<String,String> creator = new HashMap<>();
		creator.put("smartspace","MySmartSpace");
		creator.put("email","admin@admin");
		ActionBoundary newAction = new ActionBoundary();
		Map<String,Object> elementProperties = new HashMap<String, Object>();
		elementProperties.put("x", 10);
		elementProperties.put("isTired", true);
		
		newAction.setName("Test");
		newAction.setExpired(false);
		newAction.setLatlng(latlng);
		newAction.setCreator(creator);
		newAction.setElementProperties(elementProperties);
		newAction.setElementType("Test");
		
		this.restTemplate
		.postForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
				newElement, 
				ElementBoundary.class, 
				appSmartSpace,adminEmail);		//TODO How to create the admin? Check if its good!!!!!
		
		//THEN the database contains a single action
		assertThat(this.elementDao
				.readAll())
				.hasSize(1);
	}	

