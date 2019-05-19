package smartspace.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserRole;


//@Component
@Aspect
public class PermissionChecker {
//	Log logger = LogFactory.getLog(CheckRoleOfUser.class);
	private EnhancedUserDao<String> userDao;
	
	@Autowired
	public PermissionChecker(EnhancedUserDao<String> userDao) {
		super();
		this.userDao = userDao;
	}
	
	
	@Around("@annotation(smartspace.aop.CheckRoleOfUser) && args(userSmartspace,userEmail,role..)")
	public void checkRoll(ProceedingJoinPoint pjp, String smartspace, String email, UserRole role) throws Throwable {
		// before
		String method = pjp.getSignature().getName();
		String fullyQualifiedClassName = pjp.getTarget().getClass().getName();
		Object[] args = pjp.getArgs();
		
		args[2] = userDao.readUserUsingSmartspaceAndEmail(smartspace, email).getRole();
		
//		logger.debug("********* " + fullyQualifiedClassName + "." + method + "(" + smartspace + ","+ email + ","+ role + ",.....)" + role);
		
		pjp.proceed(args);			//TODO this is how to do it?!
		
	}	
}
	
