 /*
  * The contents of this file are subject to the Mozilla Public
  * License Version 1.1 (the "License"); you may not use this file
  * except in compliance with the License. You may obtain a copy of
  * the License at http://www.mozilla.org/MPL/
  *
  * Software distributed under the License is distributed on an "AS
  * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
  * implied. See the License for the specific language governing
  * rights and limitations under the License.
  *
  * The Original Code is the Bugzilla Testopia Java API.
  *
  * The Initial Developer of the Original Code is Andrew Nelson.
  * Portions created by Andrew Nelson are Copyright (C) 2006
  * Novell. All Rights Reserved.
  *
  * Contributor(s): Andrew Nelson <anelson@novell.com>
  *
  */
package tcms.API;

import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;

import com.redhat.qe.xmlrpc.BaseObject;
import com.redhat.qe.xmlrpc.Session;

/**
 * 
 * @author anelson, bstice
 * Retrives the user ID for the inputed email
 */
/**
 * @author weissj
 *
 */
/**
 * @author weissj
 *
 */
public class User extends BaseObject{
	 
	private String user; 
	 
	 /**
	  * 
	  * @param userName - your testopia/bugzilla username
	  * @param password - the password for your account 
	  * @param login - the user you want attributes returned for
	  * @param url - the url of the testopia server
	  */
	 public User(Session session, String login)
	 {
		 this.user = login;
		 this.session = session;
		//this.id = newStringAttribute("id", login);

	 }
	
	 
	 /** Creates a user object for the currently logged in user
	 * @param session
	 */
	public User(Session session){
		 this.id = newIntegerAttribute("id", null);
		 this.session = session;
	}
	 
	 /**
	 * @return the user_id for the specified login. Returns 0 if there is
	 *         an error and the user ID cannot be returned
	 * @throws XmlRpcException 
	 */
	 public Integer getId() {
		 try {
			 Map map;
		 
			 if (user == null){
				 map = (Map)this.callXmlrpcMethod("User.get_me");
			 } else {	 
				 map = (Map)this.callXmlrpcMethod("User.get", user);
			 }
			 return (Integer)map.get("id");			
			 
		 }
		 catch (XmlRpcException xe){
			 throw new RuntimeException("Could not retrieve user.", xe);
		 }
	 }

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
}
