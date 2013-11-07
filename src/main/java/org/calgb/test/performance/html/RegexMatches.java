package org.calgb.test.performance.html;

import java.util.ArrayList;

public class RegexMatches extends ArrayList<RegexMatch> {
    
    public String getText(){
        StringBuffer result = new StringBuffer();
        for(RegexMatch match: this){
          result.append(match.getText()); 
        }
        return result.toString();
    }

}
