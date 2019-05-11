package smartspace.layout;


import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElementUserController {

	private ElementsUserServiceImpl elementsService;

	@Autowired
	public ElementUserController(ElementsUserServiceImpl elementService) {
		this.elementsService = elementService;
	}
	
	

	@RequestMapping(
			path="/smartspace/elements/{userSmartspace}/{userEmail}",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary newElement (
			@RequestBody ElementBoundary element, 
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail) {		
		return new ElementBoundary(elementsService.newElement(element.convertToEntity()
				,userSmartspace, userEmail,null));			//TODO check if role is ok
			}


	@RequestMapping(
			path="/smartspace/elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
			method=RequestMethod.PUT,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void patchElement (
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId,
			@RequestBody ElementBoundary element){
			this.elementsService
			.setElement(userSmartspace, userEmail,elementSmartspace,elementId,element.convertToEntity(),null);
	}
	
	@RequestMapping(
			path="/smartspace/elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary getSpecipicElementUsingId (
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId) {
		return 
			new ElementBoundary(elementsService
			.getSpecificElement(userSmartspace, userEmail, elementSmartspace,elementId,null));
	}
	
	@RequestMapping(
			path="/smartspace/elements/{userSmartspace}/{userEmail}?page={page}?size={size}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsUsingPagination (
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return 
				this.elementsService
				.getElementsUsingPagination(userSmartspace, userEmail, size, page,null)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
	}
	
	@RequestMapping(
			path="/smartspace/elements/{userSmartspace}/{userEmail}?search=location&x={x}&y={y}&distance={distance}page={page}&size={size}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsUsingPaginationOfLocation (
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="x", required=false, defaultValue="0") int x,
			@RequestParam(name="y", required=false, defaultValue="0") int y,
			@RequestParam(name="distance", required=false, defaultValue="5") int distance,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return 
				this.elementsService
				.getElementsUsingPaginationOfLocation(userSmartspace, userEmail, x, y, distance, size, page, null)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
	}
	
	@RequestMapping(
			path="/smartspace/elements/{userSmartspace}/{userEmail}?search=name&value={name}&spage={page}&size={size}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsUsingPaginationOfSpecifiedName (
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="value", required=true) String name,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return 
				this.elementsService
				.getElementsUsingPaginationOfName(userSmartspace, userEmail, name, size, page, null)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
	}
	
	@RequestMapping(
			path="/smartspace/elements/{userSmartspace}/{userEmail}?search=type&value={type}&spage={page}&size={size}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsUsingPaginationOfSpecifiedType (
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="value", required=true) String type,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return 
				this.elementsService
				.getElementsUsingPaginationOfSpecifiedType(userSmartspace, userEmail, type, size, page, null)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
	}
}


