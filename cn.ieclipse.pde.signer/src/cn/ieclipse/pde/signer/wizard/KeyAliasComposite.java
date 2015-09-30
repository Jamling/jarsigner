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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Key entry manage SWT UI.
 * 
 * @author Jamling
 *         
 */
public class KeyAliasComposite extends Composite implements ModifyListener {
    private Text textAlias;
    private Text textPassword;
    private Text textConfirm;
    private Text textValidity;
    private Text textCN;
    private Text textOU;
    private Text textO;
    private Text textL;
    private Text textS;
    private Text textC;
    
    private KeyAlias keyAlias;
    
    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public KeyAliasComposite(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(2, false));
        
        Label lblNewLabel = new Label(this, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("Alias:");
        
        textAlias = new Text(this, SWT.BORDER);
        textAlias.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblNewLabel_1 = new Label(this, SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_1.setText("Password:");
        
        textPassword = new Text(this, SWT.BORDER | SWT.PASSWORD);
        textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblNewLabel_2 = new Label(this, SWT.NONE);
        lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_2.setText("Confirm:");
        
        textConfirm = new Text(this, SWT.BORDER | SWT.PASSWORD);
        textConfirm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblNewLabel_3 = new Label(this, SWT.NONE);
        lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_3.setText("Validity (days):");
        
        textValidity = new Text(this, SWT.BORDER);
        textValidity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        
        Label lblNewLabel_4 = new Label(this, SWT.NONE);
        lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_4.setText("Common Name (CN):");
        
        textCN = new Text(this, SWT.BORDER);
        textCN.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblNewLabel_5 = new Label(this, SWT.NONE);
        lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_5.setText("Organization Unit (OU):");
        
        textOU = new Text(this, SWT.BORDER);
        textOU.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblNewLabel_6 = new Label(this, SWT.NONE);
        lblNewLabel_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_6.setText("Organization (O):");
        
        textO = new Text(this, SWT.BORDER);
        textO.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblNewLabel_7 = new Label(this, SWT.NONE);
        lblNewLabel_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_7.setText("City or Locality (L):");
        
        textL = new Text(this, SWT.BORDER);
        textL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblNewLabel_8 = new Label(this, SWT.NONE);
        lblNewLabel_8.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_8.setText("State or Province (S):");
        
        textS = new Text(this, SWT.BORDER);
        textS.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblNewLabel_9 = new Label(this, SWT.NONE);
        lblNewLabel_9.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_9.setText("Country Code(C):");
        
        textC = new Text(this, SWT.BORDER);
        textC.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        // constraint
        textC.setTextLimit(2);
        // tool tip
        textAlias.setToolTipText("Item alias");
        textPassword.setToolTipText("The password of alias, must more that 6 charachers");
        textValidity.setToolTipText("valid days");
        textC.setToolTipText("Contry code e.g. CN, US, JP, UK...");
        // event listener
        textAlias.addModifyListener(this);
        textPassword.addModifyListener(this);
        textConfirm.addModifyListener(this);
        textValidity.addModifyListener(this);
        textCN.addModifyListener(this);
        textOU.addModifyListener(this);
        textO.addModifyListener(this);
        textL.addModifyListener(this);
        textS.addModifyListener(this);
        textC.addModifyListener(this);
    }
    
    public void setEditMode(boolean editable) {
        textAlias.setEditable(editable);
        textPassword.setEditable(editable);
        textConfirm.setEditable(editable);
        textValidity.setEditable(editable);
        textCN.setEditable(editable);
        textOU.setEditable(editable);
        textO.setEditable(editable);
        textL.setEditable(editable);
        textS.setEditable(editable);
        textC.setEditable(editable);
    }
    
    public void setKeyItem(KeyAlias keyAlias) {
        textAlias.setText(keyAlias.getAlias());
        textPassword.setText(keyAlias.getPassword() == null ? "" : keyAlias.getPassword());
        try {
            textValidity.setText(String.valueOf(keyAlias.getValidity()));
        } catch (NumberFormatException e) {
        
        }
        textCN.setText(keyAlias.getCn());
        textOU.setText(keyAlias.getOu());
        textO.setText(keyAlias.getO());
        textL.setText(keyAlias.getL());
        textS.setText(keyAlias.getS());
        textC.setText(keyAlias.getC());
        this.keyAlias = keyAlias;
    }
    
    public KeyAlias getKeyItem() {
        if (keyAlias == null) {
            keyAlias = new KeyAlias();
        }
        keyAlias.setAlias(textAlias.getText().trim());
        keyAlias.setPassword(textPassword.getText().trim());
        try {
            keyAlias.setValidity(Long.valueOf(textValidity.getText().trim()));
        } catch (NumberFormatException e) {
        
        }
        keyAlias.setCn(textCN.getText().trim());
        keyAlias.setOu(textOU.getText().trim());
        keyAlias.setO(textO.getText().trim());
        keyAlias.setL(textL.getText().trim());
        keyAlias.setS(textS.getText().trim());
        keyAlias.setC(textC.getText().trim());
        return keyAlias;
    }
    
    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
    
    private String validation;
    
    public void modifyText(ModifyEvent e) {
        validate();
        if (validationListener != null) {
            validationListener.onValidate(validation);
        }
    }
    
    private void validate() {
        KeyAlias i = getKeyItem();
        if (i.getAlias().length() == 0) {
            validation = "Alias can't be empty!";
            return;
        }
        if (i.getPassword().length() < 6) {
            validation = "Password characters must more than 6!";
            return;
        }
        if (i.getValidity() <= 0) {
            validation = "Validity must be a positive number!";
            return;
        }
        if (i.getCn().length() == 0 && i.getC().length() == 0 && i.getOu().length() == 0 && i.getO().length() == 0
                && i.getS().length() == 0 && i.getL().length() == 0) {
            validation = "The issuer info can't be empty!";
            return;
        }
        validation = null;
        return;
    }
    
    public String getValidation() {
        return validation;
    }
    
    private ValidationListener validationListener;
    
    public void setValidationListener(ValidationListener validationListener) {
        this.validationListener = validationListener;
    }
}
