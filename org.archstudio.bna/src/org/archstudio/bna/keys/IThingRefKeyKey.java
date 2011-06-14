package org.archstudio.bna.keys;

import org.archstudio.bna.IThing;
import org.archstudio.bna.IThing.IThingKey;

public interface IThingRefKeyKey<T extends IThing, N, K extends IThingKey<?>> extends IThingRefKey<T> {

	N getName();

	K getKey();
}