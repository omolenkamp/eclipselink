/*******************************************************************************
 * Copyright (c) 1998, 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/  
 package org.eclipse.persistence.testing.tests.jpa.performance;

import javax.persistence.*;
import org.eclipse.persistence.testing.models.performance.*;
import org.eclipse.persistence.testing.framework.*;

/**
 * This test compares the performance of read object Employee.
 */
public class JPAReadObjectCompletelyEmployeePerformanceComparisonTest extends PerformanceRegressionTestCase {
    protected long employeeId;

    public JPAReadObjectCompletelyEmployeePerformanceComparisonTest() {
        setDescription("This test compares the performance of read object Employee.");
    }

    /**
     * Get an employee id.
     */
    public void setup() {
        employeeId = ((Employee)getSession().readObject(org.eclipse.persistence.testing.models.performance.toplink.Employee.class)).getId();
    }

    /**
     * Read object.
     */
    public void test() throws Exception {
        EntityManager manager = createEntityManager();
        manager.getTransaction().begin();
        Employee employee = manager.getReference(Employee.class, new Long(this.employeeId));
        employee.getAddress().toString();
        String.valueOf(employee.getManager());
        employee.getManagedEmployees().size();
        employee.getPhoneNumbers().size();
        employee.getProjects().size();
        manager.getTransaction().commit();
        manager.close();
    }
}