package net.sf.anathema.framework.view.messaging;

import net.sf.anathema.lib.gui.IView;
import net.sf.anathema.lib.message.IBasicMessage;

public interface IStatusBar extends IView {

  void setLatestMessage(IBasicMessage message);
}