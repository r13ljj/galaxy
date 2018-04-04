package disruptor;

import java.util.List;

/**
 * <pre>
 *
 *  File: ActionTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/4/4				lijunjun				Initial.
 *
 * </pre>
 */
public class ActionTest implements Runnable{

    //For paging
    private int total = -1;
    private int page = 1;
    private int size = 1;

    public ActionTest(){
        total = 1000;
        size = 10;
    }

    @Override
    public void run()
    {
        int i = 0;
        do
        {
            addPage();
            i++;
            System.out.println(i+" page:"+page);
        }while(hasMorePage());
    }

    public void addPage()
    {
        this.page ++;
    }

    public boolean hasMorePage()
    {
        long pageCount = (total/size) + (total%size == 0?0:1);
        return page + 1 <= pageCount;
    }

    public static void main(String[] args)throws Exception {
        ActionTest test = new ActionTest();
        for(int i=0; i<1000; i++){
            Thread t = new Thread(test);
            t.start();
        }
    }
}
