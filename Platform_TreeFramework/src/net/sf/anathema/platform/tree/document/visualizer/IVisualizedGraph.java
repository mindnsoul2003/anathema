package net.sf.anathema.platform.tree.document.visualizer;

import java.awt.Dimension;

public interface IVisualizedGraph<G> {

  Dimension getDimension();

  boolean isSingleNode();

  void translateBy(double x, double y);

  void addTo(G cascade);
}