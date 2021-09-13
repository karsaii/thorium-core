package com.neathorium.core.extensions.interfaces;

public interface IEmptiable extends ISizable {
    boolean isEmpty();
    boolean isNullOrEmpty();
    boolean isNotNullAndNonEmpty();
}
