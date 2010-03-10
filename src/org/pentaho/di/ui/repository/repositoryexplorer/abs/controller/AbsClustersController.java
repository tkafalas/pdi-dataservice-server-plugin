package org.pentaho.di.ui.repository.repositoryexplorer.abs.controller;

import java.util.List;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.IAbsSecurityProvider;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.repository.repositoryexplorer.ControllerInitializationException;
import org.pentaho.di.ui.repository.repositoryexplorer.controllers.ClustersController;

public class AbsClustersController extends ClustersController{
  IAbsSecurityProvider service;
  boolean isAllowed = false;
  @Override
  public void init(Repository repository) throws ControllerInitializationException{
    super.init(repository);
    try {
      if(repository.hasService(IAbsSecurityProvider.class)) {
        service = (IAbsSecurityProvider) repository.getService(IAbsSecurityProvider.class);
        setAllowed(allowedActionsContains(service, IAbsSecurityProvider.CREATE_CONTENT_ACTION));
        bf.createBinding(this, "allowed", "clusters-edit", "!disabled"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
        bf.createBinding(this, "allowed", "clusters-new", "!disabled"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        bf.createBinding(this, "allowed", "clusters-remove", "!disabled"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
      }
    } catch (KettleException e) {
      throw new ControllerInitializationException(e);
    }
  }

  public boolean isAllowed() {
    return isAllowed;
  }

  public void setAllowed(boolean isAllowed) {
    this.isAllowed = isAllowed;
    this.firePropertyChange("allowed", null, isAllowed);
  }
  
  
  @Override
  public void setEnableButtons(boolean enable) {
      if(isAllowed) {
        enableButtons(true, enable, enable);
      } else {
        enableButtons(false, false, false);
      }
  }
  
  private boolean allowedActionsContains(IAbsSecurityProvider service, String action) throws KettleException {
    List<String> allowedActions = service.getAllowedActions(IAbsSecurityProvider.NAMESPACE);
    for (String actionName : allowedActions) {
      if (action != null && action.equals(actionName)) {
        return true;
      }
    }
    return false;
  }

}