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
import com.redhat.qe.xmlrpc.BaseObject.IntegerAttribute;
import com.redhat.qe.xmlrpc.BaseObject.StringAttribute;
import com.redhat.qe.xmlrpc.Session;

public class TestRun extends BaseObject{
		
	public enum Status{ Running, Stopped }
	
	//variables used to update the testRun
	private IntegerAttribute planID = newIntegerAttribute("plan", null);  
	private IntegerAttribute environmentID = newIntegerAttribute("env_id", null); 
	private IntegerAttribute build = newIntegerAttribute("build", null);  
	private IntegerAttribute manager = newIntegerAttribute("manager", null);  
	private StringAttribute summary = newStringAttribute("summary", null);

	private StringAttribute notes = newStringAttribute("notes", null);
	private IntegerAttribute status = newIntegerAttribute("status", null); 
	private IntegerAttribute productVersion = newIntegerAttribute("product_version", null); 
	private IntegerAttribute product = newIntegerAttribute("product", null); 
	

	/**
	 * 
	 * @param userName your bugzilla/testopia userName
	 * @param password your password 
	 * @param url the url of the testopia server
	 * @param runID - Integer the runID, you may enter null here if you are creating a test run
	 */
	public TestRun(Session session, Integer runID)
	{
		this.session = session;
		this.id = newIntegerAttribute("run_id", runID);
		this.listMethod = "TestRun.list";
		
	}
	public TestRun(Session session, Integer planID, Integer environment, Integer build, Integer manager, String summary)
	{
		this(session, planID, environment, build, manager, summary, null, null);
	}
	
	public TestRun(Session session, Integer planID, Integer environment,  Integer build, Integer manager, String summary, Integer product, Integer productVersion) {
		this.session = session;
		this.planID.set(planID);
		if (environment != -1) this.environmentID = newIntegerAttribute("env_id", environment);
		this.build.set(build);
		this.manager.set(manager);
		this.summary.set(summary);
		this.listMethod = "TestRun.list";
		this.id = newIntegerAttribute("run_id", null);
		this.product.set(product);
		this.productVersion.set(productVersion);
	}
	
	public TestRun(Session session, Integer planID, Integer build, Integer manager, String summary)
	{
		this(session, planID, null, build, manager, summary);
	}
	
	/**
	 * Updates are not called when the .set is used. You must call update after all your sets
	 * to push the changes over to testopia.
	 * @throws TestopiaException if planID is null 
	 * @throws XmlRpcException
	 * (you made the TestCase with a null caseID and have not created a new test plan)
	 */
	public Map<String,Object> update() throws TestopiaException, XmlRpcException
	{
		if (id.get() == null) 
			throw new TestopiaException("runID is null.");
		//update the testRunCase
		return super.updateById("TestRun.update");
	}
	
	public Object applyEnvironmentValue() throws XmlRpcException {
		return callXmlrpcMethod("TestRun.link_env_value", id.get(), environmentID.get());
	}
	
	public Object setTags(String sTags) throws XmlRpcException {
		return callXmlrpcMethod("TestRun.add_tag", id.get(), sTags);
	}	
	
	/**
	 * Calls the create method with the attributes as-is (as set via constructors
	 * or setters).  
	 * @return a map of the newly created object
	 * @throws XmlRpcException
	 */
	public Map<String,Object> create() throws XmlRpcException{
		//this.runID = getNextTestRunID(case_id.get());
		return super.create("TestRun.create");			
	}
	
	public void addCases(Integer... cases) throws XmlRpcException{
		//Map params = new HashMap();
		callXmlrpcMethod("TestRun.add_cases", this.id.get(), (Object)cases);
	}
	
	/*private int getNextTestRunID(Integer caseID) throws XmlRpcException{
		Object[] params = new Object[]{null, caseID};
		Object[] result;
		
		result = (Object[]) session.getClient().execute("TestCase.get_history", params);
		
		int highest = -1;
		for (int i=0;i<result.length;i++){
			Map<String, Object> elem = (Map<String, Object>) result[i];
			int id = (Integer) elem.get("run_id");
			if (id > highest)
			   highest = id;
		}
		return (highest + 1);
	}*/
	
	/**
	 * Gets the attributes of the test run, runID must not be null
	 * @return a hashMap of all the values found. Returns null if there is an error
	 * and the TestRun cannot be returned
	 * @throws TestopiaException 
	 * @throws XmlRpcException
	 */
	public Map<String, Object> getAttributes() throws TestopiaException, XmlRpcException
	{
		if (id.get() == null) 
			throw new TestopiaException("runID is null.");
		
		//get the hashmap
		return get("TestRun.get", id.get());
	}
	
	
	/**
	 * 
	 * @return an array of objects (Object[]) of all the testcases found. 
	 * Returns null if there is an error and the TestRun cannot be returned
	 * @throws TestopiaException
	 * @throws XmlRpcException
	 */
	public Object[] getTestCases()
	throws TestopiaException, XmlRpcException
	{
		if (id.get() == null)
			throw new TestopiaException("runID is null.");
		
		return (Object[])this.callXmlrpcMethod("TestRun.get_test_cases",
												id.get());
	}			
		
	/**
	 * 
	 * @return an array of objects (Object[]) of all the testCaseRuns found. 
	 * Returns null if there is an error and the TestRun cannot be found
	 * @throws Exception
	 * @throws XmlRpcException
	 */
	public Object[] getTestCaseRuns()
	throws TestopiaException, XmlRpcException
	{
		if (id.get() == null) 
			throw new TestopiaException("runID is null.");
			
		return (Object[])this.callXmlrpcMethod("TestRun.get_test_case_runs",
												id.get());
	}
	
	public Status getStatus() {
		return status.get()==1 ? Status.Running : Status.Stopped;
	}
	public void setStatus(Status status) {
		this.status.set(status.equals(Status.Running) ? 0:1);
	}
	
	public String getNotes() {
		return this.notes.get();
	}
	
	public void setNotes(String notes) {
		this.notes.set(notes);
	}

	/*protected Map<String,Object> updateById(String methodName) throws XmlRpcException{
		Map<String,Object> outGoingMap =  getDirtyAttributesMap();
		Object[] list;
		Map<String,Object> map;
		if (outGoingMap.size() > 0) {
			list = (Object[])this.callXmlrpcMethod(methodName, id.get(), outGoingMap);
			map = (Map<String,Object>)list[0];
		}
		else throw new TestopiaException("There are no locally updated fields to update via xmlrpc!");
		this.syncAttributes(map);
		return map;
	}*/

}
