/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cophi.model.spi;

import eu.cophi.commons.In;
import eu.cophi.model.api.Document;
import eu.cophi.model.api.Document.DocCallBack;
import eu.cophi.model.api.Document.IDoc;
import java.io.File;


/**
 *
 * @author angelodel80
 */
public class DefDocProvider implements Document.IDoc {
    
   private String[] tokens;
   private Integer[] pericopes; // rappresenta la pericopatura del testo. L'indice è la pericope
                    // il valore è l'indicazione dell'ultimo token. Per esempio
                    // pericopes[3] rappresenta la quarta pericope. Se il valore é
                    // 60 significa che la quarta pericope termina al token 60 (escluso?)
                    // l'inizio della pericope è il valore dell'elemento precedente. In questo 
                    // caso pericopes[2]
    
    DocCallBack callback;

    @Override
    public void initialize(DocCallBack c, File src) {
        //throw new UnsupportedOperationException("Not supported yet.");
        this.callback = c;
        tokens = (new In(src)).readAllStrings();
        pericopes = new Integer[tokens.length];
        pericopes[0] = new Integer(tokens.length-1);
    }

    @Override
    public String getName() {
        //throw new UnsupportedOperationException("Not supported yet.");
        return this.callback.getContext();
    }

    @Override
    public int getCurrentPosition() {
        //throw new UnsupportedOperationException("Not supported yet.");
        int ret = this.callback.getCurr();
        this.callback.currUpdate();
        return ret;
    }

    @Override
    public <T> T[] getContent(String key) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if("tokens".equals(key)) {
            return (T[])tokens;
        }
        else if("pericopes".equals(key)) {
            return (T[])pericopes;
        }
        return null;
    }

    @Override
    public double similarTo(IDoc doc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
