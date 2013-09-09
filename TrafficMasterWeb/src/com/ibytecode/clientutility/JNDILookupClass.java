package com.ibytecode.clientutility;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
 
public class JNDILookupClass {
 
    private static Context initialContext;
 
    private static final String PKG_INTERFACES = "org.jboss.ejb.client.naming";
 
    public static Context getInitialContext() throws NamingException {
        if (initialContext == null) {
            Properties properties = new Properties();
            properties.put(Context.URL_PKG_PREFIXES, PKG_INTERFACES);
            properties.put("jboss.naming.client.ejb.context", true);
            initialContext = new InitialContext(properties);
        }
        return initialContext;
    }
    
    public static String getLookupName(String appName,String moduleName, Class beanClass, Class interfaceClass, String distinctName) {
        // The EJB bean implementation class name
        String beanName = beanClass.getSimpleName();
 
        // Fully qualified remote interface name
        String interfaceName = interfaceClass.getName();
 
        // Create a look up string name
        String name = "ejb:" + appName + "/" + moduleName + "/" +
                distinctName    + "/" + beanName + "!" + interfaceName;
        return name;
    }
}