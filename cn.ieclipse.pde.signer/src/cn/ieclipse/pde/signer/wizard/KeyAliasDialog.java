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

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import cn.ieclipse.pde.signer.util.KeyTool;
import cn.ieclipse.pde.signer.util.ProcessUtil;

/**
 * Key entry manage dialog.
 * 
 * @author Jamling
 *         
 */
public class KeyAliasDialog extends TitleAreaDialog {
    
    private KeyAliasComposite composite;
    private KeyAlias item;
    private KeyTool tool;
    private boolean editMode = false;
    
    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public KeyAliasDialog(Shell parentShell) {
        super(parentShell);
    }
    
    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayout(new FillLayout(SWT.VERTICAL));
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        setTitle("Alias");
        setMessage("Create/View/Delete alias in keystore.");
        
        composite = new KeyAliasComposite(container, SWT.NONE);
        if (this.item != null) {
            composite.setKeyItem(item);
        }
        composite.setEditMode(editMode);
        composite.setValidationListener(new ValidationListener() {
            public void onValidate(String msg) {
                setErrorMessage(msg);
                getButton(IDialogConstants.OK_ID).setEnabled(getErrorMessage() == null);
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
        if (this.item != null) {
            createButton(parent, IDialogConstants.RETRY_ID, "&Delete", false);
            createButton(parent, IDialogConstants.IGNORE_ID, "&Export", true);
        }
        else {
            createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        }
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }
    
    @Override
    protected void buttonPressed(int buttonId) {
        if (IDialogConstants.RETRY_ID == buttonId) {// delete
            if (delKey()) {
                setReturnCode(-1);
                close();
            }
        }
        else if (IDialogConstants.IGNORE_ID == buttonId) {// export
            exportKey();
        }
        super.buttonPressed(buttonId);
    }
    
    @Override
    protected void okPressed() {
        item = composite.getKeyItem();
        if (genKey()) {
            super.okPressed();
        }
    }
    
    public KeyAlias getKeyItem() {
        return item;
    }
    
    public void setKeyItem(KeyAlias item) {
        this.item = item;
    }
    
    public void setTool(KeyTool tool) {
        this.tool = tool;
    }
    
    public KeyTool getTool() {
        return tool;
    }
    
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
    
    private boolean genKey() {
        ArrayList<String> aa = new ArrayList<String>();
        aa.add("-genkey");
        aa.add("-keystore");
        aa.add(String.format("\"%s\"", tool.getStoreFile()));
        aa.add("-storepass");
        aa.add(String.format("\"%s\"", tool.getStorePass()));
        aa.add("-validity");
        aa.add(String.valueOf(item.getValidity()));
        aa.add("-alias");
        aa.add(String.format("\"%s\"", item.getAlias()));
        aa.add("-keypass");
        aa.add(String.format("\"%s\"", item.getPassword()));
        aa.add("-keyalg");
        aa.add("RSA");
        aa.add("-dname");
        aa.add(String.format("\"%s\"", item.getIssuer()));
        Process p = ProcessUtil.exec(getKeyToolPath(), aa);
        if (p != null) {
            try {
                ProcessUtil.dumpProcess(p, "gbk");
                System.out.println(p.exitValue());
                tool = new KeyTool(tool.getStoreFile(), tool.getStorePass());
                if (tool.getAliases().contains(item.getAlias().toLowerCase())) {
                    return true;
                }
            } catch (Exception e) {
            
            }
        }
        return false;
    }
    
    private boolean exportKey() {
        FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
        dialog.setFileName(item.getAlias());
        dialog.setFilterExtensions(new String[] { "*.cer", "*.*" });
        String path = dialog.open();
        if (path != null) {
            ArrayList<String> aa = new ArrayList<String>();
            aa.add("-exportcert");
            aa.add("-keystore");
            aa.add(String.format("\"%s\"", tool.getStoreFile()));
            aa.add("-storepass");
            aa.add(String.format("\"%s\"", tool.getStorePass()));
            aa.add("-alias");
            aa.add(String.format("\"%s\"", item.getAlias()));
            aa.add("-file");
            aa.add(String.format("\"%s\"", path));
            Process p = ProcessUtil.exec(getKeyToolPath(), aa);
            if (p != null) {
                ProcessUtil.dumpProcess(p, "gbk");
                if (p.exitValue() == 0) {
                    MessageDialog.openConfirm(getShell(), "Export Successfully!", "Export Successfully!");
                }
            }
        }
        return false;
    }
    
    private boolean delKey() {
        try {
            tool.deleteEntry(composite.getKeyItem().getAlias());
            tool.save(tool.getStoreFile(), tool.getStorePass());
            return true;
        } catch (Exception e) {
        
        }
        return false;
    }
    
    public static String getKeyToolPath() {
        String path = System.getenv("java_home");
        if (path == null) {
            path = System.getProperty("java.home");
            // if (path != null) {
            // File f = new File(path);
            // while (f.getParentFile() != null) {
            // System.out.println(f);
            // if (new File(f, "bin/keytool.exe").exists()) {
            // path = f.getAbsolutePath();
            // break;
            // }
            // f = f.getParentFile();
            // }
            // }
        }
        if (path != null) {
            path = "keytool";
        }
        return path;
    }
    
    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(450, 450);
    }
}
