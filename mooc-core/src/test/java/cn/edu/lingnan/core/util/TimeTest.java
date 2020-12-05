package cn.edu.lingnan.core.util;

import org.junit.jupiter.api.Test;

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


}
