package org.openmrs.module.smartcontainer;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptMap;

public  class SMARTConceptMap {
private Map<Concept,String> conceptMap=new HashMap<Concept, String>();
private final Log log=LogFactory.getLog(getClass());
private String CONCEPT_SOURCE_NAME;
public String getCONCEPT_SOURCE_NAME() {
	return CONCEPT_SOURCE_NAME;
}
public void setCONCEPT_SOURCE_NAME(String cONCEPT_SOURCE_NAME) {
	CONCEPT_SOURCE_NAME = cONCEPT_SOURCE_NAME;
}
private ConceptMap map = null;
	
	public String lookUp(Concept concept) throws ConceptMappingNotFoundException
			 {
		log.info(CONCEPT_SOURCE_NAME+"  ddd");
		String conceptSource=null;
		conceptSource=conceptMap.get(concept);
		if(conceptSource==null){
		for (ConceptMap cm : concept.getConceptMappings()) {
			if (cm.getSource().getName().equals(getCONCEPT_SOURCE_NAME())) {
				map = cm;
			}
		}
		if(map!=null){
			conceptSource=map.getSourceCode();
			conceptMap.put(concept,conceptSource );
			
		}
		else{
			throw new ConceptMappingNotFoundException("Can not find Concept mapping for concept "+concept);
		}
		}
		return conceptSource;
		
	}
public void resetCache(){
	conceptMap.clear();
}

}
