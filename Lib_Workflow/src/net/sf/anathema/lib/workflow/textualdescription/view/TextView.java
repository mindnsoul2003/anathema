package net.sf.anathema.lib.workflow.textualdescription.view;

import com.google.common.base.Objects;
import net.sf.anathema.lib.control.ObjectValueListener;
import net.sf.anathema.lib.gui.event.AbstractDocumentListener;
import net.sf.anathema.lib.lang.StringUtilities;
import net.sf.anathema.lib.workflow.textualdescription.SwingTextView;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.awt.Color;

public class TextView implements SwingTextView {

  private final JTextComponent textComponent;

  protected TextView(JTextComponent textComponent) {
    this.textComponent = textComponent;
    this.textComponent.setFont(new JTextField().getFont());
    this.textComponent.setDisabledTextColor(Color.DARK_GRAY);
  }

  @Override
  public final void setText(String text) {
    if (Objects.equal(textComponent.getText(), text)) {
      return;
    }
    textComponent.setText(text == null ? "" : text); //$NON-NLS-1$
  }

  @Override
  public final void addTextChangedListener(final ObjectValueListener<String> listener) {
    textComponent.getDocument().addDocumentListener(new AbstractDocumentListener() {
      @Override
      protected void updateText(DocumentEvent e) {
        String currentText = textComponent.getText();
        listener.valueChanged(StringUtilities.isNullOrEmpty(currentText) ? null : currentText);
      }
    });
  }

  public JComponent getComponent() {
    return textComponent;
  }

  public JTextComponent getTextComponent() {
    return textComponent;
  }

  @Override
  public void setEnabled(boolean enabled) {
    textComponent.setEditable(enabled);
    getComponent().setEnabled(enabled);
    UIDefaults defaults = UIManager.getDefaults();
    Color enabledColor = defaults.getColor("TextField.background"); //$NON-NLS-1$
    Color disabledColor = defaults.getColor("TextField.disabledBackground"); //$NON-NLS-1$
    getTextComponent().setBackground(enabled ? enabledColor : disabledColor);
  }
}