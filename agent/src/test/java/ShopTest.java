import com.jonex.galaxy.agent.Shop;

/**
 * <pre>
 *
 *  File: ShopTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/6/13				lijunjun				Initial.
 *
 * </pre>
 */
public class ShopTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(new Shop().getProductPrice("book"));
        System.out.println(new Shop().getProductPrice("book2"));
        int count = 0;
        while (true) {
            Thread.sleep(500);
            count++;
            double price = new Shop().getProductPrice("book2");
            System.out.println(price);
            if (3 == price || count >= 10) {
                break;
            }
        }
    }

}
