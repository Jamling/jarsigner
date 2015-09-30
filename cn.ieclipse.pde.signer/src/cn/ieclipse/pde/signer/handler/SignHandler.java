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
package cn.ieclipse.pde.signer.handler;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import cn.ieclipse.pde.signer.wizard.SignApkWizard;
import cn.ieclipse.pde.signer.wizard.SignJarWizard;
import cn.ieclipse.pde.signer.wizard.SignPluginWizard;

/**
 * Sign Handler
 * 
 * @author Jamling
 * @date 2015年8月31日
 *       
 */
public class SignHandler extends AbstractHandler {
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
     * ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        ISelection sel = window.getSelectionService().getSelection();
        if (sel instanceof IStructuredSelection) {
            Object obj = ((IStructuredSelection) sel).getFirstElement();
            if (obj instanceof IProject) {
                IProject prj = (IProject) obj;
                openProject(prj, null);
            }
            else if (obj instanceof IFile) {
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
            else if (obj instanceof IJavaProject) {
                IJavaProject jprj = (IJavaProject) obj;
                IProject prj = jprj.getProject();
                openProject(prj, jprj);
            }
            else {
                openJar(null);
            }
        }
        return null;
    }
    
    private void openProject(IProject prj, IJavaProject jprj) {
        try {
            if (prj.hasNature("com.android.ide.eclipse.adt.AndroidNature")) {
                IPath p = prj.getLocation();
                if (jprj == null) {
                    jprj = JavaCore.create(prj);
                }
                IPath bin = jprj.getOutputLocation().removeFirstSegments(1).removeLastSegments(1);
                File file = new File(p.append(bin).toOSString());
                File apk = new File(file, prj.getName() + ".apk");
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
        WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
        dialog.open();
    }
}
