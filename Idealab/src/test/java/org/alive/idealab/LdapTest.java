package org.alive.idealab;


import org.junit.jupiter.api.Test;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/6/9 17:13
 */
public class LdapTest {

    @Test
    public void initLdap() throws Exception {
        Hashtable<String, String> environment = new Hashtable<>();

        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, "ldap://172.16.8.99:389");
        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
        // environment.put(Context.SECURITY_PRINCIPAL, "DSTCAR\\skywalking");
        environment.put(Context.SECURITY_PRINCIPAL, "skywalking@dstcar.com");
        environment.put(Context.SECURITY_CREDENTIALS, "FOwY3ioeYyuu71msaUJEIO3bDcPP9BMH");
        environment.put(Context.REFERRAL, "follow");

        DirContext context = new InitialDirContext(environment);

        String filter = "(&(objectClass=person))";
        String accountName = "skywalking";
        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + accountName + "))";
        String[] attrIDs = { "cn" };
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(attrIDs);
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration<SearchResult> searchResults
                = context.search("dc=dstcar,dc=com", searchFilter, searchControls);

        if (searchResults.hasMore()) {
            SearchResult result = (SearchResult) searchResults.next();
            Attributes attrs = result.getAttributes();

            String distinguishedName = result.getNameInNamespace();
            System.out.println(distinguishedName);
            String commonName = attrs.get("cn").toString();
            // assertThat(commonName, isEqualTo("cn: Joe Simms")));
            System.out.println(commonName);
        }

        context.close();

    }

    @Test
    public void authUser() throws NamingException {
        Hashtable<String, String> environment = new Hashtable<>();

        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, "ldap://172.16.8.99:389");
        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
        // environment.put(Context.SECURITY_PRINCIPAL, "DSTCAR\\skywalking");
        environment.put(Context.SECURITY_PRINCIPAL, "xuhailin@dstcar.com");
        environment.put(Context.SECURITY_CREDENTIALS, "AaZz1@#4502");
        environment.put(Context.REFERRAL, "follow");

        DirContext context = new InitialDirContext(environment);
        System.out.println("Auth Success");
        context.close();
    }
}
