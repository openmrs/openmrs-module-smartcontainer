package org.openmrs.module.smartcontainer.smartData;

/**
 * The class representing SMART datatype
 * <p/>
 * <a
 * href="http://wiki.chip.org/smart-project/index.php/Developers_Documentation:_SMART_Data_Model#CodedValue_RDF">
 * CodedValue</a>
 */
public class CodedValue {
    private String code;
    private String codeBaseURL;
    private String title;
    private CodeProvenance codeProvenance;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeBaseURL() {
        return codeBaseURL;
    }

    public void setCodeBaseURL(String codeBaseURL) {
        this.codeBaseURL = codeBaseURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CodeProvenance getCodeProvenance() {
        return codeProvenance;
    }

    public void setCodeProvenance(CodeProvenance codeProvenance) {
        this.codeProvenance = codeProvenance;
    }

}
