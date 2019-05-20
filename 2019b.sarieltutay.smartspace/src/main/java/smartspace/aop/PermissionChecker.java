package smartspace.aop;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
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
	public Object checkRoll(ProceedingJoinPoint pjp, String userSmartspace, String userEmail, UserRole role) throws Throwable {
		// before
		String method = pjp.getSignature().getName();
		String fullyQualifiedClassName = pjp.getTarget().getClass().getName();
		Object[] args = pjp.getArgs();
		
		Optional<UserEntity> user = this.userDao.readById(userSmartspace + "=" + userEmail);
		
		args[2] = user.get().getRole();
				
//		logger.debug("********* " + fullyQualifiedClassName + "." + method + "(" + smartspace + ","+ email + ","+ role + ",.....)" + role);
		Object rv;
		try {
			rv = pjp.proceed(args);			//TODO this is how to do it?!
			return rv;
		} catch (Exception e) {
			throw e;
		}
	
	}	
}

