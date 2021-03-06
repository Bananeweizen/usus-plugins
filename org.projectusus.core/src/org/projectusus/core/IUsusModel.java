// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core;

import java.util.List;

import org.projectusus.core.basis.CodeProportion;

public interface IUsusModel {

    List<CodeProportion> getCodeProportions();

    void refreshCodeProportions();

    void addUsusModelListener( IUsusModelListener listener );

    void removeUsusModelListener( IUsusModelListener listener );

    boolean needsFullRecompute();
}
