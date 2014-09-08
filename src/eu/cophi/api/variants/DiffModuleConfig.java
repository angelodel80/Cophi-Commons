/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
package eu.cophi.api.variants;



import eu.cophi.modules.variants.builtin.Provider;
import java.util.prefs.Preferences;
import java.util.*;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.UIManager;

import eu.cophi.spi.variants.DiffProvider;

/**
 * Module settings for Diff module.
 *
 * @author Maros Sandor
 */
public class DiffModuleConfig {

    public static final String PREF_EXTERNAL_DIFF_COMMAND = "externalDiffCommand"; // NOI18N
    private static final String PREF_IGNORE_LEADINGTRAILING_WHITESPACE = "ignoreWhitespace"; // NOI18N
    private static final String PREF_IGNORE_INNER_WHITESPACE = "ignoreInnerWhitespace"; // NOI18N
    private static final String PREF_IGNORE_CASE = "ignoreCase"; // NOI18N
    private static final String PREF_USE_INTERNAL_DIFF = "useInternalDiff"; // NOI18N
    private static final String PREF_ADDED_COLOR = "addedColor"; // NOI18N
    private static final String PREF_CHANGED_COLOR = "changedColor"; // NOI18N
    private static final String PREF_DELETED_COLOR = "deletedColor"; // NOI18N
    private static final String PREF_MERGE_UNRESOLVED_COLOR = "merge.unresolvedColor"; // NOI18N
    private static final String PREF_MERGE_APPLIED_COLOR = "merge.appliedColor"; // NOI18N
    private static final String PREF_MERGE_NOTAPPLIED_COLOR = "merge.notappliedColor"; // NOI18N
    private static final String PREF_SIDEBAR_DELETED_COLOR = "sidebar.deletedColor"; //NOI18N
    private static final String PREF_SIDEBAR_CHANGED_COLOR = "sidebar.changedColor"; //NOI18N
    private static final DiffModuleConfig INSTANCE = new DiffModuleConfig();
    private final Color defaultAddedColor;
    private final Color defaultChangedColor;
    private final Color defaultDeletedColor;
    private final Color defaultAppliedColor;
    private final Color defaultNotAppliedColor;
    private final Color defaultUnresolvedColor;
    private final Color defaultSidebarDeletedColor;
    private final Color defaultSidebarChangedColor;

    public static DiffModuleConfig getDefault() {
        return INSTANCE;
    }

    private DiffModuleConfig() {
        Color c = UIManager.getColor("nb.diff.added.color"); //NOI18N
        if (null == c) {
            c = new Color(180, 255, 180);
        }
        defaultAddedColor = c;

        c = UIManager.getColor("nb.diff.changed.color"); //NOI18N
        if (null == c) {
            c = new Color(160, 200, 255);
        }
        defaultChangedColor = c;

        c = UIManager.getColor("nb.diff.deleted.color"); //NOI18N
        if (null == c) {
            c = new Color(255, 160, 180);
        }
        defaultDeletedColor = c;

        c = UIManager.getColor("nb.diff.applied.color"); //NOI18N
        if (null == c) {
            c = new Color(180, 255, 180);
        }
        defaultAppliedColor = c;

        c = UIManager.getColor("nb.diff.notapplied.color"); //NOI18N
        if (null == c) {
            c = new Color(160, 200, 255);
        }
        defaultNotAppliedColor = c;

        c = UIManager.getColor("nb.diff.unresolved.color"); //NOI18N
        if (null == c) {
            c = new Color(255, 160, 180);
        }
        defaultUnresolvedColor = c;

        c = UIManager.getColor("nb.diff.sidebar.deleted.color"); //NOI18N
        if (null == c) {
            c = new Color(255, 225, 232);
        }
        defaultSidebarDeletedColor = c;

        c = UIManager.getColor("nb.diff.sidebar.changed.color"); //NOI18N
        if (null == c) {
            c = new Color(233, 241, 255);
        }
        defaultSidebarChangedColor = c;
    }

    public Color getAddedColor() {
        return getColor(PREF_ADDED_COLOR, defaultAddedColor);
    }

    public Color getDefaultAddedColor() {
        return defaultAddedColor;
    }

    public Color getChangedColor() {
        return getColor(PREF_CHANGED_COLOR, defaultChangedColor);
    }

    public Color getDefaultChangedColor() {
        return defaultChangedColor;
    }

    public Color getDeletedColor() {
        return getColor(PREF_DELETED_COLOR, defaultDeletedColor);
    }

    public Color getDefaultDeletedColor() {
        return defaultDeletedColor;
    }

    public Color getAppliedColor() {
        return getColor(PREF_MERGE_APPLIED_COLOR, defaultAppliedColor);
    }

    public Color getDefaultAppliedColor() {
        return defaultAppliedColor;
    }

    public Color getNotAppliedColor() {
        return getColor(PREF_MERGE_NOTAPPLIED_COLOR, defaultNotAppliedColor);
    }

    public Color getDefaultNotAppliedColor() {
        return defaultNotAppliedColor;
    }

    public Color getUnresolvedColor() {
        return getColor(PREF_MERGE_UNRESOLVED_COLOR, defaultUnresolvedColor);
    }

    public Color getDefaultUnresolvedColor() {
        return defaultUnresolvedColor;
    }

    public Color getSidebarDeletedColor() {
        return getColor(PREF_SIDEBAR_DELETED_COLOR, defaultSidebarDeletedColor);
    }

    public Color getDefaultSidebarDeletedColor() {
        return defaultSidebarDeletedColor;
    }

    public Color getSidebarChangedColor() {
        return getColor(PREF_SIDEBAR_CHANGED_COLOR, defaultSidebarChangedColor);
    }

    public Color getDefaultSidebarChangedColor() {
        return defaultSidebarChangedColor;
    }

    public void setChangedColor(Color color) {
        putColor(PREF_CHANGED_COLOR, defaultChangedColor.equals(color) ? null : color);
    }

    public void setAddedColor(Color color) {
        putColor(PREF_ADDED_COLOR, defaultAddedColor.equals(color) ? null : color);
    }

    public void setDeletedColor(Color color) {
        putColor(PREF_DELETED_COLOR, defaultDeletedColor.equals(color) ? null : color);
    }

    public void setNotAppliedColor(Color color) {
        putColor(PREF_MERGE_NOTAPPLIED_COLOR, defaultNotAppliedColor.equals(color) ? null : color);
    }

    public void setAppliedColor(Color color) {
        putColor(PREF_MERGE_APPLIED_COLOR, defaultAppliedColor.equals(color) ? null : color);
    }

    public void setUnresolvedColor(Color color) {
        putColor(PREF_MERGE_UNRESOLVED_COLOR, defaultUnresolvedColor.equals(color) ? null : color);
    }

    public void setSidebarDeletedColor(Color color) {
        putColor(PREF_SIDEBAR_DELETED_COLOR, defaultSidebarDeletedColor.equals(color) ? null : color);
    }

    public void setSidebarChangedColor(Color color) {
        putColor(PREF_SIDEBAR_CHANGED_COLOR, defaultSidebarChangedColor.equals(color) ? null : color);
    }

    private void putColor(String key, Color color) {
        if (color == null) {
            getPreferences().remove(key);
        } else {
            getPreferences().putInt(key, color.getRGB());
        }
    }

    private Color getColor(String key, Color defaultColor) {
        int rgb = getPreferences().getInt(key, defaultColor.getRGB());
        return new Color(rgb);
    }

    public DiffProvider getDefaultDiffProvider() {

        Provider.INSTANCE.setOptions(getOptions());
        return Provider.INSTANCE;
    }

//    private void setDefaultProvider(DiffProvider ds) {
//        // TODO: Diff providers are registered in the layer so that we can change the order in which they
//        // TODO: appear in the lookup programmatically during runtime
//        FileObject services = FileUtil.getConfigFile("Services/DiffProviders");
//        DataFolder df = DataFolder.findFolder(services);
//        DataObject[] children = df.getChildren();
//        for (int i = 0; i < children.length; i++) {
//            if (children[i] instanceof InstanceDataObject) {
//                InstanceDataObject ido = (InstanceDataObject) children[i];
//                if (ido.instanceOf(ds.getClass())) {
//                    try {
//                        if (ds.equals(ido.instanceCreate())) {
//                            df.setOrder(new DataObject[]{ido});
//                            break;
//                        }
//                    } catch (java.io.IOException ioex) {
//                    } catch (ClassNotFoundException cnfex) {
//                    }
//                }
//            }
//        }
//    }
    private String getDiffCommand() {
        return getPreferences().get(PREF_EXTERNAL_DIFF_COMMAND, "diff {0} {1}");
    }

    public void setOptions(Provider.Options options) {
        getPreferences().putBoolean(PREF_IGNORE_LEADINGTRAILING_WHITESPACE, options.ignoreLeadingAndtrailingWhitespace);
        getPreferences().putBoolean(PREF_IGNORE_INNER_WHITESPACE, options.ignoreInnerWhitespace);
        getPreferences().putBoolean(PREF_IGNORE_CASE, options.ignoreCase);
        getBuiltinProvider().setOptions(options);
    }

    public Provider.Options getOptions() {
        Provider.Options options = new Provider.Options();
        options.ignoreLeadingAndtrailingWhitespace = getPreferences().getBoolean(PREF_IGNORE_LEADINGTRAILING_WHITESPACE, true);
        options.ignoreInnerWhitespace = getPreferences().getBoolean(PREF_IGNORE_INNER_WHITESPACE, false);
        options.ignoreCase = getPreferences().getBoolean(PREF_IGNORE_CASE, false);
        return options;
    }

    private Provider getBuiltinProvider() {
        return Provider.INSTANCE;
    }

    public void setUseInteralDiff(boolean useInternal) {
        getPreferences().putBoolean(PREF_USE_INTERNAL_DIFF, useInternal);
        if (useInternal) {
            //setDefaultProvider(getBuiltinProvider());
        } else {

            //setDefaultProvider(Provider.INSTANCE);

        }

    }

    public boolean isUseInteralDiff() {
        return getPreferences().getBoolean(PREF_USE_INTERNAL_DIFF, true);
    }

    // properties ~~~~~~~~~~~~~~~~~~~~~~~~~
    public Preferences getPreferences() {
        //return NbPreferences.forModule(DiffModuleConfig.class);
        return new Preferences() {

            @Override
            public void put(String string, String string1) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String get(String string, String string1) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void remove(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void clear() throws BackingStoreException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void putInt(String string, int i) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getInt(String string, int i) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void putLong(String string, long l) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public long getLong(String string, long l) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void putBoolean(String string, boolean bln) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean getBoolean(String string, boolean bln) {
                //throw new UnsupportedOperationException("Not supported yet.");
                return true;
            }

            @Override
            public void putFloat(String string, float f) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public float getFloat(String string, float f) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void putDouble(String string, double d) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public double getDouble(String string, double d) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void putByteArray(String string, byte[] bytes) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public byte[] getByteArray(String string, byte[] bytes) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String[] keys() throws BackingStoreException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String[] childrenNames() throws BackingStoreException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Preferences parent() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Preferences node(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean nodeExists(String string) throws BackingStoreException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void removeNode() throws BackingStoreException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String name() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String absolutePath() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isUserNode() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String toString() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void flush() throws BackingStoreException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void sync() throws BackingStoreException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void addPreferenceChangeListener(PreferenceChangeListener pl) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void removePreferenceChangeListener(PreferenceChangeListener pl) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void addNodeChangeListener(NodeChangeListener nl) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void removeNodeChangeListener(NodeChangeListener nl) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void exportNode(OutputStream out) throws IOException, BackingStoreException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void exportSubtree(OutputStream out) throws IOException, BackingStoreException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    /**
     * Returns number of spaces replacing a tab in editor
     *
     * @param mimeType
     * @return
     */
    public int getSpacesPerTabFor(String mimeType) {
        int spacesPerTab = 1;
//        Preferences pref = MimeLookup.getLookup(mimeType).lookup(Preferences.class);
//        if (pref != null) {
//            spacesPerTab = pref.getInt(SimpleValueNames.TAB_SIZE, 1);
//        }
        return spacesPerTab;
    }
}
