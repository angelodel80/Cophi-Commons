/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cophi.model.spi;

import eu.cophi.model.api.Document;
import eu.cophi.model.api.Document.IDoc;
import eu.cophi.model.api.Document.Text;

/**
 *
 * @author angelodel80
 */
public class DefContentHandler implements Document.ContentHandler {

   private String content;

    public DefContentHandler() {
    }

    @Override
    public void initialize(IDoc provider) {
        //throw new UnsupportedOperationException("Not supported yet.");
        StringBuilder strBuilder = new StringBuilder();
        String[] tokens = provider.getContent("tokens");
        for (String token : tokens){
            strBuilder.append(token).append("--");
        }
            
        this.content = strBuilder.toString();
    }

    @Override
    public <T> String getContent(T content) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if (content instanceof Text) {
            return this.content;
        }
        return "no content";
    }
}
