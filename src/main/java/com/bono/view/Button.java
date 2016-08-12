package com.bono.view;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by bono on 8/11/16.
 */
public interface Button {

    void setButtonIcon(Icon icon);

    void setButtonText(String text);

    void addButtonActionListener(ActionListener l);


}
