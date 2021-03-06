/*
 * Copyright 2013 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.rendering.nui.layouts;

import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.layouts.miglayout.MigLayout;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UIButton;
import org.terasology.rendering.nui.widgets.UILabel;
import org.terasology.rendering.nui.properties.Property;
import org.terasology.rendering.nui.properties.PropertyProvider;

/**
 * Created by synopia on 03.01.14.
 */
public class PropertyLayout extends MigLayout {

    public PropertyLayout() {
    }

    public PropertyLayout(String id) {
        super(id);
    }

    public void addPropertyProvider(String label, final PropertyProvider<?> propertyProvider) {
        if (propertyProvider.getProperties().size() > 0) {
            final UIButton expand = new UIButton("", "+");
            final UILabel headline = new UILabel(label);
            final MigLayout layout = new MigLayout();
            layout.setColConstraints("[min][fill]");
            layout.setRowConstraints("[min]");

            expand.subscribe(new ActivateEventListener() {
                @Override
                public void onActivated(UIWidget widget) {
                    UIButton button = (UIButton) widget;
                    if ("-".equals(button.getText())) {
                        layout.clear();
                        invalidate();
                        button.setText("+");
                    } else {
                        for (Property<?, ?> property : propertyProvider.getProperties()) {
                            layout.addWidget(property.getLabel(), new CCHint("newline"));
                            layout.addWidget(property.getEditor(), new CCHint());
                        }
                        invalidate();
                        button.setText("-");
                    }
                }
            });
            addWidget(expand, new CCHint("newline, w 50!"));
            addWidget(headline, new CCHint());
            addWidget(layout, new CCHint("newline, spanx 2"));
        }
    }
}
