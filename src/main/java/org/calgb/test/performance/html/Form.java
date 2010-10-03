package org.calgb.test.performance.html;

import java.util.HashMap;
import java.util.List;

public class Form extends HtmlElement {

    public Form(String tagname, String innerHtml)
        {
            super(tagname, innerHtml);
        }

    public HashMap<String, String> getParameters()
        {
            List<HtmlElement> inputs = find("input");
            HashMap<String, String> params = new HashMap<String, String>();
            for (HtmlElement input : inputs)
                {
                    HashMap<String, String> attributes = input.getAttr();
                    String paramName = attributes.get("name");
                    String paramValue = attributes.get("value");

                    if (paramName == null)
                        {
                            break;
                        }
                    else if (paramValue == null)
                        {
                            params.put(paramName, "");
                        }
                    else
                        {
                            params.put(paramName, paramValue);
                        }
                    
                }
            return params;
        }

}
