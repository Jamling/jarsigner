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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Jamling
 *         
 */
public class KeyTool {
    private String storeFile = "";
    private String storePass = "";
    private KeyStore store;
    
    public KeyTool(String file, String pass)
            throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
        this.storeFile = file;
        this.storePass = pass;
        
        FileInputStream in = new FileInputStream(storeFile);
        this.store = KeyStore.getInstance("JKS");
        this.store.load(in, storePass.toCharArray());
        in.close();
    }
    
    public KeyTool(String pass) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        this.storePass = pass;
        this.store = KeyStore.getInstance("JKS");
        this.store.load(null, storePass == null ? null : storePass.toCharArray());
    }
    
    public void save(String path, String password)
            throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
        FileOutputStream output = new FileOutputStream(path);
        if (password != null) {
            this.store.store(output, password.toCharArray());
        }
        else {
            this.store.store(output, this.storePass.toCharArray());
        }
        output.close();
    }
    
    public static Provider[] getProviders() {
        Provider[] ps = Security.getProviders();
        return ps;
    }
    
    public static X509Certificate readCertificate(String file) throws CertificateException, IOException {
        FileInputStream in = new FileInputStream(file);
        return readCertificate(in);
    }
    
    public static X509Certificate readCertificate(InputStream in) throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate c = cf.generateCertificate(in);
        in.close();
        if (c instanceof X509Certificate) {
            return (X509Certificate) c;
        }
        return null;
    }
    
    public void readKeystore() throws Exception {
    }
    
    public List<String> getAliases() throws KeyStoreException {
        List<String> list = new ArrayList<String>();
        Enumeration<String> aliases = store.aliases();
        while (aliases.hasMoreElements()) {
            list.add(aliases.nextElement());
        }
        return list;
    }
    
    public X509Certificate getCertificate(String alias) throws KeyStoreException {
        Certificate c = this.store.getCertificate(alias);
        if (c instanceof X509Certificate) {
            return (X509Certificate) c;
        }
        return null;
    }
    
    public long getValidity(String alias) throws Exception {
        X509Certificate c = getCertificate(alias);
        Date d1 = ((X509Certificate) c).getNotBefore();
        Date d2 = ((X509Certificate) c).getNotAfter();
        long t = (d2.getTime() - d1.getTime());
        return t / (24 * 3600000);
    }
    
    public String getIssuer(String alias) throws Exception {
        Certificate c = getCertificate(alias);
        if (c instanceof X509Certificate) {
            return ((X509Certificate) c).getIssuerDN().getName();
        }
        throw new IllegalAccessException("Not x509 format certificate.");
    }
    
    public Key getKey(String alias, String password)
            throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
        return store.getKey(alias, password.toCharArray());
    }
    
    public Entry getEntry(String alias, String password)
            throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
        KeyStore.PasswordProtection p = new KeyStore.PasswordProtection(password.toCharArray());
        Entry e = this.store.getEntry(alias, p);
        return e;
    }
    
    public void deleteEntry(String alias) throws KeyStoreException {
        this.store.deleteEntry(alias);
    }
    
    public PrivateKey getPrivateKey(String alias, String password) throws Exception {
        Entry e = getEntry(alias, password);
        if (e instanceof PrivateKeyEntry) {
            return ((PrivateKeyEntry) e).getPrivateKey();
        }
        return null;
    }
    
    public KeyPair getKeyPair(String alias, String password)
            throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
        Key key = getKey(alias, password);
        if (key instanceof PrivateKey) {
            PublicKey pubKey = getPublicKey(alias);
            return new KeyPair(pubKey, (PrivateKey) key);
        }
        return null;
    }
    
    public PublicKey getPublicKey(String alias) throws KeyStoreException {
        Certificate c = getCertificate(alias);
        return c.getPublicKey();
    }
    
    public void exportCert(String alias, String password, String path)
            throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, IOException {
        KeyPair pair = getKeyPair(alias, password);
        if (pair != null) {
            try {
                ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(path));
                fos.writeObject(pair.getPrivate());
                fos.writeObject(getCertificate(alias));
                fos.close();
            } finally {
            
            }
        }
    }
    
    public KeyStore getStore() {
        return store;
    }
    
    public String getStoreFile() {
        return storeFile;
    }
    
    public String getStorePass() {
        return storePass;
    }
    
    /*
     * 
     * MD5 : DE:76:16:F8:D1:E3:41:8E:CF:C9:E2:7D:9A:FA:BF:5C
     * SHA1:50:DE:B1:DF:F3:D9:DA:D0:53:3E:8B:4C:D5:BD:16:6F:EC:ED:2F:5F
     */
    public static String bin2hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length << 1);
        for (byte b : bytes) {
            sb.append(Integer.toHexString(b >> 4 & 0x0f));
            sb.append(Integer.toHexString(b & 0x0f));
            sb.append(":");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
}
