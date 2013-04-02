package org.calgb.test.performance.html;

import java.util.HashMap;
import java.util.List;

public class Form extends HtmlElement {

    public Form(final String tagname, final String innerHtml)
        {
            super(tagname, innerHtml);
        }

    public HashMap<String, String> getParameters()
        {
            final List<HtmlElement> inputs = find("input");
            final HashMap<String, String> params = new HashMap<String, String>();
            for (final HtmlElement input : inputs)
                {
                    final HashMap<String, String> attributes = input.getAttr();
                    final String paramName = attributes.get("name");
                    final String paramValue = attributes.get("value");

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
