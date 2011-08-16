/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.smartcontainer.util;

/**
 * Constants for the smartcontainer module
 */
public class SmartConstants {

	/**
	 * The name of the global property to store the true/false value of whether
	 * this instance wants to use the ActiveList/Problem table and object to
	 * manage the patient's problems <br/>
	 * Also see the config.xml for the default inclusion of this property in a
	 * user's db
	 */
	public static final String GP_USE_PROBLEM_OBJECT_NAME = "smartcontainer.problem.useProblemObject";

	/**
	 * The name of the global property to store the true/false of whether this
	 * instance wants to use the obs table to look up a patient's problems <br/>
	 * Also see the config.xml for the default inclusion of this property in a
	 * user's db
	 * 
	 * @see SmartConstants#GP_PROBLEM_ADDED_CONCEPT
	 * @see SmartConstants#GP_PROBLEM_RESOLVED_CONCEPT
	 */
	public static final String GP_USE_OBS_FOR_PROBLEM = "smartcontainer.problem.useObsForProblem";

	/**
	 * The name of the global property to store the concept id of the "Problem Added" concept 
	 * used in forms.  This can be used in place or in addition to the problem object
	 * 
	 * @see #GP_USE_OBS_FOR_PROBLEM
	 */
	public static String GP_PROBLEM_ADDED_CONCEPT = "smartcontainer.problem.addedConcept";
	
	/**
	 * The name of the global property to store the concept id of the "Problem Resolved" concept 
	 * used in forms.  This can be used in place or in addition to the problem object
	 * 
	 * @see #GP_USE_OBS_FOR_PROBLEM
	 */
	public static String GP_PROBLEM_RESOLVED_CONCEPT = "smartcontainer.problem.resolvedConcept";

}
