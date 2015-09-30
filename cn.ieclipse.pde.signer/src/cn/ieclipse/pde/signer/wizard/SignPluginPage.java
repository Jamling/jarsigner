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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import cn.ieclipse.pde.signer.util.Utils;

/**
 * Sign eclipse plugin update site project page. Provide a text filed for user
 * to input update site project path.
 * 
 * @author Jamling
 *         
 */
public class SignPluginPage extends WizardPage {
    private String file;
    private Text textSrc;
    
    /**
     * Create the wizard.
     */
    public SignPluginPage() {
        super("SignPluginPage");
        setTitle("Sign Eclipse Plugin");
        setDescription("Sign plugins  and features (directory) output jars under eclipse plugin update site project");
    }
    
    /**
     * Create contents of the wizard.
     * 
     * @param parent
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        
        setControl(container);
        container.setLayout(new GridLayout(3, false));
        
        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("Project:");
        
        textSrc = new Text(container, SWT.BORDER);
        textSrc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        if (file != null) {
            textSrc.setText(file);
        }
        
        Button btnOpen = new Button(container, SWT.NONE);
        GridData gd_btnOpen = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_btnOpen.widthHint = 75;
        btnOpen.setLayoutData(gd_btnOpen);
        btnOpen.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.OPEN);
                String path = dialog.open();
                if (path != null && path.length() > 0) {
                    textSrc.setText(path);
                }
            }
        });
        btnOpen.setText("&Open");
        
        Label lblNewLabel_2 = new Label(container, SWT.NONE);
        lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_2.setImage(ResourceManager.getPluginImage("org.eclipse.pde.ui", "/icons/obj16/alert_obj.gif"));
        
        Label lblNewLabel_1 = new Label(container, SWT.WRAP);
        lblNewLabel_1.setText("The output plugins jars will be forced to use \"ECLIPSE_\" as cert name. ");
        lblNewLabel_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Label(container, SWT.NONE);
        
        textSrc.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                onSourcePackageChanged();
            }
        });
    }
    
    protected void onSourcePackageChanged() {
        setPageComplete(check());
    }
    
    public void setSourcePackage(String file) {
        this.file = file;
    }
    
    public String getSourcePackage() {
        return textSrc.getText().trim();
    }
    
    private boolean check() {
        if (Utils.isEmpty(getSourcePackage())) {
            setErrorMessage("Please select an eclipse plugin update site project.");
            return false;
        }
        File f = new File(getSourcePackage());
        if (!f.exists() || !f.isDirectory()) {
            setErrorMessage("Invalid eclipse plugin update site project.");
            return false;
        }
        
        File sub = new File(f, "plugins");
        if (!sub.exists() || sub.list() == null) {
            setErrorMessage("No jars found under project, did you build your features?");
            return false;
        }
        
        setErrorMessage(null);
        return true;
    }
}
