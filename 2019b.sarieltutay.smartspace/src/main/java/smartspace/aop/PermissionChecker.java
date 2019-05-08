package smartspace.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserRole;


@Component
@Aspect
public class PermissionChecker {
	Log logger = LogFactory.getLog(CheckRollOfUser.class);
	private EnhancedUserDao<String> userDao;
	
	@Autowired
	public PermissionChecker(EnhancedUserDao<String> userDao) {
		super();
		this.userDao = userDao;
	}
	
	
	@Before("@annotation(smartspace.aop.CheckRollOfUser) && args(smartspace,email..)")
	public void checkRoll(ProceedingJoinPoint pjp, String smartspace, String email, UserRole role) throws Throwable {
		// before
		String method = pjp.getSignature().getName();
		String fullyQualifiedClassName = pjp.getTarget().getClass().getName();
		
		role = userDao.readUserUsingSmartspaceAndEmail(smartspace, email).getRole();
		
		logger.debug("********* " + fullyQualifiedClassName + "." + method + "(" + smartspace + ","+ email + ","+ role + ",.....)" + role);

		pjp.proceed(new Object[] {smartspace,email,role});			//TODO this is how to do it?!
		
	}	
}
	
