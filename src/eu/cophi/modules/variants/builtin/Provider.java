/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cophi.modules.variants.builtin;


import eu.cophi.api.variants.DiffModuleConfig;
import eu.cophi.api.variants.Variant;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import eu.cophi.spi.variants.DiffProvider;


/**
 *
 * @author angelodel80
 *
 *
 * public class BuiltInDiffProvider extends DiffProvider implements
 * java.io.Serializable {
 *
 */
public class Provider extends DiffProvider {

    public Provider() {
    };
    
    public static Provider INSTANCE = new Provider();

    private Options options = DiffModuleConfig.getDefault().getOptions();
    static final long serialVersionUID = 1L;

    /**
     * Get the display name of this diff provider.
     */
    public String getDisplayName() {
        return "BuiltInDiffProvider.displayName";
    }

    /**
     * Get a short description of this diff provider.
     */
    public String getShortDescription() {
        return "BuiltInDiffProvider.shortDescription";
    }

    /**
     * Create the differences of the content two streams.
     *
     * @param r1 the first source
     * @param r2 the second source to be compared with the first one.
     * @return the list of differences found, instances of {@link Difference};
     * or <code>null</code> when some error occured.
     */
    @Override
    public Variant[] computeDiff(Reader r1, Reader r2) throws IOException {
        if (options == null) {
            // blind fix of #144033, probably a deserialization issue?
            options = DiffModuleConfig.getDefault().getOptions();
        }
        return HuntVariants.diff(getLines(r1), getLines(r2), options);
    }

    private String[] getLines(Reader r) throws IOException {
        BufferedReader br = new BufferedReader(r);
        String line;
        List<String> lines = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        return lines.toArray(new String[lines.size()]);
    }

    /**
     * @param options a new set of diff options
     */
    public void setOptions(Options options) {
        this.options = options;
    }

    public static class Options {

        /**
         * True to ignore leading and trailing whitespace when computing diff,
         * false otherwise.
         */
        public boolean ignoreLeadingAndtrailingWhitespace;
        /**
         * True to ignore inner (not leading or trailing) whitespace when
         * computing diff, false otherwise.
         */
        public boolean ignoreInnerWhitespace;
        /**
         * True to ignore changes in case.
         */
        public boolean ignoreCase;
    }
}
