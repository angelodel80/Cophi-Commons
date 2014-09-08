/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cophi.model.api;

import eu.cophi.model.spi.DefContentHandler;
import eu.cophi.model.spi.DefDocProvider;
import java.io.File;

/**
 *
 * @author angelodel80
 */
public final class Document {

    private int cur=0;
    private final int id;
    private final IDoc impl;
    private final ContentHandler handler;
    
    /**
     *
     */
    public final Text DEF_TEXT = new Text(){

        @Override
        public String getContent() {
            //throw new UnsupportedOperationException("Not supported yet.");
            return handler.getContent(this);
        }

        @Override
        public int compareTo(String t) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    };
    
    public final Paratext DEF_PARATEXT = new Paratext() {

        @Override
        public String getContent() {
            //throw new UnsupportedOperationException("Not supported yet.");
            return handler.getContent(this);
        }
    };
    
    public final Extratext DEF_EXTRATEXT = new Extratext(){

        @Override
        public String getContent() {
            //throw new UnsupportedOperationException("Not supported yet.");
            return handler.getContent(this);
        }
        
    };
    
    public final Metadata DEF_METADATA = new Metadata() {

        @Override
        public String getContent() {
            //throw new UnsupportedOperationException("Not supported yet.");
            return handler.getContent(this);
        }
    };
    
    public final Annotation DEF_ANNOTATION = new Annotation() {

        @Override
        public String getContent() {
            //throw new UnsupportedOperationException("Not supported yet.");
            return handler.getContent(this);
        }
    };
    
    public final Facsimile DEF_FACSIMILE = new Facsimile() {

        @Override
        public String getContent() {
            //throw new UnsupportedOperationException("Not supported yet.");
            return this.getName();
        }
    };
    
    // sia testo che paratesto, ma forse anche l'extratesto derivano da una classe/interfaccia comune
    // la quale contiene le funzionalità di base e le strutture dati pertinenti
    public static abstract class Text extends Object implements Comparable<String> {
        private Text(){}
        // string deve avere un tipo appropriato per esempio Content? Pensare ad un tipo Content
        // con alcune funzionalità di base come per esempio la navigazione oppure il visitor, etc..
        public abstract String getContent();
    }
    public static abstract class Paratext{
        private Paratext(){}
        
        public abstract String getContent();
    }
    public static abstract class Extratext{
        private Extratext(){}
        public abstract String getContent();
    }
    public static abstract class Metadata{
        private Metadata(){}
        public abstract String getContent();
    }
    
    public static abstract class Annotation{
        private Annotation(){}
        public abstract String getContent();
    }
    
    public static abstract class Facsimile extends Image {
        private Facsimile(){
            super();
        }
        public abstract String getContent();
        
    }
    
    // il documento è formato da pericopi oppure è formato da testo paratesto e extratesto
    // il documento è una risorsa testuale fatta di testo e immagine. Il testo è 
    // a sua volta la trascrizione diplomatica oppure interpretata del contenuto testuale
    // del documento Questo può essere deciso come rappresentarlo direttamente all'interno
    // dell'implementazione?
    // possiamo trovare un modo per separare il modello dati (esempio albero, oppure annotazione su token) 
    // dalla vista? (esempio seq pericopi, pagine, paragrafi, capitoli)
    
    static final String DEFAULT_SRC = "src.txt";

    private Document() {
        this(   Double.valueOf(Math.random()*1000).intValue(), 
                new DefDocProvider(),
                new DefContentHandler(),
                new File(DEFAULT_SRC)
                );
    }

    private Document(int id, IDoc provider, ContentHandler handler, File src) {
        this.id = id;
        this.impl = provider;
        this.handler = handler;
        DocCallBack c = new DocCallBack(this);
        this.impl.initialize(c, src);
        this.handler.initialize(this.impl);
    }

    // factory method
    public static Document getDefaultDoc() {
        Document doc = new Document();
        return doc;
    }
    
    // metodi di API
    public final String getIdentifier(){
        return this.impl.getName();
    }
    
    public final int getPosition(){
        return this.impl.getCurrentPosition();
    }
    
    // nell'ambito filologico oltre ai token bisognerebbe prevedere una entità
    // simile, ma molto specifica che è la lezione (lectio)
    /* FIXME  plurale di lectio*/ 
    public final String[] getLectio(){
        return this.impl.getContent("tokens");
    }
    
    public final String[] getTokens(){
        return this.impl.getContent("tokens");
    }
    
    public final Integer[] getPericopes(){
        return this.impl.getContent("pericopes");
    }

    // interfaccia SPI per i provider
    public interface IDoc {
        public void initialize(DocCallBack c, File src);
        public String getName();
        public int getCurrentPosition();
        public <T> T[] getContent(String key); // metodi generici
        public double similarTo(IDoc doc);
    }
    
    // interfaccia SPI per handler del contenuto 
    // vedere l'handler che implementa tika oppure parser testuali
    public interface ContentHandler{
        public void initialize(IDoc provider);
        public <T> String getContent(T content);
        
    }

    // classe che implementa le callback
    public static final class DocCallBack {

        Document doc;

        DocCallBack(Document doc) {
            this.doc = doc;
        }
        
        public final String getContext() {
            return String.valueOf(doc.id).concat("\t^-^\n");
        }
        
        public final void currUpdate(){
            this.doc.cur++;
        }
        
        public final int getCurr(){
            return this.doc.cur;
        }
    }
}
