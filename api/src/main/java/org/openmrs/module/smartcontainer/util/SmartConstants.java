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
	
	public static final String MODULE_ID = "smartcontainer";
	
	/**
	 * The name of the global property to store the true/false value of whether this instance wants
	 * to use the ActiveList/Problem table and object to manage the patient's problems <br/>
	 * Also see the config.xml for the default inclusion of this property in a user's db
	 */
	public static final String GP_USE_PROBLEM_OBJECT_NAME = "smartcontainer.problem.useProblemObject";
	
	/**
	 * The name of the global property to store the true/false of whether this instance wants to use
	 * the obs table to look up a patient's problems <br/>
	 * Also see the config.xml for the default inclusion of this property in a user's db
	 * 
	 * @see SmartConstants#GP_PROBLEM_ADDED_CONCEPT
	 * @see SmartConstants#GP_PROBLEM_RESOLVED_CONCEPT
	 */
	public static final String GP_USE_OBS_FOR_PROBLEM = "smartcontainer.problem.useObsForProblem";
	
	/**
	 * The name of the global property to store the concept id of the "Problem Added" concept used
	 * in forms. This can be used in place or in addition to the problem object
	 * 
	 * @see #GP_USE_OBS_FOR_PROBLEM
	 */
	public static final String GP_PROBLEM_ADDED_CONCEPT = "smartcontainer.problem.addedConcept";
	
	/**
	 * The name of the global property to store the concept id of the "Problem Resolved" concept
	 * used in forms. This can be used in place or in addition to the problem object
	 * 
	 * @see #GP_USE_OBS_FOR_PROBLEM
	 */
	public static final String GP_PROBLEM_RESOLVED_CONCEPT = "smartcontainer.problem.resolvedConcept";
	
	/**
	 * The name of the global property to store the true/false value of whether this instance wants
	 * to use the ActiveList/Allergy table and object to manage the patient's problems
	 */
	public static final String GP_USE_ALLERGY_OBJECT = "smartcontainer.allergy.useAllergyObject";
	
	/**
	 * The name of the global property to store the concept id of the "Smart Allergy Exception"
	 * concept used in forms.
	 */
	public static final String GP_ALLERGY_EXCEPTION_CONCEPT = "smartcontainer.allergy.exceptionConcept";
	
	/**
	 * The id for the local SNOMED concept source to be used by SMART apps that require concept
	 * mappings to it
	 */
	public static final String GP_LOCAL_SNOMED_CONCEPT_SOURCE_ID = "smartcontainer.local.snomedConceptSource";
	
	/**
	 * The id for the local LOINC concept source to be used by SMART apps that require concept
	 * mappings to it
	 */
	public static final String GP_LOCAL_LOINC_CONCEPT_SOURCE_ID = "smartcontainer.local.loincConceptSource";
	
	/**
	 * The id for the local RxNORM concept source to be used by SMART apps that require concept
	 * mappings to it
	 */
	public static final String GP_LOCAL_RxNORM_CONCEPT_SOURCE_ID = "smartcontainer.local.rxNormConceptSource";
	
	/**
	 * The id for the local FDA concept source to be used by SMART apps that require concept
	 * mappings to it
	 */
	public static final String GP_LOCAL_FDA_CONCEPT_SOURCE_ID = "smartcontainer.local.fdaConceptSource";
	
}
