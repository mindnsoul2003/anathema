package net.sf.anathema.campaign.music.presenter.library.content;

import net.sf.anathema.campaign.music.model.libary.ILibrary;
import net.sf.anathema.campaign.music.model.libary.ILibraryControl;
import net.sf.anathema.campaign.music.presenter.IMusicSearchControl;
import net.sf.anathema.campaign.music.presenter.util.Mp3FileFilter;
import net.sf.anathema.campaign.music.view.library.ILibraryControlView;
import net.sf.anathema.framework.message.MessageUtilities;
import net.sf.anathema.framework.presenter.DirectoryFileChooser;
import net.sf.anathema.framework.presenter.resources.FileUi;
import net.sf.anathema.lib.gui.action.SmartAction;
import net.sf.anathema.lib.message.Message;
import net.sf.anathema.lib.resources.IResources;

import java.awt.Component;
import java.nio.file.Path;

import static net.sf.anathema.campaign.music.presenter.library.content.AddMusicFolderAction.ADD_MUSIC_CHOOSER_VALUE;

public class AddMusicFileAction extends SmartAction {
  private final ILibraryControl model;
  private final ILibraryControlView view;
  private final IMusicSearchControl searchControl;
  private final IResources resources;

  public AddMusicFileAction(
          IResources resources,
          IMusicSearchControl searchControl,
          ILibraryControl model,
          ILibraryControlView view) {
    super(new FileUi(resources).getAddFileIcon());
    this.resources = resources;
    this.searchControl = searchControl;
    setToolTipText(resources.getString("Music.Actions.AddFile.Tooltip")); //$NON-NLS-1$
    this.model = model;
    this.view = view;
    view.whenSelectionChanges(new Runnable() {
      @Override
      public void run() {
        updateEnabled();
      }
    });
    updateEnabled();
  }

  private void updateEnabled() {
    setEnabled(view.getSelectedLibrary() != null);
  }

  @Override
  protected void execute(Component parentComponent) {
    Path mp3File = DirectoryFileChooser.chooseSingleFile(
            parentComponent,
            ADD_MUSIC_CHOOSER_VALUE,
            resources.getString("Music.Actions.AddFile.FileDialogTitle"), new Mp3FileFilter(resources)); //$NON-NLS-1$
    if (mp3File == null) {
      return;
    }
    String libraryName = ((ILibrary) view.getSelectedLibrary()).getName();
    try {
      model.addTrack(libraryName, mp3File);
      view.getTrackListView().setObjects(searchControl.getTracks(((ILibrary) view.getSelectedLibrary()).getName()));
    } catch (Exception e) {
      MessageUtilities.indicateMessage(AddMusicFileAction.class, parentComponent, new Message(
              resources.getString("Errors.MusicDatabase.ReadMusicData"), e)); //$NON-NLS-1$
    }
  }
}