package cn.ieclipse.pde.signer.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Main {
    
    protected Shell shell;
    
    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            Main window = new Main();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
    
    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        shell.setSize(450, 300);
        shell.setText("JarSigner");
        shell.setLayout(new GridLayout(2, false));
        
        Button btnJar = new Button(shell, SWT.NONE);
        btnJar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                SignJar.main(null);
            }
        });
        btnJar.setLayoutData(
                new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnJar.setText("Sign JAR");
        
        Label lblNewLabel_1 = new Label(shell, SWT.NONE);
        lblNewLabel_1.setText("Sign jar file");
        
        Button btnApk = new Button(shell, SWT.NONE);
        btnApk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                SignApk.main(null);
            }
        });
        btnApk.setLayoutData(
                new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnApk.setText("Sign APK");
        
        Label lblSignAndroidApk = new Label(shell, SWT.NONE);
        lblSignAndroidApk
                .setText("Sign android apk file or view the signature of apk");
                
        Button btnPlugin = new Button(shell, SWT.NONE);
        btnPlugin.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                SignPlugin.main(null);
            }
        });
        btnPlugin.setLayoutData(
                new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnPlugin.setText("Sign Plugin");
        
        Label lblSignEclipsePlugin = new Label(shell, SWT.NONE);
        lblSignEclipsePlugin.setText("Sign eclipse plugin project");
        
        Label lblThisIsDesktop = new Label(shell, SWT.NONE);
        lblThisIsDesktop.setLayoutData(
                new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
        lblThisIsDesktop.setText(
                "This is desktop version of cn.ieclipse.pde.signer. More info please see\r\n");
                
        Link link = new Link(shell, SWT.NONE);
        link.setLayoutData(
                new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        link.setText("<a>https://github.com/Jamling/jarsigner</a>");
        new Label(shell, SWT.NONE);
        new Label(shell, SWT.NONE);
        shell.pack();
        Rectangle bounds = Display.getDefault().getPrimaryMonitor().getBounds();
        Rectangle rect = shell.getBounds();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        shell.setLocation(x, y);
    }
    
}
