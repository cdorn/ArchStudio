package org.archstudio.xarchadt.core.internal;

import org.archstudio.xarchadt.IXArchADTSubstitutionHint;

public class BasicXArchADTSubstitutionHint implements IXArchADTSubstitutionHint {
	protected HintType hintType;

	protected String sourceNsURI;
	protected String sourceTypeName;

	protected String targetNsURI;
	protected String targetTypeName;

	public BasicXArchADTSubstitutionHint(HintType hintType, String sourceNsURI, String sourceTypeName,
			String targetNsURI, String targetTypeName) {
		this.hintType = hintType;
		this.sourceNsURI = sourceNsURI;
		this.sourceTypeName = sourceTypeName;
		this.targetNsURI = targetNsURI;
		this.targetTypeName = targetTypeName;
	}

	public HintType getHintType() {
		return hintType;
	}

	public String getSourceNsURI() {
		return sourceNsURI;
	}

	public String getSourceTypeName() {
		return sourceTypeName;
	}

	public String getTargetNsURI() {
		return targetNsURI;
	}

	public String getTargetTypeName() {
		return targetTypeName;
	}

	public String toString() {
		return "BasicXArchADTSubstitutionHint [hintType=" + hintType + ", sourceNsURI=" + sourceNsURI
				+ ", sourceTypeName=" + sourceTypeName + ", targetNsURI=" + targetNsURI + ", targetTypeName="
				+ targetTypeName + "]";
	}

}
