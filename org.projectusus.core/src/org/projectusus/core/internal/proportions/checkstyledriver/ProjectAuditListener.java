// Copyright (c) 2009 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal.proportions.checkstyledriver;

import static org.eclipse.core.runtime.IStatus.ERROR;
import static org.projectusus.core.internal.UsusCorePlugin.getPluginId;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.projectusus.core.internal.proportions.sqi.NewWorkspaceResults;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

class ProjectAuditListener implements AuditListener {
    private final Set<IStatus> errors = new HashSet<IStatus>();

    private final IProject currentProject;
    private final Collection<IFile> currentFiles;

    ProjectAuditListener( IProject project, Collection<IFile> files ) {
        super();
        this.currentProject = project;
        this.currentFiles = files;
    }

    boolean hasErrors() {
        return !errors.isEmpty();
    }

    IStatus getErrors() {
        return createMultiStatusFrom( errors );
    }

    // interface methods
    // //////////////////

    public void addError( AuditEvent event ) {
        // unused
    }

    public void fileStarted( AuditEvent event ) {
        String filename = event.getFileName();
        for( IFile file : currentFiles ) {
            if( getFileSystemLocation( file ).equals( filename ) ) {
                NewWorkspaceResults.getInstance().setCurrentFile( file );
            }
        }
    }

    public void fileFinished( AuditEvent event ) {
        NewWorkspaceResults.getInstance().setCurrentFile( null );
    }

    public void auditStarted( AuditEvent event ) {
        NewWorkspaceResults.getInstance().setCurrentProject( currentProject );
    }

    public void auditFinished( AuditEvent event ) {
        NewWorkspaceResults.getInstance().setCurrentProject( null );
    }

    public void addException( AuditEvent event, Throwable thr ) {
        if( thr instanceof CoreException ) {
            errors.add( ((CoreException)thr).getStatus() );
        } else {
            addNewStatusFrom( thr );
        }
    }

    // internal methods
    // /////////////////

    private void addNewStatusFrom( Throwable thr ) {
        String msg = thr.getMessage() == null ? "[No details.]" : thr.getMessage(); //$NON-NLS-1$
        errors.add( new Status( IStatus.ERROR, getPluginId(), msg, thr ) );
    }

    private IStatus createMultiStatusFrom( Set<IStatus> collectedErrors ) {
        String message = "Errors occurred during ISIS computation."; //$NON-NLS-1$
        MultiStatus result = new MultiStatus( getPluginId(), ERROR, message, null );
        for( IStatus error : collectedErrors ) {
            result.add( error );
        }
        return result;
    }

    private String getFileSystemLocation( IFile file ) {
        return file.getLocation().toOSString();
    }
}
