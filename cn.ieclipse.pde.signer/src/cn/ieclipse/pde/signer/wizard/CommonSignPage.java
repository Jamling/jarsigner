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
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import cn.ieclipse.pde.signer.util.KeyTool;
import cn.ieclipse.pde.signer.util.Utils;

/**
 * Key tool page.
 * 
 * <pre>
 * 1, manage the key store.
 * 2, manage the key entry.
 * 3, open the key entry.
 * </pre>
 * 
 * @author Jamling
 *         
 */
public class CommonSignPage extends WizardPage {
    
    /**
     * Create the wizard.
     */
    public CommonSignPage() {
        super("wizardPage");
        setTitle("Sign package");
        setDescription("Sign your package with key entry");
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
        
        createSourceComponent(container);
        createKeyComponent(container);
    }
    
    protected void createSourceComponent(Composite container) {
    
    }
    
    private Text textStore;
    private Text textStorePass;
    private Text textAliasPass;
    private Button btnOpenStore;
    private Button btnNewStore;
    private Button chkStorePass;
    private Combo comboAlias;
    private Button btnViewAlias;
    private Button btnNewAlias;
    private Button chkAliasPass;
    private Text lbMD5Finger;
    private Text lbSHA1Finger;
    private Text lbValidity;
    
    private KeyTool mKeyTool;
    
    protected void createKeyComponent(Composite container) {
        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("Keystore:");
        
        textStore = new Text(container, SWT.BORDER);
        textStore.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        btnOpenStore = new Button(container, SWT.NONE);
        btnOpenStore.setText("Browse...");
        
        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_1.setText("Password:");
        
        textStorePass = new Text(container, SWT.BORDER | SWT.PASSWORD);
        textStorePass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        btnNewStore = new Button(container, SWT.NONE);
        
        btnNewStore.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
        btnNewStore.setText("Create...");
        new Label(container, SWT.NONE);
        
        chkStorePass = new Button(container, SWT.CHECK);
        chkStorePass.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean chk = chkStorePass.getSelection();
                if (chk) {
                    if (mKeyTool != null) {// open
                        conf.setStorePass(getKeystore(), getStorePass());
                    }
                    else {
                        conf.setStorePass(getKeystore(), null);
                    }
                }
            }
        });
        chkStorePass.setText("Remember keystore password");
        new Label(container, SWT.NONE);
        
        Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        
        Label lblNewLabel_2 = new Label(container, SWT.NONE);
        lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_2.setText("Alias:");
        
        comboAlias = new Combo(container, SWT.READ_ONLY);
        comboAlias.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        btnViewAlias = new Button(container, SWT.NONE);
        
        btnViewAlias.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnViewAlias.setText("Detail...");
        
        Label lblNewLabel_3 = new Label(container, SWT.NONE);
        lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_3.setText("Password:");
        
        textAliasPass = new Text(container, SWT.BORDER | SWT.PASSWORD);
        textAliasPass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        btnNewAlias = new Button(container, SWT.NONE);
        btnNewAlias.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnNewAlias.setText("Create...");
        new Label(container, SWT.NONE);
        
        chkAliasPass = new Button(container, SWT.CHECK);
        chkAliasPass.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean chk = chkAliasPass.getSelection();
                if (chk) {
                    conf.setAliasPass(getKeystore(), getAlias(), isAliasOpen() ? getAliasPass() : null);
                }
            }
        });
        chkAliasPass.setText("Remember alias password");
        new Label(container, SWT.NONE);
        
        Label lblNewLabel_4 = new Label(container, SWT.NONE);
        lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_4.setText("MD5 finger:");
        
        lbMD5Finger = new Text(container, SWT.WRAP | SWT.READ_ONLY);
        lbMD5Finger.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        lbMD5Finger.setText("");
        
        Label lblNewLabel_6 = new Label(container, SWT.NONE);
        lblNewLabel_6.setText("SHA1 finger:");
        
        lbSHA1Finger = new Text(container, SWT.WRAP | SWT.READ_ONLY);
        lbSHA1Finger.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        lbSHA1Finger.setText("");
        
        Label lblNewLabel_8 = new Label(container, SWT.NONE);
        lblNewLabel_8.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_8.setText("Validity:");
        
        lbValidity = new Text(container, SWT.WRAP | SWT.READ_ONLY);
        lbValidity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        lbValidity.setText("");
        
        // event
        textStorePass.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                onStorePassChanged();
            }
        });
        textStore.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                onStoreChanged();
            }
        });
        
        textAliasPass.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                onAliasPassChanged();
            }
        });
        
        comboAlias.addSelectionListener(new SelectionAdapter() {
            
            public void widgetSelected(SelectionEvent e) {
                onAliasChanged();
            }
        });
        btnOpenStore.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
                String file = dialog.open();
                if (file != null) {
                    textStore.setText(file);
                }
            }
        });
        btnNewStore.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                KeyStoreCreateDialog dialog = new KeyStoreCreateDialog(getShell());
                int code = dialog.open();
                if (code == 0) {
                    textStore.setText(dialog.getStorePath());
                    textStorePass.setText(dialog.getStorePass());
                    try {
                        mKeyTool = new KeyTool(dialog.getStorePass());
                        mKeyTool.save(dialog.getStorePath(), dialog.getStorePass());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        btnNewAlias.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (mKeyTool != null) {
                    KeyAliasDialog dialog = new KeyAliasDialog(getShell());
                    dialog.setTool(mKeyTool);
                    dialog.setEditMode(true);
                    int btn = dialog.open();
                    if (btn == 0) {
                        mKeyTool = dialog.getTool();
                        openStore();
                        onAliasChanged();
                    }
                }
            }
        });
        btnViewAlias.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int index = comboAlias.getSelectionIndex();
                if (index < 0) {
                    return;
                }
                KeyAliasDialog dialog = new KeyAliasDialog(getShell());
                KeyAlias input = new KeyAlias();
                String alias = comboAlias.getItem(index);
                input.setAlias(alias);
                try {
                    input.setValidity(mKeyTool.getValidity(alias));
                    input.setIssuer(mKeyTool.getIssuer(alias));
                } catch (Exception ex) {
                
                }
                dialog.setKeyItem(input);
                dialog.setTool(mKeyTool);
                int btn = dialog.open();
                if (btn < 0) {// delete
                    mKeyTool = dialog.getTool();
                    onStoreOpen(true);
                }
            }
        });
        
        loadConf();
    }
    
    private void loadConf() {
    
    }
    
    private void openStore() {
        String file = textStore.getText().trim();
        String pwd = textStorePass.getText();
        if (file.length() > 0 && pwd.length() > 0) {
            try {
                mKeyTool = new KeyTool(file, pwd);
            } catch (Exception e) {
                // e.printStackTrace();
                mKeyTool = null;
            }
        }
        else {
            mKeyTool = null;
        }
        onStoreOpen(mKeyTool != null);
    }
    
    public void onStoreOpen(boolean open) {
        btnNewAlias.setEnabled(open);
        btnViewAlias.setEnabled(open);
        if (open) {
            try {
                String[] aliasArray = mKeyTool.getAliases().toArray(new String[] {});
                comboAlias.setItems(aliasArray);
                if (aliasArray.length > 0) {
                    comboAlias.select(0);
                    onAliasChanged();
                }
            } catch (KeyStoreException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    private void openAlias() {
        int sel = comboAlias.getSelectionIndex();
        if (sel < 0) {
            return;
        }
        String alias = comboAlias.getItem(sel);
        String pwd = textAliasPass.getText();
        try {
            this.pubKey = mKeyTool.getCertificate(alias);
            this.privateKey = mKeyTool.getPrivateKey(alias, pwd);
            onAliasOpen(alias, true);
        } catch (Exception e) {
            // e.printStackTrace();
            this.pubKey = null;
            this.privateKey = null;
            onAliasOpen(alias, false);
        }
    }
    
    public void onAliasOpen(String alias, boolean open) {
        mAliasOpen = open;
        if (open) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] dest = md.digest(pubKey.getEncoded());
                lbMD5Finger.setText(KeyTool.bin2hex(dest));
                
                md = MessageDigest.getInstance("SHA1");
                dest = md.digest(pubKey.getEncoded());
                lbSHA1Finger.setText(KeyTool.bin2hex(dest));
                
                lbValidity.setText(
                        "From " + formatDate(pubKey.getNotBefore()) + " to " + formatDate(pubKey.getNotAfter()));
            } catch (Exception e) {
            
            }
        }
        else {
            lbMD5Finger.setText("");
            lbSHA1Finger.setText("");
            lbValidity.setText("");
        }
    }
    
    public String getKeystore() {
        return textStore.getText().trim();
    }
    
    public String getStorePass() {
        return textStorePass.getText();
    }
    
    public String getAlias() {
        return comboAlias.getText();
    }
    
    public String getAliasPass() {
        return textAliasPass.getText();
    }
    
    private boolean mAliasOpen = false;
    
    public boolean isAliasOpen() {
        return mAliasOpen;
    }
    
    public KeyTool getKeyTool() {
        return mKeyTool;
    }
    
    private X509Certificate pubKey;
    private PrivateKey privateKey;
    
    public X509Certificate getPubKey() {
        return pubKey;
    }
    
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    
    public String formatDate(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(d);
    }
    
    public void onStoreChanged() {
        String pwd = conf.getStorePass(getKeystore());
        if (!Utils.isEmpty(pwd)) {
            textStorePass.setText(pwd);
            chkStorePass.setSelection(true);
        }
        else {
            chkStorePass.setSelection(false);
        }
        openStore();
        setPageComplete(checkInput());
    }
    
    public void onStorePassChanged() {
        openStore();
        setPageComplete(checkInput());
    }
    
    public void onAliasChanged() {
        String pwd = conf.getAliasPass(getKeystore(), getAlias());
        if (!Utils.isEmpty(pwd)) {
            textAliasPass.setText(pwd);
            chkAliasPass.setSelection(true);
        }
        else {
            chkAliasPass.setSelection(false);
        }
        openAlias();
        setPageComplete(checkInput());
    }
    
    public void onAliasPassChanged() {
        openAlias();
        setPageComplete(checkInput());
    }
    
    public boolean checkInput() {
        if (Utils.isEmpty(textStore)) {
            setErrorMessage("Please select store file.");
            return false;
        }
        if (Utils.isEmpty(textStorePass)) {
            setErrorMessage("Please input store password.");
            return false;
        }
        if (mKeyTool == null) {
            setErrorMessage("Please input correct store password to open store.");
            return false;
        }
        if (Utils.isEmpty(comboAlias)) {
            setErrorMessage("No key entry (alias) exists, please create first.");
            return false;
        }
        
        if (Utils.isEmpty(textAliasPass)) {
            setErrorMessage("Please input key entry (alias) password.");
            return false;
        }
        
        if (!isAliasOpen()) {
            setErrorMessage("Can't open key entry (alias), please change the alias or password");
            return false;
        }
        
        setErrorMessage(null);
        return true;
    }
    
    private Conf conf;
    
    public void initConf(File file) {
        conf = Conf.parse(file);
        if (conf == null) {
            conf = new Conf();
        }
    }
    
    public void saveConf(File file) {
        conf.save(file);
    }
}
