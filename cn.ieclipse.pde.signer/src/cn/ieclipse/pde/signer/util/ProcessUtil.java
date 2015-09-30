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
package cn.ieclipse.pde.signer.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jamling
 *         
 */
public class ProcessUtil {
    
    public static Process exec(String program, List<String> args) {
        ProcessBuilder builder = new ProcessBuilder();
        File f = new File(program);
        if (f != null && f.getParentFile() != null && f.getParentFile().exists()) {
            builder.directory(f.getParentFile());
        }
        
        String cmd = String.format("\"%s\"", f.getName());
        // String cmd = f.getName();
        if (args != null) {
            args.add(0, cmd);
            builder.command(args);
        }
        else {
            builder.command(cmd);
        }
        System.out.println(builder.command());
        try {
            Process p = builder.start();
            return p;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                System.out.println(new String(e.toString().getBytes("utf-8"), "gbk"));
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return null;
        }
    }
    
    public static Process exec(String program, String... args) {
        if (args != null) {
            List<String> array = new ArrayList<String>(args.length);
            for (int i = 0; i < args.length; i++) {
                array.add(args[i]);
            }
            return exec(program, array);
        }
        return exec(program, (List<String>) null);
    }
    
    public static Process exec(String program) {
        return exec(program, (List<String>) null);
    }
    
    public static void dumpProcess(Process p, String encoding) {
        int len = -1;
        byte[] buf = new byte[8192];
        try {
            while ((len = p.getErrorStream().read(buf)) >= 0) {
                System.out.println(new String(buf, 0, len, encoding));
            }
            
            while ((len = p.getInputStream().read(buf)) >= 0) {
                System.out.println(new String(buf, 0, len, encoding));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void dumpProcess(Process p, String encoding, Callback callback) {
        int len = -1;
        byte[] buf = new byte[8192];
        StringBuilder sb = new StringBuilder();
        try {
            while ((len = p.getErrorStream().read(buf)) >= 0) {
                sb.append(new String(buf, 0, len, encoding));
            }
            
            if (callback != null && sb.length() > 0) {
                callback.onError(sb.toString());
            }
            sb.delete(0, sb.length());
            
            while ((len = p.getInputStream().read(buf)) >= 0) {
                sb.append(new String(buf, 0, len, encoding));
            }
            if (callback != null && sb.length() > 0) {
                callback.onCompleted(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static interface Callback {
        void onError(String msg);
        
        void onCompleted(String msg);
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        ArrayList<String> aa = new ArrayList<String>();
        aa.add("-genkey");
        aa.add("-keystore");
        aa.add(String.format("\"%s\"", "C:\\Program Files\\test.key"));
        aa.add("-storepass");
        aa.add(String.format("\"%s\"", "a  b  "));
        aa.add("-validity");
        aa.add("365");
        aa.add("-alias");
        aa.add("alias_test");
        aa.add("-keypass");
        aa.add("bbbbbb");
        aa.add("-dname");
        aa.add("CN=a, OU=ou, O=u, L=naning, ST=jiangsu, C=cn");
        
        // exec2("C:\\Program Files\\Java\\jre6\\bin\\keytool", aa);
        Process p = exec("cmd");
        dumpProcess(p, "gbk");
    }
}
