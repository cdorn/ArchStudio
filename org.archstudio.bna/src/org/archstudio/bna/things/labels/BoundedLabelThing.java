package org.archstudio.bna.things.labels;

import org.archstudio.bna.constants.IFontConstants;
import org.archstudio.bna.facets.IHasMutableColor;
import org.archstudio.bna.facets.IHasMutableFontData;
import org.archstudio.bna.facets.IHasMutableHorizontalAlignment;
import org.archstudio.bna.facets.IHasMutableText;
import org.archstudio.bna.facets.IHasMutableVerticalAlignment;
import org.archstudio.bna.facets.IRelativeMovable;
import org.archstudio.bna.things.AbstractRectangleThing;
import org.archstudio.swtutils.constants.FontStyle;
import org.archstudio.swtutils.constants.HorizontalAlignment;
import org.archstudio.swtutils.constants.VerticalAlignment;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.graphics.RGB;

@NonNullByDefault
public class BoundedLabelThing extends AbstractRectangleThing implements IHasMutableText, IHasMutableColor,
		IHasMutableHorizontalAlignment, IHasMutableVerticalAlignment, IHasMutableFontData, IRelativeMovable {

	public BoundedLabelThing(@Nullable Object id) {
		super(id);
	}

	protected void initProperties() {
		setText("");
		setColor(new RGB(0, 0, 0));
		setFontName(IFontConstants.DEFAULT_FONT_NAME);
		setFontSize(12);
		setFontStyle(FontStyle.NORMAL);
		setHorizontalAlignment(HorizontalAlignment.CENTER);
		setVerticalAlignment(VerticalAlignment.MIDDLE);
		setDontIncreaseFontSize(true);
		super.initProperties();
	}

	public @Nullable
	RGB getColor() {
		return get(COLOR_KEY);
	}

	public void setColor(@Nullable RGB c) {
		set(COLOR_KEY, c);
	}

	public String getText() {
		return get(TEXT_KEY, "");
	}

	public void setText(String text) {
		set(TEXT_KEY, text);
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return get(HORIZONTAL_ALIGNMENT_KEY, HorizontalAlignment.CENTER);
	}

	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		set(HORIZONTAL_ALIGNMENT_KEY, horizontalAlignment);
	}

	public VerticalAlignment getVerticalAlignment() {
		return get(VERTICAL_ALIGNMENT_KEY, VerticalAlignment.MIDDLE);
	}

	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		set(VERTICAL_ALIGNMENT_KEY, verticalAlignment);
	}

	public String getFontName() {
		return get(FONT_NAME_KEY, IFontConstants.DEFAULT_FONT_NAME);
	}

	public void setFontName(String fontName) {
		set(FONT_NAME_KEY, fontName);
	}

	public int getFontSize() {
		return get(FONT_SIZE_KEY, 12);
	}

	public void setFontSize(int fontSize) {
		set(FONT_SIZE_KEY, fontSize);
	}

	public FontStyle getFontStyle() {
		return get(FONT_STYLE_KEY, FontStyle.NORMAL);
	}

	public void setFontStyle(FontStyle fontStyle) {
		set(FONT_STYLE_KEY, fontStyle);
	}

	public boolean getDontIncreaseFontSize() {
		return get(DONT_INCREASE_FONT_SIZE_KEY, true);
	}

	public void setDontIncreaseFontSize(boolean dontIncrease) {
		set(DONT_INCREASE_FONT_SIZE_KEY, dontIncrease);
	}
}
