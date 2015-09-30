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
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;

import cn.ieclipse.pde.signer.util.BcpSigner;
import cn.ieclipse.pde.signer.util.JarSigner;
import cn.ieclipse.pde.signer.util.KeyTool;
import cn.ieclipse.pde.signer.util.ProcessUtil;

/**
 * Common sign wizard, provide PCBSigner
 * 
 * @author Jamling
 *         
 */
public abstract class CommonSignWizard extends Wizard {
    protected CommonSignPage page1;
    protected File cfgFile;
    
    protected void initCfgFile() {
        cfgFile = new File("conf.cfg");
    }
    
    public CommonSignWizard(String title) {
        setWindowTitle(title);
        initCfgFile();
        page1 = new CommonSignPage();
        page1.initConf(cfgFile);
    }
    
    @Override
    public boolean performFinish() {
        try {
            IRunnableWithProgress runnable = new IRunnableWithProgress() {
                
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    sign(monitor);
                }
            };
            getContainer().run(false, true, runnable);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    protected abstract void sign(IProgressMonitor monitor);
    
    protected String pcbSign(String input, String output, String cert) {
        String alias = page1.getAlias();
        String msg = BcpSigner.sign(page1.getPubKey(), page1.getPrivateKey(), input, output,
                cert == null ? alias.toUpperCase() : cert);
        return msg;
    }
    
    protected void jarSign(String input, String output, String cert) throws Exception {
        KeyTool tool = page1.getKeyTool();
        String alias = page1.getAlias();
        String pwd = page1.getAliasPass();
        boolean ret = JarSigner.sign(input, output, tool.getStoreFile(), tool.getStorePass(), alias, pwd,
                new ProcessUtil.Callback() {
                    
                    public void onError(String msg) {
                        System.out.println("error:" + msg);
                        page1.setErrorMessage(msg);
                    }
                    
                    public void onCompleted(String msg) {
                        System.out.println("output:" + msg);
                        page1.setErrorMessage(msg);
                    }
                });
        System.out.println("sign ? " + ret);
    }
    
    @Override
    public boolean canFinish() {
        return getContainer().getCurrentPage() == page1 && super.canFinish();
    }
}
