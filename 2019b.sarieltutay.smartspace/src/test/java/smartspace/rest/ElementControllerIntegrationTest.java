package smartspace.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.infra.*;
import smartspace.layout.ElementBoundary;
import smartspace.layout.LocationForBoundary;
import smartspace.layout.UserForBoundary;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class ElementControllerIntegrationTest {
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedElementDao<String> elementDao;
	private EnhancedUserDao<String> userDao;
	private ElementsService elementService;
	
	@Value("${smartspace.name}")
	private String appSmartSpace;
	private String adminEmail = "admin@admin";
	
	
	@Autowired
	public void setElementService(ElementsService elementService) {
		this.elementService = elementService;
	}
	
	@Autowired
	public void setElementDao(EnhancedElementDao<String> elementDao) {
		this.elementDao = elementDao;
	}
	
	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/elements";
	}
	
	@Before
	public void inti() {
		this.userDao
			.create(new UserEntity("appSmartSpace",adminEmail,"admin","adminAvatar",UserRole.ADMIN,0));
	}
	
	@After
	public void tearDown() {
		this.elementDao
			.deleteAll();
	}
	
	@Test
	public void postNewElement() throws Exception{
		
		//GIVEN the element data base is empty 
		//AND user data base has 1 use which is admin
		
		//WHEN admin POST new element
		LocationForBoundary latlng = new LocationForBoundary();
		latlng.setLat(3.5);
		latlng.setLng(2.5);
		UserForBoundary creator = new UserForBoundary();
		creator.setSmartspace("MySmartSpace");
		creator.setEmail("admin@admin");
		ElementBoundary newElement = new ElementBoundary();
		Map<String,Object> elementProperties = new HashMap<String, Object>();
		elementProperties.put("x", 10);
		elementProperties.put("isTired", true);
		
		newElement.setName("Test");
		newElement.setExpired(false);
		newElement.setLatlng(latlng);
		newElement.setCreator(creator);
		newElement.setElementProperties(elementProperties);
		newElement.setElementType("Test");
		
		this.restTemplate
		.postForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
				newElement, 
				ElementBoundary.class, 
				appSmartSpace,adminEmail);		//TODO How to create the admin? Check if its good!!!!!
		
		//THEN the database contains a single element
		assertThat(this.elementDao
				.readAll())
				.hasSize(1);
	}
	
	@Test
	public void postListOfElements() throws Exception {
		
		//GIVEN the element data base is empty 
		//AND user data base has 1 use which is admin
		
		//WHEN admin POST new list of 5 elements
		int size = 5;
		List<ElementBoundary> listOfElement = new ArrayList<ElementBoundary>();
		ElementBoundary newElement = new ElementBoundary();
		
		LocationForBoundary latlng = new LocationForBoundary();
		latlng.setLat(3.5);
		latlng.setLng(2.5);
		
		UserForBoundary creator = new UserForBoundary();
		creator.setSmartspace("MySmartSpace");
		creator.setEmail("admin@admin");

		Map<String,Object> elementProperties = new HashMap<String, Object>();
		elementProperties.put("x", 10);
		elementProperties.put("isTired", true);
		
		IntStream.range(1, size+1)
		.forEach(i-> {			
			newElement.setName("Test");
			newElement.setExpired(false);
			newElement.setLatlng(latlng);
			newElement.setCreator(creator);
			newElement.setElementProperties(elementProperties);
			newElement.setElementType("Test");
			listOfElement.add(newElement);
		});
	
		this.restTemplate
		.postForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
				listOfElement, 
				ElementBoundary.class, 
				appSmartSpace,adminEmail);		//TODO How to create the admin? Check if its good!!!!!
		
		//THEN the database contains the exact amount of elements
		assertThat(this.elementDao
				.readAll())
				.hasSize(size);
	}
	

	@Test(expected=Exception.class)
	public void testPostNewMessageWithBadCode() throws Exception{
		// GIVEN the database is empty
		
		// WHEN I POST new message with bad code
		LocationForBoundary latlng = new LocationForBoundary();
		latlng.setLat(3.5);
		latlng.setLng(2.5);
		
		UserForBoundary creator = new UserForBoundary();
		creator.setSmartspace("MySmartSpace");
		creator.setEmail("admin@admin");
		ElementBoundary newElement = new ElementBoundary();
		Map<String,Object> elementProperties = new HashMap<String, Object>();
		elementProperties.put("x", 10);
		elementProperties.put("isTired", true);
		
		newElement.setName("Test");
		newElement.setExpired(false);
		newElement.setLatlng(latlng);
		newElement.setCreator(creator);
		newElement.setElementProperties(elementProperties);
		newElement.setElementType("Test");
		
		this.restTemplate
		.postForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
				newElement, 
				ElementBoundary.class, 
				"Not our smartspace","NotAdmin");		//TODO How to create the admin? Check if its good!!!!!
		
		// THEN the test end with exception
		
		//assertThat(restTemplate)
	}
	
	
	@Test
	public void testGetAllMessagesUsingPagination() throws Exception{
		// GIVEN the database contains 5 elements
		int size = 5;
		IntStream.range(1, size + 1)
		.mapToObj(i-> new ElementEntity())
		.forEach(this.elementDao::create);
		
		// WHEN I GET messages of size 10 and page 0
		ElementBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}" +"?size={size}&page={page}", 
					ElementBoundary[].class, 
					appSmartSpace,adminEmail,10, 0);
		
		// THEN I receive 3 messages
		assertThat(response)
			.hasSize(size);
	}
	
}