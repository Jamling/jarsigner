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
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;

/**
 * @author Jamling
 *         
 */
public final class JarSigner {
    
    public static boolean sign(String src, String dest, String store, String storePwd, String alias, String aliasPwd,
            ProcessUtil.Callback callback) throws Exception {
        String cmd = "jarsigner";
        List<String> args = new ArrayList<String>();
        args.add("-keystore");
        args.add(String.format("\"%s\"", store));
        args.add("-storepass");
        args.add(String.format("\"%s\"", storePwd));
        args.add("-keypass");
        args.add(String.format("\"%s\"", aliasPwd));
        if (dest != null && dest.length() > 0) {
            args.add("-signedjar");
            args.add(String.format("\"%s\"", dest));
        }
        args.add(String.format("\"%s\"", src));
        args.add(String.format("\"%s\"", alias));
        Process p = ProcessUtil.exec(cmd, args);
        if (p == null) {
            if (callback != null) {
                callback.onError("Sign fail, please check your java enviroment.");
            }
            return false;
        }
        ProcessUtil.dumpProcess(p, "gbk", callback);
        int code = p.exitValue();
        return code == 0;
    }
    
    public static boolean removeMetaInf(String src) throws Exception {
        ZipFile zipFile = new ZipFile(src);
        System.out.println("valid ? " + zipFile.isValidZipFile());
        FileHeader metainf = zipFile.getFileHeader("META-INF");
        if (metainf != null) {
            zipFile.removeFile(metainf);
            return true;
        }
        return false;
    }
    
    public static boolean optApk(String src, String dest, int align, boolean c, boolean f, boolean z) {
        String cmd = new File("tool/zipalign").getAbsolutePath();
        List<String> args = new ArrayList<String>();
        if (c) {
            args.add("-c");
        }
        if (f) {
            args.add("-f");
        }
        if (z) {
            args.add("-z");
        }
        args.add(String.valueOf(align));
        args.add(String.format("\"%s\"", src));
        args.add(String.format("\"%s\"", dest));
        Process p = ProcessUtil.exec(cmd, args);
        ProcessUtil.dumpProcess(p, "gbk");
        int code = p.exitValue();
        return code == 0;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String src = "C:\\HelloAndroid.apk";
        String dst = "C:\\HelloAndroid_signed.apk";
        String opt = "C:\\HelloAndroid_opt.apk";
        // removeMetaInf(src);
        // sign(src, dst, "C:\\ieclipse.keystore", "storepass", "pde_alias",
        // "keypass");
        // optApk(src, opt, 4, false, true, true);
        System.out.println(System.getProperty("file.encoding"));
    }
}
