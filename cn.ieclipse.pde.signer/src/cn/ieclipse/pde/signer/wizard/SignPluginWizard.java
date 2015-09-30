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
package cn.ieclipse.pde.signer.wizard;

import java.io.File;
import java.io.FilenameFilter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Sign update site project. All jars under features and plugins directory will
 * to signed.
 * 
 * @author Jamling
 *         
 */
public class SignPluginWizard extends CommonSignWizard {
    
    protected SignPluginPage page0;
    
    public SignPluginWizard() {
        super("Sign Eclipse Plugins Wizard");
        page0 = new SignPluginPage();
    }
    
    @Override
    public void addPages() {
        addPage(page0);
        addPage(page1);
    }
    
    public SignPluginPage getSourcePage() {
        return page0;
    }
    
    @Override
    protected void sign(IProgressMonitor monitor) {
        String input = null;
        String msg = null;
        File f = new File(page0.getSourcePackage());
        File[] features = new File(f, "features").listFiles(jarFilter);
        File[] plugins = new File(f, "plugins").listFiles(jarFilter);
        if (features != null) {
            for (File file : plugins) {
                input = file.getAbsolutePath();
                msg = pcbSign(input, null, "ECLIPSE_");
                if (msg != null) {
                    break;
                }
            }
        }
        if (plugins != null) {
            for (File file : plugins) {
                input = file.getAbsolutePath();
                msg = pcbSign(input, null, "ECLIPSE_");
                if (msg != null) {
                    break;
                }
            }
        }
        if (msg == null) {
            MessageDialog.openInformation(getShell(), "Sign Successfully!",
                    "Sign successfully! The output signed package(s) have been replace the unsigned package(s)");
            page1.saveConf(cfgFile);
        }
        else {
            MessageDialog.openError(getShell(), "Error", String.format("Error while sign %s, error : %s", input, msg));
        }
    }
    
    private FilenameFilter jarFilter = new FilenameFilter() {
        
        public boolean accept(File dir, String name) {
            return name != null && name.endsWith(".jar");
        }
    };
}
