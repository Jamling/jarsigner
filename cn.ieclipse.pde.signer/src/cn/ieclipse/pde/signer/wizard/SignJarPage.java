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
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.jface.dialogs.IMessageProvider;
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

import cn.ieclipse.pde.signer.util.BcpSigner;
import cn.ieclipse.pde.signer.util.KeyTool;
import cn.ieclipse.pde.signer.util.Utils;

/**
 * Sign jar page.
 * 
 * @author Jamling
 *         
 */
public class SignJarPage extends WizardPage {
    private String[] srcFilter = { "*.jar", "*.*" };
    private String srcFile;
    private Text textSrc;
    private Text textDst;
    private Text textSignature;
    
    /**
     * Create the wizard.
     */
    public SignJarPage() {
        super(SignJarPage.class.getName());
        setTitle("Sign Jar");
        setDescription("Sign jar");
    }
    
    public void setSrcFilter(String[] srcFilter) {
        this.srcFilter = srcFilter;
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
        lblNewLabel.setLayoutData(
                new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("Source package:");
        
        textSrc = new Text(container, SWT.BORDER);
        textSrc.setLayoutData(
                new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        if (srcFile != null) {
            textSrc.setText(srcFile);
        }
        
        Button btnOpen = new Button(container, SWT.NONE);
        GridData gd_btnOpen = new GridData(SWT.FILL, SWT.CENTER, false, false,
                1, 1);
        gd_btnOpen.widthHint = 75;
        btnOpen.setLayoutData(gd_btnOpen);
        btnOpen.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
                dialog.setFilterExtensions(srcFilter);
                String path = dialog.open();
                if (path != null && path.length() > 0) {
                    textSrc.setText(path);
                }
            }
        });
        btnOpen.setText("&Open");
        
        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setLayoutData(
                new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_1.setText("Dest package:");
        
        textDst = new Text(container, SWT.BORDER);
        textDst.setLayoutData(
                new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                
        Button btnSave = new Button(container, SWT.NONE);
        btnSave.setLayoutData(
                new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnSave.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
                String path = dialog.open();
                if (path != null && path.length() > 0) {
                    textDst.setText(path);
                }
            }
        });
        btnSave.setText("&Save");
        
        Label lblNewLabel_2 = new Label(container, SWT.NONE);
        lblNewLabel_2.setLayoutData(
                new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_2.setText("Old signatures:");
        
        textSignature = new Text(container,
                SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI );
        textSignature.setEditable(false);
        textSignature.setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        textSrc.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                onSourcePackageChanged();
            }
        });
        
        textDst.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                onDestPackageChanged();
            }
        });
        
    }
    
    protected void onSourcePackageChanged() {
        String path = getSourcePackage();
        int pos = path.lastIndexOf(".");
        if (pos >= 0 && Utils.isEmpty(textDst)) {
            String dst = path.substring(0, pos) + "_signed"
                    + path.substring(pos);
            textDst.setText(dst);
        }
        setPageComplete(check());
        showCert(path);
    }
    
    private void showCert(String file) {
        JarFile jar = null;
        try {
            jar = new JarFile(file, true);
        } catch (Exception e) {
            // e.printStackTrace();
            textSignature
                    .setText("Can't read signature info from source package");
            return;
        } finally {
            if (jar != null) {
                // JDK issue, jdk 8 fix this.
                // try {
                // jar.close();
                // } catch (IOException e) {
                //
                // }
            }
        }
        JarEntry je = null;
        // je =
        // jar.getJarEntry("res/drawable-mdpi/eposode_list_item_focsed.png");
        if (je == null) {
            for (Enumeration<JarEntry> e = jar.entries(); e
                    .hasMoreElements();) {
                JarEntry entry = e.nextElement();
                if (!BcpSigner.stripPattern.matcher(entry.getName())
                        .matches()) {
                    je = entry;
                    break;
                }
            }
        }
        // System.out.println(je);
        
        if (je != null) {
            try {
                byte[] buf = new byte[4096];
                // int len = 0;
                InputStream is = jar.getInputStream(je);
                while (is.read(buf) != -1) {
                
                }
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            Certificate[] cs = je.getCertificates();
            if (cs != null) {
                StringBuilder sb = new StringBuilder();
                int i = 0;
                MessageDigest md = null;
                for (Certificate c : cs) {
                    i++;
                    sb.append(
                            String.format("====== Certificate %d ======\n", i));
                    if (c instanceof X509Certificate) {
                        X509Certificate x = (X509Certificate) c;
                        sb.append(String.format("Issuer: %s\n",
                                x.getIssuerDN().getName()));
                    }
                    try {
                        md = MessageDigest.getInstance("md5");
                        sb.append(String.format("MD5 finger: %s\n",
                                KeyTool.bin2hex(md.digest(c.getEncoded()))));
                        md = MessageDigest.getInstance("sha1");
                        sb.append(String.format("SHA1 finger: %s",
                                KeyTool.bin2hex(md.digest(c.getEncoded()))));
                    } catch (Exception e) {
                    
                    }
                }
                textSignature.setText(sb.toString());
            }
            else {
                textSignature.setText(
                        "Can't read signature info from source package or there is no signature info in source package");
            }
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                
                }
            }
        }
        
    }
    
    protected void onDestPackageChanged() {
        if (getDestPackage().length() == 0
                || getDestPackage().equals(getSourcePackage())) {
            setMessage(
                    "The dest package is same as source package or none, it will overwrite source package",
                    IMessageProvider.WARNING);
        }
        else {
            setMessage(null);
        }
    }
    
    public void setSourcePackage(String file) {
        if (file != null) {
            srcFile = file;
        }
    }
    
    public String getSourcePackage() {
        return textSrc.getText().trim();
    }
    
    public String getDestPackage() {
        return textDst.getText().trim();
    }
    
    private boolean check() {
        if (Utils.isEmpty(getSourcePackage())) {
            setErrorMessage("Please select source package to sign.");
            return false;
        }
        
        if (!new File(getSourcePackage()).exists()
                || new File(getSourcePackage()).isDirectory()) {
            setErrorMessage("Invalid source package.");
            return false;
        }
        onDestPackageChanged();
        setErrorMessage(null);
        return true;
    }
    
    @Override
    public boolean isPageComplete() {
        return super.isPageComplete() && check();
    }
    
    @Override
    public boolean canFlipToNextPage() {
        return super.canFlipToNextPage() && check();
    }
}
