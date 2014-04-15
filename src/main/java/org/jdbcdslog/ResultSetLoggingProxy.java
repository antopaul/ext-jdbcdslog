package org.jdbcdslog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultSetLoggingProxy  implements InvocationHandler {

	static Logger logger = LoggerFactory.getLogger(ResultSetLoggingProxy.class);
	
	Object target = null;
	
	public ResultSetLoggingProxy(ResultSet target) {
		this.target = target;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object r = null;
		try {
			if("equals".equals(method.getName())) {
				ResultSet other = null;
				if(args[0] != null && args[0] instanceof ResultSet) {
					other = (ResultSet)((ResultSetLoggingProxy)Proxy.getInvocationHandler(args[0])).target;
				}
				boolean result = target.equals(other);
				return (Object)Boolean.valueOf(result);
			} else if("hashCode".equals(method.getName())) {
				int result = target.hashCode();
				return (Object) new Integer(result);
			}
			r = method.invoke(target, args);
		} catch(Throwable e) {
			LogUtils.handleException(e, ResultSetLogger.getLogger(), LogUtils.createLogEntry(method, null, null, null));
		}
		if(ResultSetLogger.isInfoEnabled() && method.getName().equals("next") && ((Boolean)r).booleanValue()) {
			String fullMethodName = method.getDeclaringClass().getName() + "." + method.getName();
			ResultSet rs = (ResultSet)target;
			ResultSetMetaData md = rs.getMetaData();
			StringBuffer s = new StringBuffer(fullMethodName).append(" {");
			if(md.getColumnCount() > 0)
				s.append(LogUtils.sqlValueToString(rs.getObject(1)));
			for(int i = 2; i <= md.getColumnCount(); i++)
				s.append(", ").append(LogUtils.sqlValueToString(rs.getObject(i)));
			s.append("}");
			ResultSetLogger.info(s.toString());
		} 
		return r;
	}

	static Object wrapByResultSetProxy(ResultSet r) {
		return Proxy.newProxyInstance(r.getClass().getClassLoader(), new Class[]{ResultSet.class}, 
				new ResultSetLoggingProxy(r));
	}

}
