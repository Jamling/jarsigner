/**
 * 
 */
package cn.ieclipse.pde.signer.test;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.ieclipse.pde.signer.wizard.SignJarWizard;

/**
 * @author Jamling
 *         
 */
public class SignJar {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        WizardDialog dialog = new WizardDialog(new Shell(), new SignJarWizard("Test sign jar"));
        dialog.open();
        Display.getCurrent().dispose();
    }
    
}
