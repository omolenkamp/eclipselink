/*
 * Copyright (c) 1998, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     Oracle - initial API and implementation from Oracle TopLink
package org.eclipse.persistence.testing.models.employee.interfaces;

import java.math.BigDecimal;
import java.io.Serializable;

/**
 * <b>Purpose</b>:         Abstract superclass for Large &amp; Small projects in Employee Demo
 *    <p><b>Description</b>:     Project is an example of an abstract superclass. It demonstrates how class inheritance can be mapped to database tables.
 * It's subclasses are concrete and may or may not add columns through additional tables. The PROJ_TYPE field in the
 * database table indicates which subclass to instantiate. Projects are involved in a M:M relationship with employees.
 * The Employee classs maintains the definition of the relation table.
 *
 *    @see LargeProject
 *    @see SmallProject
 */
public abstract interface Project extends Serializable {
    String getDescription();

    BigDecimal getId();

    String getName();

    org.eclipse.persistence.testing.models.employee.interfaces.Employee getTeamLeader();

    void setDescription(String description);

    void setName(String name);

    void setTeamLeader(org.eclipse.persistence.testing.models.employee.interfaces.Employee teamLeader);
}
