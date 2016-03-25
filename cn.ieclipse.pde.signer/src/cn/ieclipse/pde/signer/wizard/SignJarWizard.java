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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;

import cn.ieclipse.pde.explorer.Explorer;
import cn.ieclipse.pde.explorer.ExplorerPlugin;

/**
 * Sign jar wizard.
 * 
 * @author Jamling
 *         
 */
public class SignJarWizard extends CommonSignWizard {
    
    protected SignJarPage page0;
    
    public SignJarWizard(String title) {
        super(title);
        page0 = new SignJarPage();
    }
    
    public SignJarWizard() {
        this("Sign Jar Wizard");
    }
    
    @Override
    public void addPages() {
        addPage(page0);
        addPage(page1);
    }
    
    public SignJarPage getSourcePage() {
        return page0;
    }
    
    @Override
    protected void sign(IProgressMonitor monitor) {
        String input = page0.getSourcePackage();
        String output = page0.getDestPackage();
        String msg = super.pcbSign(input, output, null);
        if (msg == null) {
            boolean confirm = MessageDialog.openConfirm(getShell(),
                    "Sign Successfully!",
                    String.format(
                            "Sign successfully! The output package save to %s, would you like to explorer?",
                            page0.getDestPackage()));
            if (confirm) {
                Explorer e = new Explorer(page0.getDestPackage());
                ExplorerPlugin.explorer(e.getFolder(), e.getFile());
            }
            page1.saveConf(cfgFile);
        }
        else {
            MessageDialog.openError(getShell(), "Error", String
                    .format("Error while sign %s, error : %s", input, msg));
        }
    }
}
