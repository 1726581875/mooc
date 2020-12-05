package cn.edu.lingnan.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author xmz
 * @date: 2020/12/01
 */
public class ConvertTimeUtil {

    private static final Logger log = LoggerFactory.getLogger(ConvertTimeUtil.class);

    public static Date getEndTime(String endTime){
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        Date date= null;
        try {
            date = formatter.parse(endTime);
        } catch (ParseException e) {
            log.error("转换时间发生异常",e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        calendar.add(Calendar.SECOND,-1);
        return calendar.getTime();
    }

    public static Date getTime(String timeStr){
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        Date date= null;
        try {
            date = formatter.parse(timeStr);
        } catch (ParseException e) {
            log.error("转换时间发生异常",e);
        }
        return date;
    }




}
