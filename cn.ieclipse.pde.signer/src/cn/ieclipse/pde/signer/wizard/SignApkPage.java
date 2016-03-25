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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import cn.ieclipse.pde.signer.util.Utils;

/**
 * 
 * @author Jamling
 * @deprecated
 */
public class SignApkPage extends WizardPage {
    private Text textSrc;
    private Text textDst;
    
    /**
     * Create the wizard.
     */
    public SignApkPage() {
        super("wizardPage");
        setTitle("Wizard Page title");
        setDescription("Wizard Page description");
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
        lblNewLabel.setText("Source package:");
        
        textSrc = new Text(container, SWT.BORDER);
        textSrc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Button btnOpen = new Button(container, SWT.NONE);
        GridData gd_btnOpen = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_btnOpen.widthHint = 75;
        btnOpen.setLayoutData(gd_btnOpen);
        btnOpen.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
                dialog.setFilterExtensions(new String[] { "*.apk" });
                String path = dialog.open();
                if (path != null && path.length() > 0) {
                    textSrc.setText(path);
                }
            }
        });
        btnOpen.setText("&Open");
        
        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_1.setText("Dest package:");
        
        textDst = new Text(container, SWT.BORDER);
        textDst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Button btnSave = new Button(container, SWT.NONE);
        btnSave.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnSave.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
                // dialog.setFilterExtensions(new String[] { "jar", "apk", "jad"
                // });
                String path = dialog.open();
                if (path != null && path.length() > 0) {
                    textDst.setText(path);
                }
            }
        });
        btnSave.setText("&Save");
        // new Label(container, SWT.NONE);
        //
        // Button btnCheckButton_1 = new Button(container, SWT.CHECK);
        // btnCheckButton_1.setText("Zipalign the apk.");
        // new Label(container, SWT.NONE);
        //
        // Label lblNewLabel_2 = new Label(container, SWT.NONE);
        // lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
        // false, false, 1, 1));
        // lblNewLabel_2.setText("Optimized package:");
        //
        // text = new Text(container, SWT.BORDER);
        // text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
        // 1));
        //
        // Button btnSave_1 = new Button(container, SWT.NONE);
        // btnSave_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
        // false, 1, 1));
        // btnSave_1.setText("Save");
        
        textSrc.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                onSourcePackageChanged();
            }
        });
    }
    
    protected void onSourcePackageChanged() {
        String path = getSourcePackage();
        int pos = path.lastIndexOf(".");
        if (pos >= 0) {
            String dst = path.substring(0, pos) + "_signed" + path.substring(pos);
            textDst.setText(dst);
        }
        setPageComplete(check());
    }
    
    public void setSourcePackage(String file) {
        textSrc.setText(file);
    }
    
    public String getSourcePackage() {
        return textSrc.getText().trim();
    }
    
    public String getDestPackage() {
        return textDst.getText();
    }
    
    private boolean check() {
        if (Utils.isEmpty(getSourcePackage())) {
            setErrorMessage("Please select source package to sign.");
            return false;
        }
        
        if (!new File(getSourcePackage()).exists() || new File(getSourcePackage()).isDirectory()) {
            setErrorMessage("Invalid source package.");
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean isPageComplete() {
        return super.isPageComplete() && check();
    }
}
