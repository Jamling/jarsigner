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

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * Key entry java bean.
 * 
 * @author Jamling
 *         
 */
public class KeyAlias implements Serializable {
    private static final long serialVersionUID = -7997764494358932095L;
    
    private String alias;
    private long validity;
    private String password;
    
    private String cn;
    private String ou;
    private String o;
    private String l;
    private String s;
    private String c;
    
    private String issuer;
    
    public void setIssuer(String issuer) {
        this.issuer = issuer;
        StringTokenizer st = new StringTokenizer(issuer, ",");
        while (st.hasMoreElements()) {
            String item = (String) st.nextElement();
            item = item.trim();
            if (item.startsWith("CN=")) {
                cn = item.substring(3).trim();
            }
            else if (item.startsWith("OU=")) {
                ou = item.substring(3).trim();
            }
            else if (item.startsWith("O=")) {
                o = item.substring(2).trim();
            }
            else if (item.startsWith("L=")) {
                l = item.substring(2).trim();
            }
            else if (item.startsWith("S=")) {
                s = item.substring(2).trim();
            }
            else if (item.startsWith("ST=")) {
                s = item.substring(3).trim();
            }
            else if (item.startsWith("C=")) {
                c = item.substring(2).trim();
            }
        }
    }
    
    public String getIssuer() {
        StringBuilder sb = new StringBuilder();
        sb.append("CN=");
        sb.append(cn == null ? "" : cn);
        sb.append(", OU=");
        sb.append(ou == null ? "" : ou);
        sb.append(", O=");
        sb.append(o == null ? "" : o);
        sb.append(", L=");
        sb.append(l == null ? "" : l);
        sb.append(", S=");
        sb.append(s == null ? "" : s);
        sb.append(", C=");
        sb.append(c == null ? "" : c);
        this.issuer = sb.toString();
        return this.issuer;
    }
    
    public long getValidity() {
        return validity;
    }
    
    public void setValidity(long validity) {
        this.validity = validity;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getCn() {
        return cn;
    }
    
    public void setCn(String cn) {
        this.cn = cn;
    }
    
    public String getOu() {
        return ou;
    }
    
    public void setOu(String ou) {
        this.ou = ou;
    }
    
    public String getO() {
        return o;
    }
    
    public void setO(String o) {
        this.o = o;
    }
    
    public String getL() {
        return l;
    }
    
    public void setL(String l) {
        this.l = l;
    }
    
    public String getS() {
        return s;
    }
    
    public void setS(String s) {
        this.s = s;
    }
    
    public String getC() {
        return c;
    }
    
    public void setC(String c) {
        this.c = c;
    }
    
    public static void main(String[] args) {
        KeyAlias key = new KeyAlias();
        key.setIssuer("CN=Jamling Li, OU=ieclipse.cn, O=ieclipse.cn, L=Nanjing, ST=JiangSu, C=CN");
        System.out.println(key.getIssuer());
    }
}
