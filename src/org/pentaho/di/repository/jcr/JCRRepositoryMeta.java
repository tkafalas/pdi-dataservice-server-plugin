package org.pentaho.di.repository.jcr;

import java.util.List;

import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.BaseRepositoryMeta;
import org.pentaho.di.repository.RepositoryCapabilities;
import org.pentaho.di.repository.RepositoryMeta;
import org.w3c.dom.Node;

public class JCRRepositoryMeta extends BaseRepositoryMeta implements RepositoryMeta {

	/** The id as specified in the repository plugin meta, used for backward compatibility only */
	public static String REPOSITORY_TYPE_ID = "KettleJCRRepository";
	
	private JCRRepositoryLocation repositoryLocation;

	private boolean	versionCommentMandatory;

	public JCRRepositoryMeta() {
		super(REPOSITORY_TYPE_ID);
	}
	
	public JCRRepositoryMeta(String id, String name, String description, JCRRepositoryLocation repositoryLocation, boolean versionCommentMandatory)
	{
		super(id, name, description);
		this.repositoryLocation = repositoryLocation;
		this.versionCommentMandatory = versionCommentMandatory;
	}
	
	public String getXML()
	{
        StringBuffer retval = new StringBuffer(100);
		
		retval.append("  ").append(XMLHandler.openTag(XML_TAG));
		retval.append(super.getXML());
		retval.append("    ").append(XMLHandler.addTagValue("repository_location_url",  repositoryLocation.getUrl()));
		retval.append("    ").append(XMLHandler.addTagValue("version_comment_mandatory",  versionCommentMandatory));
		retval.append("  ").append(XMLHandler.closeTag(XML_TAG));
        
		return retval.toString();
	}
	
	public void loadXML(Node repnode, List<DatabaseMeta> databases) throws KettleException
	{
		super.loadXML(repnode, databases);
		try
		{
			String url = XMLHandler.getTagValue(repnode, "repository_location_url") ;
			this.repositoryLocation = new JCRRepositoryLocation(url);
			this.versionCommentMandatory = "Y".equalsIgnoreCase(XMLHandler.getTagValue(repnode, "version_comment_mandatory")) ;
		}
		catch(Exception e)
		{
			throw new KettleException("Unable to load Kettle database repository meta object", e);
		}
	}	


	public RepositoryCapabilities getRepositoryCapabilities() {
    	return new RepositoryCapabilities() {
    		public boolean supportsUsers() { return true; }
    		public boolean managesUsers() { return false; }
    		public boolean isReadOnly() { return false; }
    		public boolean supportsRevisions() { return true; }
    		public boolean supportsMetadata() { return true; }
    		public boolean supportsLocking() { return true; }
    	};
	}

	/**
	 * @return the repositoryLocation
	 */
	public JCRRepositoryLocation getRepositoryLocation() {
		return repositoryLocation;
	}

	/**
	 * @param repositoryLocation the repositoryLocation to set
	 */
	public void setRepositoryLocation(JCRRepositoryLocation repositoryLocation) {
		this.repositoryLocation = repositoryLocation;
	}

	public boolean isVersionCommentMandatory() {
		return versionCommentMandatory;
	}

	public void setVersionCommentMandatory(boolean versionCommentMandatory) {
		this.versionCommentMandatory = versionCommentMandatory;
	}
}