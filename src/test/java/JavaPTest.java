import java.util.Arrays;

import	java.util.LinkedList;
/**
 * @Auther: hu.xiaohe
 * @Date: 2019/10/18 09:38
 * @Description:
 */
public class JavaPTest {
    public static void main(String[] args) {
        int[][] peoples = {{9,0},{7,0},{1,9},{3,0},{2,7},{5,3},{6,0},{3,4},{6,2},{5,2}};
        Arrays.sort(peoples,(a,b) ->a[0]!=b[0]?b[0]-a[0]:a[1]-b[1]);
        for(int[] ele:peoples){
            System.out.println(ele[0]+"--"+ele[1]);
        }
        LinkedList<int[]> sorts = new LinkedList < > ();
        for(int[] people:peoples) {
            sorts.add(people[1],people);
        }

        int[][] rs = sorts.toArray(peoples);
        System.out.println("--------------------------------");
        for(int[] ele:rs){
            System.out.println(ele[0]+"--"+ele[1]);
        }

    }
}
