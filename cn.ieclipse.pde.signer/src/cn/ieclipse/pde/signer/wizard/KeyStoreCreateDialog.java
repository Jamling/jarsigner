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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Key store create dialog.
 * 
 * @author Jamling
 *         
 */
public class KeyStoreCreateDialog extends TitleAreaDialog {
    
    private Text textStore;
    private Text textPwd;
    private Text textPwd2;
    
    private String path;
    private String pass;
    
    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public KeyStoreCreateDialog(Shell parentShell) {
        super(parentShell);
    }
    
    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        
        setTitle("Create new keystore");
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayout(new GridLayout(3, false));
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        setTitle("Create new keystore");
        setMessage("Create new keystore file to save your key pair.");
        
        Label lblNewLabel_2 = new Label(container, SWT.NONE);
        lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_2.setText("Keystore:");
        
        textStore = new Text(container, SWT.BORDER);
        textStore.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validate();
            }
        });
        textStore.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Button btnSave = new Button(container, SWT.NONE);
        btnSave.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnSave.setText("&Save to");
        
        Label lblNewLabel_3 = new Label(container, SWT.NONE);
        lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_3.setText("Password:");
        
        textPwd = new Text(container, SWT.BORDER | SWT.PASSWORD);
        textPwd.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validate();
            }
        });
        textPwd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Label(container, SWT.NONE);
        
        Label lblNewLabel_4 = new Label(container, SWT.NONE);
        lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_4.setText("Confirm:");
        
        textPwd2 = new Text(container, SWT.BORDER | SWT.PASSWORD);
        textPwd2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        textPwd2.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                validate();
            }
        });
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        Label lblNewLabel_1 = new Label(container, SWT.WRAP);
        lblNewLabel_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        // event
        btnSave.addSelectionListener(new SelectionAdapter() {
            
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
                String path = dialog.open();
                if (path != null) {
                    textStore.setText(path);
                }
            }
        });
        return area;
    }
    
    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }
    
    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(450, 300);
    }
    
    @Override
    protected void okPressed() {
        validate();
        if (getButton(IDialogConstants.OK_ID).isEnabled()) {
            super.okPressed();
        }
    }
    
    private void validate() {
        path = textStore.getText().trim();
        pass = textPwd.getText();
        if (path.length() <= 0) {
            setErrorMessage("Please select file to save the keystore");
            return;
        }
        else if (!new File(path).exists()) {
            setErrorMessage("Please select file to save the keystore");
        }
        
        if (pass.length() < 6) {
            setErrorMessage("Password must be at least 6 characters!");
            return;
        }
        else if (!pass.equals(textPwd2.getText())) {
            setErrorMessage("Confirm password not same as password!");
            return;
        }
        setErrorMessage(null);
    }
    
    public String getStorePath() {
        return path;
    }
    
    public String getStorePass() {
        return pass;
    }
    
    @Override
    public void setErrorMessage(String newErrorMessage) {
        super.setErrorMessage(newErrorMessage);
        boolean hasError = getErrorMessage() != null && !"".equals(getErrorMessage());
        getButton(IDialogConstants.OK_ID).setEnabled(!hasError);
    }
}
