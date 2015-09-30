/**
 * 
 */
package cn.ieclipse.pde.signer.test;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.ieclipse.pde.signer.wizard.SignApkWizard;

/**
 * @author Jamling
 *         
 */
public class SignApk {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        WizardDialog dialog = new WizardDialog(new Shell(), new SignApkWizard());
        dialog.open();
        Display.getCurrent().dispose();
        
    }
    
}
