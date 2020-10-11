import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public final class LengthRestrictedDocument extends PlainDocument {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final int lim;

  public LengthRestrictedDocument(int lim) {
    this.lim = lim;
  }

  @Override
  public void insertString(int offs, String str, AttributeSet a)
      throws BadLocationException {
    if (str == null)
      return;

    if ((getLength() + str.length()) < lim) {
      super.insertString(offs, str, a);
    }
  }
}