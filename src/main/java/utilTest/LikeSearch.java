package utilTest;

import	java.util.concurrent.ConcurrentHashMap;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Auther: hu.xiaohe
 * @Date: 2019/10/21 09:36
 * @Description:
 */
public class LikeSearch<T> {
    private final CharColum<T>[] columns = new CharColum[Character.MAX_VALUE];

    public CharColum<T>[] getColumns(){
        return columns;
    }

    private final ReentrantReadWriteLock  rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
    
    public void put(T t,String value){
        char[] chars = value.toCharArray();
        for (int i=0;i<chars.length; i++){
            char c = chars[i];
            CharColum<T> column = columns [c];
            if(column == null){
                column = new CharColum<T>();
                columns [c] = column;
            }
            column.add(t,(byte) i);
        }
    }
    
    public void update(T id,String newValue){
        remove(id);
        put(id,newValue);
        
    }

    private boolean remove(T id) {
        boolean sign = false;
        for(CharColum<T> column : columns){
            if(column !=null){
                if(column.remove(id)){
                    sign = true;
                }
            }
        }
        return sign;
    }

    public Collection<T> search(String word,int limit){
        char[] chars = word.toCharArray();
        int n = word.length();
        Context context = new Context();
        for (int i=0;i<chars.length; i++){
            CharColum<T> column = columns [chars[i]];
            if(column == null){
                break;
            }
            if(!context.filter(column)){
                break;
            }
            n--;
        }
        if(n==0){
            return  context.limit(limit);
        }
        return Collections.emptySet();
    }

    private class Context{
        Map<T,byte[]> result;
        boolean used = false;
        private boolean filter(CharColum<T> columns){
            if(this.used == false){
                this.result = new TreeMap<T,byte[]>(columns.poxIndex);
                this.used = true;
                return used;
            }
            boolean flag = false;
            Map<T,byte[]> newResult = new TreeMap < T, byte [] > ();
            Set<Map.Entry < T, byte []>> entrySet = columns.poxIndex.entrySet();
            for (Map.Entry<T,byte[]> entry:entrySet){
                T id = entry.getKey();
                byte[] charPox = entry.getValue();
                if(!result.containsKey(id)){
                    continue;
                }
                byte[] before = result.get(id);
                boolean in = false;
                for(byte pox:before){
                    if(contain(charPox,(byte)(pox+1))){
                        in = true;
                        break;
                    }
                }
                if(in){
                    flag = true;
                    newResult.put(id,charPox);
                }
            }
            result = newResult;
            return flag;
        }

        public boolean contain(byte[] charPox,byte target){
            boolean rs = false;
            for(byte num:charPox){
                if(num == target){
                    rs = true;
                }
            }
            return rs;
        }

        public Collection<T> limit(int limit){
            if(result == null){
                return null;
            }
            if(result.size()<=limit){
                return result.keySet();
            }
            Collection<T> ids = new TreeSet<>();
            for(T id:result.keySet()){
                ids.add(id);
                if(ids.size() >=limit){
                    break;
                }
            }
            return ids;
        }
    }

    public static class CharColum<T> {
        ConcurrentHashMap < T,byte[]> poxIndex = new ConcurrentHashMap<>();
        public void add(T t, byte pox) {
            byte[] arr = poxIndex.get(t);
            if(arr == null){
                arr = new byte []{pox};
            }else{
                arr = copy(arr,pox);
            }
            poxIndex.put(t,arr);
        }

        public byte[] copy(byte[] arr,byte pox){
            byte[] newArr = new byte[arr.length+1];
            newArr [newArr.length-1] = pox;
            System.arraycopy(arr,0,newArr,0, arr.length);
            return newArr;
        }

        public boolean remove(T id) {
            if(poxIndex.remove(id) !=null){
                return true;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        LikeSearch<String> likeSearch = new LikeSearch<>();
        likeSearch.put("想湖北","想湖北");
        likeSearch.put("想湖北理想","想湖北理想");
        likeSearch.put("湖南新华","湖南");
        likeSearch.put("河北","河北");
        likeSearch.put("河南","河南");
        likeSearch.put("北京","北京");
        likeSearch.put("南京","南京");
        Collection<String> search = likeSearch.search("想湖北", 10);
        search.forEach(ele -> System.out.println(ele));
    }

}
