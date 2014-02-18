package net.sf.anathema.character.infernal.reporting.rendering;


import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import net.sf.anathema.character.raksha.reporting.content.RakshaLureContent;
import net.sf.anathema.character.reporting.pdf.content.ReportSession;
import net.sf.anathema.character.reporting.pdf.rendering.boxes.virtueflaw.VirtueFlawBoxEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.extent.Bounds;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.AbstractContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.SheetGraphics;


import static net.sf.anathema.character.reporting.pdf.rendering.graphics.TextUtilities.createBoldFont;
import static net.sf.anathema.character.reporting.pdf.rendering.page.IVoidStateFormatConstants.REDUCED_LINE_HEIGHT;


public class LureEncoder extends AbstractContentEncoder<RakshaLureContent> {


  private final VirtueFlawBoxEncoder traitEncoder = new VirtueFlawBoxEncoder();


  public LureEncoder() {
    super(RakshaLureContent.class);
  }


  @Override
  public void encode(SheetGraphics graphics, ReportSession reportSession, Bounds bounds) throws DocumentException {
    InfernalUrgeContent content = createContent(reportSession);
    Bounds textBounds = traitEncoder.encode(graphics, bounds, content.getLimitValue());
    Phrase phrase = new Phrase();
    phrase.add(new Chunk(content.getLureTitle(), createNameFont(graphics)));
    phrase.add(new Chunk(content.getLureDescription(), createDefaultFont(graphics)));
    graphics.createSimpleColumn(textBounds).withLeading(REDUCED_LINE_HEIGHT).andTextPart(phrase).encode();
  }


  private Font createNameFont(SheetGraphics graphics) {
    return createBoldFont(createDefaultFont(graphics));
  }


  private Font createDefaultFont(SheetGraphics graphics) {
    return graphics.createTableFont();
  }
}

