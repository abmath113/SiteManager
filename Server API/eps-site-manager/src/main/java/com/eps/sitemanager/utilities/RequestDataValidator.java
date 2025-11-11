package com.eps.sitemanager.utilities;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;


@Component	
public class RequestDataValidator {

	   private static final String ALPHANUMERIC = "^[a-zA-Z0-9_-]+$";
	   private static final String ALPHANUMERICSPACE = "[a-z A-Z0-9]*";
	   private static final String ALPHAUPPERCASENUMERIC = "[A-Z0-9]*";
	   private static final String ALPHABETIC = "[a-zA-Z]*";
	   private static final String ALPHABETICUPPERCASE = "[A-Z]*";
	   private static final String ALPHABETICUPPERCASEAPOSTROPHECOMMA = "[A-Z',]*";
	   private static final String ALPHABETICLOWERCASE = "[a-z]*";
	   private static final String ALPHABETICSPACE = "[a-z A-Z]*";
	   private static final String ALPHABETICSPACEANDAMPERSAND = "[a-z A-Z &]*";
	   private static final String CARDMASKNUMERIC = "[0-9*Xx]*";
	   private static final String NUMERIC = "[0-9]*";
	   private static final String NUMERICCOLON = "[0-9:]*";
	   private static final String NUMERICCOMMA = "[0-9,]*";
	   private static final String NUMERICDOT = "[0-9.]*";
	   private static final String NUMERICDOTSPACEAPOSTROPHECOMMA = "[0-9. ',]*";
	   private static final String EMAILID = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,10})+$";
	   private static final String DATE = "[0-9\\/-]*";
	   private static final String TIME = "[0-9:]*";
	   private static final String IMAGENAME = ".+\\.[a-z]+";
	   private static final String SALUTATION = "[a-zA-Z]+\\.";
	   private static final String SECRECTQUESTION = "[a-z A-Z0-9]+\\?";
	   private static final String HOSTNAME = "[a-z.0-9]*";
	   private static final String ALPHAUPPERCASEUNDERSCORE = "[A-Z\\_]*";
	   private static final String ALPHAUPPERCASEPLUS = "[A-Z\\+]*";
	   private static final String ALPHAUPPERCASEPLUSDASH = "[A-Z\\+\\-]*";
	   private static final String ALPHANUMERICDOT = "[a-zA-Z0-9.]*";
	   private static final String ALPHANUMERICDOTAPOSTROPHECOMMA = "[a-zA-Z0-9.',]*";
	   private static final String ALPHANUMERICDOTCOMMACOLONSEMICOLON = "[a-zA-Z0-9.,:;]*";
	   private static final String ALPHANUMERICSPACEDOT = "[a-zA-Z0-9 \\.]*";
	   private static final String ALPHANUMERICCOMMA = "[a-zA-Z0-9\\,]*";
	   private static final String ALPHANUMERICSPACEDASH = "[a-zA-Z0-9 \\-]*";
	   private static final String ALPHANUMERICSPACEDASHUNDERSCORE = "[a-zA-Z0-9 \\-\\_]*";
	   private static final String ALPHANUMERICSPACEDASHUNDERSCOREBRACKET = "[a-zA-Z0-9 \\-\\_\\(\\)]*";
	   private static final String ALPHANUMERICSPACEDOTUNDERSCORE = "[a-zA-Z0-9 \\.\\_]*";
	   private static final String ALPHANUMERICSPACEAPOSTROPHECOMMA = "[a-zA-Z0-9 \\'\\,]*";
	   private static final String VALIDPASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*\\.\\,\\'\\\"\\:\\;\\_\\-\\)\\(\\?\\~\\`\\+\\=\\>\\<\\}\\{\\]\\[\\|\\\\\\/])[a-zA-Z0-9!@#$%^&*\\.\\,\\'\\\"\\:\\;\\_\\-\\)\\(\\?\\~\\`\\+\\=\\>\\<\\}\\{\\]\\[\\|\\\\\\/]{12,15}$";
	   private static final String URL = "[a-zA-Z0-9 \\/\\:\\.]*";
	   private static final String ADDRESS = "[a-zA-Z0-9 /\\\\,\\.\\-\\(\\)\\*:@&#~{}]+";
	   private static final String NOTREQUIREDCHARS = "[\\>\\<\\~\\`\\}\\{\\|\\^\\$]+";

	   
	   
	    public boolean isValidEmailId(String str) {
	        boolean flag = false;
	        if (str.matches(EMAILID)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isValidSalutation(String str) {
	        boolean flag = false;
	        if (str.matches(SALUTATION)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isValidSecrectQuestion(String str) {
	        boolean flag = false;
	        if (str.matches(SECRECTQUESTION)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isValidDate(String str) {
	        boolean flag = false;
	        if (str.matches(DATE)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isValidTime(String str) {
	        boolean flag = false;
	        if (str.matches(TIME)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isValidImage(String str) {
	        boolean flag = false;
	        if (str.matches(IMAGENAME)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public String getRefindedString(String str) {
	        str = str.trim();
	        str = str.replace("<", "&lt;");
	        str = str.replace(">", "&gt;");
	        return str;
	    }

	    public boolean isStringEmpty(String str){
	        boolean flag = false;
	        str = str.trim();
	        // Check Empty String 
	        if (str.length() < 1 || str.equals("")) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaString(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHABETIC)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaUppercaseString(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHABETICUPPERCASE)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaUppercaseApostropheComma(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHABETICUPPERCASEAPOSTROPHECOMMA)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaLowercaseString(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHABETICLOWERCASE)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaWithSpace(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHABETICSPACE)) {
	            flag = true;
	        }
	        return flag;
	    }
	    
	    public boolean isAlphaWithSpaceAndAmpersand(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHABETICSPACEANDAMPERSAND)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaNumeric(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHANUMERIC)) {
	            flag = true;
	        }
	        return flag;
	    }
	    
	    public boolean isAlphUppercaseNumeric(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHAUPPERCASENUMERIC)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphNumericWithSpace(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHANUMERICSPACE)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isNumeric(int num) {
	        String str = String.valueOf(num);
	        boolean flag = false;
	        if (str.matches(NUMERIC)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isNumericString(String str) {
	        boolean flag = false;
	        if (str.matches(NUMERIC)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isCardMaskNumString(String str) {
	        boolean flag = false;
	        if (str.matches(CARDMASKNUMERIC)) {
	            flag = true;
	        }
	        return flag;
	    }
	    
	    public boolean isNumericColon(String str) {
	        boolean flag = false;
	        if (str.matches(NUMERICCOLON)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isNumericComma(String str) {
	        boolean flag = false;
	        if (str.matches(NUMERICCOMMA)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isNumericDot(String str) {
	        boolean flag = false;
	        if (str.matches(NUMERICDOT)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isValidPassword(String password) {
	        boolean flag = false;
	        if (password.matches(VALIDPASSWORD)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean checkValidURL(String refererURL, String currentURL) {
	        boolean validURL = false;
	        String partRefererURL;
	        String partCurrentURL;
	        try {
	            partRefererURL = refererURL.substring(0, refererURL.lastIndexOf('/'));
	            partCurrentURL = currentURL.substring(0, currentURL.lastIndexOf('/'));

	            if (partRefererURL.equals(partCurrentURL) && !refererURL.isEmpty()) {
	                validURL = true;
	            }
	        } catch (Exception ex) {
	           
	        }
	       
	        return validURL;
	    }

	    public String getTodayDate() {
	        java.util.Date dt = new java.util.Date();
	        Format formatter = new SimpleDateFormat("MMMyyyy");
	        return formatter.format(dt);
	    }

	    public boolean isCurrentFileFormat(String str, String type) {
	        boolean flag = false;
	        str = str.trim();
	        type = "." + type;
	        String subs = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
	        if (subs.matches(type)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isValidHostName(String str) {
	        boolean flag = false;
	        if (str.matches(HOSTNAME)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public Date convertCalenderDateToSqlDate(String dateStr) throws ParseException {
	        String dd = dateStr.substring(0, 2);
	        String mm = dateStr.substring(3, 5);
	        String yy = dateStr.substring(6, 10);
	        dateStr = yy + "-" + mm + "-" + dd;
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        return df.parse(dateStr);
	    }

	    public boolean isAlphaNumericSpaceDot(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHANUMERICSPACEDOT)) {
	            flag = true;
	        }
	        return flag;
	    }
	    
	    public boolean isAlphaNumericSpaceDotUnderscore(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHANUMERICSPACEDOTUNDERSCORE)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaNumericDot(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHANUMERICDOT)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaNumericDotApostropheComma(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHANUMERICDOTAPOSTROPHECOMMA)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaNumericDotCommaColonSemicolon(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHANUMERICDOTCOMMACOLONSEMICOLON)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaNumericComma(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHANUMERICCOMMA)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaUppercaseUnderscore(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHAUPPERCASEUNDERSCORE)) {
	            flag = true;
	        }
	        return flag;
	    }
	            
	    public boolean isAlphaUppercasePlus(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHAUPPERCASEPLUS)) {
	            flag = true;
	        }
	        return flag;
	    }
	            
	    public boolean isAlphaUppercasePlusDash(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHAUPPERCASEPLUSDASH)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaNumericSpaceDash(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHANUMERICSPACEDASH)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaNumericSpaceDashUnderscore(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHANUMERICSPACEDASHUNDERSCORE)) {
	            flag = true;
	        }
	        return flag;
	    }
	    
	    public boolean isAlphaNumericSpaceDashUnderscoreBracket(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHANUMERICSPACEDASHUNDERSCOREBRACKET)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public String getDynamicDate(int count) {
	        Calendar today = Calendar.getInstance();
	        today.add(Calendar.DATE, count);
	        java.util.Date dt = new java.util.Date(today.getTimeInMillis());
	        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
	        return formatter.format(dt);
	    }

	    public boolean deleteDestinationFolder(File folder){
	        boolean flag = false;
	        try {
	            if (folder.exists()) {
	                File[] files = folder.listFiles();
	                for (File oFileCur : files) {
	                    if (oFileCur.isDirectory()) {
	                        // call itself to delete the contents of the current folder
	                        deleteDestinationFolder(oFileCur);
	                    }
	                    Files.delete(oFileCur.toPath());
	                }
	            }
	            if (Files.deleteIfExists(folder.toPath())) {
	                flag = true;
	            }
	        } catch (Exception ex) {
	           
	        }
	        return flag;
	    }

	    public boolean isNumericDotSpaceApostropheComma(String str) {
	        boolean flag = false;
	        if (str.matches(NUMERICDOTSPACEAPOSTROPHECOMMA)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isAlphaNumericSpaceApostropheComma(String str) {
	        boolean flag = false;
	        if (str.matches(ALPHANUMERICSPACEAPOSTROPHECOMMA)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isValidURL(String str) {
	        boolean flag = false;
	        if (str.matches(URL)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isValidAddress(String str) {
	        boolean flag = false;
	        if (str.matches(ADDRESS)) {
	            flag = true;
	        }
	        return flag;
	    }

	    public boolean isValidComment(String str) {
	        boolean flag = true;
	        if(str.contains(">") || str.contains("<")) {
	            flag = false;
	        } else if(str.contains("&gt;") || str.contains("&lt;")) {
	            flag = false;
	        } else if(!isAlphaString(str.substring(0, 1))) {
	            //This check added for CSV Macro Code Injection vulnerability closure : check for = + -
	            flag = false;
	        }
	        return flag;
	    }

	    public boolean isIPAddress(String ipAddress) {
	        String[] ipBlocks = ipAddress.split("\\.");
	        if (ipBlocks.length == 4) {
	            for (String ipBlock : ipBlocks) {
	                if(ipBlock.equals("")){
	                  return false;
	                }
	                if (!isNumericString(ipBlock)) {
	                  return false;
	                }
	                
	                int ipVal = Integer.parseInt(ipBlock);
	                if (ipVal < 0 || ipVal > 255) {
	                    return false;
	                }
	                if(!ipBlock.equals(String.valueOf(ipVal))){ 
	                   return false;  
	                } 
	            }
	        } else {
	            return false;
	        }
	        return true;
	    }

	    public boolean isValidSearchPattern(String pattern) {
	        return pattern.contains(".*");
	    }
	    
	    public boolean containsNotRequiredChars(String str) {
	        boolean flag = false;
	        Pattern p = Pattern.compile(NOTREQUIREDCHARS);
	        Matcher m = p.matcher(str);
	        if (m.find()) {
	            flag = true;
	        }
	        return flag;
	    }

	}

