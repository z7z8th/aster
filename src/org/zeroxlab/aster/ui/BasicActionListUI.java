/*
 * Copyright (C) 2011 0xlab - http://0xlab.org/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authored by Kan-Ru Chen <kanru@0xlab.org>
 */

package org.zeroxlab.aster.ui;

import com.android.ninepatch.NinePatch;
import com.android.ninepatch.NinePatchChunk;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;

import org.zeroxlab.aster.ActionListModel;
import org.zeroxlab.aster.JActionList;
import org.zeroxlab.aster.AsterCommand;

/**
 * Basic UI for {@link JActionList}.
 */
public class BasicActionListUI extends ActionListUI {
    /**
     * The associated action list.
     */
    protected JActionList actionList;

    protected MouseListener mouseListener;

    protected MouseMotionListener mouseMotionListener;

    protected ChangeListener actionListChangeListener;

    /*
     * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
     */
    public static ComponentUI createUI(JComponent c) {
        return new BasicActionListUI();
    }

    /*
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    public void installUI(JComponent c) {
        this.actionList = (JActionList) c;
        installDefaults();
        installListeners();
    }

    /*
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    public void uninstallUI(JComponent c) {
        c.setLayout(null);
        uninstallListeners();
        uninstallDefaults();

        this.actionList = null;
    }

    public void installDefaults() {
    }

    public void installListeners() {
        this.mouseListener = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }
            };
        this.actionList.addMouseListener(this.mouseListener);

        this.mouseMotionListener = new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                }
            };
        this.actionList.addMouseMotionListener(this.mouseMotionListener);

        this.actionListChangeListener = new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    actionList.repaint();
                }
            };
        this.actionList.getModel().addChangeListener(
            this.actionListChangeListener);
    }

    public void uninstallDefaults() {
    }

    public void uninstallListeners() {
        this.actionList.removeMouseListener(this.mouseListener);
        this.mouseListener = null;

        this.actionList.removeMouseMotionListener(this.mouseMotionListener);
        this.mouseMotionListener = null;

        this.actionList.getModel().removeChangeListener(
            this.actionListChangeListener);
        this.actionListChangeListener = null;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        paintRootButton(g);
    }

    // ******************
    //   Layout Methods
    // ******************
    protected static int textMargin = 10;
    protected static int buttonMargin = 10;
    protected static String ROOT_LABEL = "RECALL";
    protected java.util.List<ActionButton> buttonList;

    protected void updateButtonList() {
        buttonList = new Vector<ActionButton>();
        for (AsterCommand cmd : this.actionList.getModel().getCommands()) {
            buttonList.add(new ActionButton(cmd));
        }
    }

    protected void paintRootButton(Graphics g) {
        FontMetrics fm = actionList.getFontMetrics(getFont());
        Rectangle bbox = fm.getStringBounds(ROOT_LABEL, g).getBounds();
        try {
            InputStream stream = this.getClass().getResourceAsStream("/border.9.png");
            NinePatch mPatch = NinePatch.load(stream, true, false);
            mPatch.draw((Graphics2D)g,
                        buttonMargin,
                        buttonMargin,
                        bbox.width + textMargin*2,
                        bbox.height + textMargin*2);
        } catch (IOException e) {
        }
        g.setFont(getFont());
        g.drawString(ROOT_LABEL,
                     buttonMargin + textMargin,
                     buttonMargin + textMargin + fm.getAscent());
        g.drawLine(buttonMargin + textMargin + bbox.width/2,
                   buttonMargin + bbox.height + textMargin*2 + buttonMargin,
                   buttonMargin + textMargin + bbox.width/2,
                   buttonMargin + bbox.height + textMargin*2 + buttonMargin + 10);
    }

    protected Font getFont() {
        return new Font(Font.SANS_SERIF, Font.BOLD, 20);
    }

    protected void layout(Graphics g) {
    }

    public Dimension getMinimumSize(JComponent c) {
        return getPreferredSize(c);
    }
    public Dimension getPreferredSize(JComponent c) {
        FontMetrics fm = actionList.getFontMetrics(getFont());
        int width = fm.stringWidth(ROOT_LABEL) + textMargin*2 + buttonMargin*2;
        return new Dimension(width, 400);
    }
    public Dimension getMaximumSize(JComponent c) {
        return getPreferredSize(c);
    }

    class ActionButton {
        AsterCommand mCommand;
	public ActionButton(AsterCommand cmd) {
            mCommand = cmd;
        }
    }
}
