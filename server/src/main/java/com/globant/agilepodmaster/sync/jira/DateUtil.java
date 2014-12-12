package com.globant.agilepodmaster.sync.jira;

import com.globant.agilepodmaster.sync.SyncContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Util to get the date format pattern by asking help to regex.
 * 
 * @author jose.dominguez@globant.com
 */
public final class DateUtil {
  
  /** Private Constructor.*/
  private DateUtil() {}


  private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {
    {
      put("^\\d{8}$", "yyyyMMdd");
      put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
      put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
      put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
      put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
      put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
      put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
      put("^\\d{12}$", "yyyyMMddHHmm");
      put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
      put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
      put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
      put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
      put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
      put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$",
          "dd MMM yyyy HH:mm");
      put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$",
          "dd MMMM yyyy HH:mm");
      put("^\\d{14}$", "yyyyMMddHHmmss");
      put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
      put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$",
          "dd-MM-yyyy HH:mm:ss");
      put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$",
          "yyyy-MM-dd HH:mm:ss");
      put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$",
          "MM/dd/yyyy HH:mm:ss");
      put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$",
          "yyyy/MM/dd HH:mm:ss");
      put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$",
          "dd MMM yyyy HH:mm:ss");
      put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$",
          "dd MMMM yyyy HH:mm:ss");
      put("^\\d{1,2}/[a-z]{3}/\\d{2}\\s\\d{1,2}:\\d{2}\\s[a-z]{2}$",
          "dd/MMM/yy HH:mm");
      put("^\\d{4}-\\d{2}-\\d{1,2}\\w{1}\\d{2}:\\d{2}:\\d{2}.*$",
          "yyyy-MM-dd'T'HH:mm:ss");
    }
  };

  /**
   * Determine SimpleDateFormat pattern matching with the given date string.
   * Returns null if format is unknown. You can simply extend DateUtil with more
   * formats if needed.
   * 
   * @param dateString The date string to determine the SimpleDateFormat pattern
   *        for.
   * @return The matching SimpleDateFormat pattern, or null if format is
   *         unknown.
   * @see SimpleDateFormat
   */
  public static String determineDateFormat(String dateString) {
    for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
      if (dateString.toLowerCase().matches(regexp)) {
        return DATE_FORMAT_REGEXPS.get(regexp);
      }
    }
    return null; // Unknown format.
  }
  
  /**
   * Convert a String to date from any date formats defined in this class.
   * @param theDate the date to be converted
   * @param context context where to log conversion errors. 
   * @return the date
   */
  public static Date getDate(final String theDate, SyncContext context) {

    String dateFormat = DateUtil.determineDateFormat(theDate);

    Date date = null;
    if (dateFormat != null) {
      try {
        date = new SimpleDateFormat(dateFormat).parse(theDate);
      } catch (ParseException e) {
        context.error("Parse exception for date:" + theDate);
      }
    } else {
      context.error("Invalid date format:" + theDate);
    }
    return date;
  }
  
}
