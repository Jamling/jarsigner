/*
 * Copyright 2014-2015 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.pde.signer.popup.actions;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import cn.ieclipse.pde.signer.handler.SignHandler;
import cn.ieclipse.pde.signer.wizard.SignApkWizard;
import cn.ieclipse.pde.signer.wizard.SignJarWizard;
import cn.ieclipse.pde.signer.wizard.SignPluginWizard;

/**
 * Sign popup action class.
 * 
 * @deprecated use {@link SignHandler}
 *             
 * @author Jamling
 *         
 */
@Deprecated
public class SignAction implements IObjectActionDelegate {
    
    private Shell shell;
    private ISelection selection;
    
    /**
     * Constructor for Action1.
     */
    public SignAction() {
        super();
    }
    
    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        shell = targetPart.getSite().getShell();
    }
    
    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction action) {
        if (selection instanceof IStructuredSelection) {
            Object obj = ((IStructuredSelection) selection).getFirstElement();
            // String path = null;
            // java project.
            if (obj instanceof IProject) {
                IProject prj = (IProject) obj;
                try {
                    if (prj.hasNature("com.android.ide.eclipse.adt.AndroidNature")) {
                        
                        IJavaProject jprj = JavaCore.create(prj);
                        String bin = jprj.getOutputLocation().toOSString();
                        File apk = new File(new File(bin).getParentFile(), prj.getName() + ".apk");
                        openAndroid(apk.getAbsolutePath());
                    }
                    else if (prj.hasNature("org.eclipse.pde.UpdateSiteNature")) {
                        openPlugin(prj.getLocation().toOSString());
                    }
                    else {
                        openJar(null);
                    }
                } catch (CoreException e) {
                    // TODO
                }
            }
            else if (obj instanceof IResource) {
                IResource resource = (IResource) obj;
                String file = resource.getLocation().toOSString();
                if (file != null) {
                    if (file.endsWith(".jar")) {
                        openJar(file);
                    }
                    else if (file.endsWith(".apk")) {
                        openAndroid(file);
                    }
                    else {
                        openJar(null);
                    }
                }
                else {
                    openJar(null);
                }
            }
            else {
                openJar(null);
            }
        }
    }
    
    private void openPlugin(String dir) {
        SignPluginWizard wizard = new SignPluginWizard();
        wizard.getSourcePage().setSourcePackage(dir);
        open(wizard);
    }
    
    private void openAndroid(String file) {
        SignApkWizard wizard = new SignApkWizard();
        wizard.getSourcePage().setSourcePackage(file);
        open(wizard);
    }
    
    private void openJar(String file) {
        SignJarWizard wizard = new SignJarWizard();
        wizard.getSourcePage().setSourcePackage(file);
        open(wizard);
    }
    
    private void open(Wizard wizard) {
        WizardDialog dialog = new WizardDialog(shell, wizard);
        dialog.open();
    }
    
    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }
    
}
