package net.sf.anathema.initialization;

import java.awt.AWTException;
import java.util.Collection;

import net.disy.commons.core.exception.CentralExceptionHandling;
import net.sf.anathema.framework.IAnathemaModel;
import net.sf.anathema.framework.InitializationException;
import net.sf.anathema.framework.configuration.IAnathemaPreferences;
import net.sf.anathema.framework.exception.CentralExceptionHandler;
import net.sf.anathema.framework.presenter.AnathemaViewProperties;
import net.sf.anathema.framework.repository.RepositoryException;
import net.sf.anathema.framework.resources.AnathemaResources;
import net.sf.anathema.framework.resources.IAnathemaResources;
import net.sf.anathema.framework.view.AnathemaView;
import net.sf.anathema.framework.view.IAnathemaView;
import net.sf.anathema.initialization.modules.IModuleCollection;
import net.sf.anathema.initialization.plugin.AnathemaPluginManager;
import net.sf.anathema.initialization.plugin.IPluginConstants;
import net.sf.anathema.lib.resources.IResources;

import org.java.plugin.registry.Extension;
import org.java.plugin.registry.Extension.Parameter;

public class AnathemaInitializer {

  private static final String EXTENSION_POINT_RESOURCES = "AnathemaResources"; //$NON-NLS-1$
  private static final String PARAM_BUNDLE = "bundle"; //$NON-NLS-1$
  private final IModuleCollection moduleCollection;
  private final IAnathemaPreferences anathemaPreferences;
  private final AnathemaPluginManager pluginManager;

  public AnathemaInitializer(IAnathemaPreferences anathemaPreferences) throws InitializationException {
    this.pluginManager = new AnathemaPluginManager();
    pluginManager.collectPlugins();
    this.moduleCollection = new ModuleCollection(pluginManager);
    this.anathemaPreferences = anathemaPreferences;
  }

  public IAnathemaView initialize() throws Exception {
    IAnathemaResources resources = initResources();
    CentralExceptionHandling.setHandler(new CentralExceptionHandler(resources));
    IAnathemaModel anathemaModel = initModel(resources);
    IAnathemaView view = initView(resources);
    new AnathemaPresenter(anathemaModel, view, resources).initPresentation(moduleCollection);
    return view;
  }

  private IAnathemaModel initModel(IResources resources) throws RepositoryException {
    return new AnathemaModelInitializer(anathemaPreferences).initializeModel(moduleCollection, resources);
  }

  private IAnathemaView initView(IAnathemaResources resources) throws AWTException {
    AnathemaViewProperties viewProperties = new AnathemaViewProperties(resources, anathemaPreferences.initMaximized());
    return new AnathemaView(viewProperties);
  }

  private IAnathemaResources initResources() {
    IAnathemaResources resources = new AnathemaResources();
    for (Extension extension : pluginManager.getExtension(IPluginConstants.PLUGIN_CORE, EXTENSION_POINT_RESOURCES)) {
      Collection<Parameter> bundleParams = extension.getParameters(PARAM_BUNDLE);
      for (Parameter param : bundleParams) {
        resources.addResourceBundle(param.valueAsString());
      }
    }
    return resources;
  }
}