/**
 * 
 */
package cn.ieclipse.pde.signer.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jamling
 *         
 */
public class Conf implements java.io.Serializable {
    private static final long serialVersionUID = -3159509826104717684L;
    private List<StoreItem> storeList = new ArrayList<Conf.StoreItem>();
    
    public List<String> getStoreList() {
        ArrayList<String> ret = new ArrayList<String>(storeList.size());
        for (StoreItem item : storeList) {
            ret.add(item.name);
        }
        return ret;
    }
    
    private StoreItem find(String store) {
        StoreItem ret = null;
        for (StoreItem item : storeList) {
            if (item.name.equals(store)) {
                ret = item;
                break;
            }
        }
        if (ret == null) {
            ret = new StoreItem();
            ret.setName(store);
            storeList.add(ret);
        }
        return ret;
    }
    
    public String getStorePass(String store) {
        return find(store).getPass();
    }
    
    public String getAliasPass(String store, String alias) {
        return find(store).getKeyPass(alias);
    }
    
    public void setStorePass(String store, String pass) {
        find(store).setPass(pass);
    }
    
    public void setAliasPass(String store, String key, String pass) {
        find(store).addKey(key, pass);
    }
    
    private static class StoreItem implements java.io.Serializable {
        private static final long serialVersionUID = -5340812444799590132L;
        private String name;
        private String pass;
        private HashMap<String, String> keyMap = new HashMap<String, String>();
        
        public void addKey(String key, String pass) {
            keyMap.put(key, pass);
        }
        
        public String getKeyPass(String key) {
            return keyMap.get(key);
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public void setPass(String pass) {
            this.pass = pass;
        }
        
        public String getPass() {
            return this.pass;
        }
    }
    
    public static Conf parse(File f) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Conf conf = (Conf) ois.readObject();
            ois.close();
            return conf;
        } catch (Exception e) {
            return null;
        }
    }
    
    public void save(File f) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(this);
            oos.flush();
            oos.close();
        } catch (Exception e) {
        }
    }
}
