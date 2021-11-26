package cn.edu.lingnan.mooc.core.util;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author xmz
 * @date: 2020/11/30
 */
public class TimeTest {

    @Test
    public void test() throws ParseException {
        String time="2020-11-03";
        Date date=null;
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        date=formatter.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        calendar.add(Calendar.SECOND,-1);
        System.out.println(calendar.getTime());
    }


    public static void main(String[] args) throws ParseException {
        String dataStr = "2021/04/19 00:00:00";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = dateFormat.parse(dataStr);
        System.out.println(date);

    }


}
