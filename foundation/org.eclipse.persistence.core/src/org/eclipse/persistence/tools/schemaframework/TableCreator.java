/*******************************************************************************
 * Copyright (c) 1998, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/  
package org.eclipse.persistence.tools.schemaframework;

import java.util.*;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.sequencing.Sequence;
import org.eclipse.persistence.sequencing.TableSequence;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.Session;

/**
 * <b>Purpose</b>: This class is responsible for creating the tables defined in the project.
 * A specific subclass of this class is created for each project.  The specific table information
 * is defined in the subclass.
 *
 * @since TopLink 2.0
 * @author Peter Krogh
 */
public class TableCreator {
    protected List<TableDefinition> tableDefinitions;
    protected String name;
    protected boolean ignoreDatabaseException; //if true, DDL generation will continue even if exceptions occur

    public TableCreator() {
        this(new ArrayList<TableDefinition>());
    }

    public TableCreator(List<TableDefinition> tableDefinitions) {
        super();
        this.tableDefinitions = tableDefinitions;
    }

    /**
     * Add the table.
     */
    public void addTableDefinition(TableDefinition tableDefinition) {
        this.tableDefinitions.add(tableDefinition);
    }
    
    /**
     * Add a set of tables.
     */
    public void addTableDefinitions(Collection<TableDefinition> tableDefs) {
        this.tableDefinitions.addAll(tableDefs);
    }


    /**
     * Create constraints.
     */
    public void createConstraints(DatabaseSession session) {
        //CR2612669
        createConstraints(session, new SchemaManager(session));
    }

    /**
     * Create constraints.
     */
    public void createConstraints(DatabaseSession session, SchemaManager schemaManager) {
        createConstraints(session, schemaManager, true);
    }

    /**
     * Create constraints.
     */
    public void createConstraints(DatabaseSession session, SchemaManager schemaManager, boolean build) {
        buildConstraints(schemaManager, build);

        // Unique constraints should be generated before foreign key constraints,
        // because foreign key constraints can reference unique constraints
        for (TableDefinition table : getTableDefinitions()) {
            try {
                schemaManager.createUniqueConstraints(table);
            } catch (DatabaseException ex) {
                if (!shouldIgnoreDatabaseException()) {
                    throw ex;
                }
            }
        }
        
        for (TableDefinition table : getTableDefinitions()) {
            try {
                schemaManager.createForeignConstraints(table);
            } catch (DatabaseException ex) {
                if (!shouldIgnoreDatabaseException()) {
                    throw ex;
                }
            }
        }
    }

    /**
     * This creates the tables on the database.
     * If the table already exists this will fail.
     */
    public void createTables(org.eclipse.persistence.sessions.DatabaseSession session) {
        //CR2612669
        createTables(session, new SchemaManager(session));
    }

    /**
     * This creates the tables on the database.
     * If the table already exists this will fail.
     */
    public void createTables(DatabaseSession session, SchemaManager schemaManager) {
        createTables(session, schemaManager, true);
    }

    /**
     * This creates the tables on the database.
     * If the table already exists this will fail.
     */
    public void createTables(DatabaseSession session, SchemaManager schemaManager, boolean build) {
        buildConstraints(schemaManager, build);

        String sequenceTableName = getSequenceTableName(session);
        for (TableDefinition table : getTableDefinitions()) {
            // Must not create sequence table as done in createSequences.
            if (!table.getName().equals(sequenceTableName)) {
                try {
                    schemaManager.createObject(table);
                    session.getSessionLog().log(SessionLog.FINEST, "default_tables_created", table.getFullName());
                } catch (DatabaseException ex) {
                    session.getSessionLog().log(SessionLog.FINEST, "default_tables_already_existed", table.getFullName());
                    if (!shouldIgnoreDatabaseException()) {
                        throw ex;
                    }
                }
            }
        }
        
        createConstraints(session, schemaManager, false);

        schemaManager.createSequences();
    }

    /**
     * Drop the table constraints from the database.
     */
    public void dropConstraints(DatabaseSession session) {
        //CR2612669
        dropConstraints(session, new SchemaManager(session));
    }

    /**
     * Drop the table constraints from the database.
     */
    public void dropConstraints(DatabaseSession session, SchemaManager schemaManager) {
        dropConstraints(session, schemaManager, true);
    }

    /**
     * Drop the table constraints from the database.
     */
    public void dropConstraints(DatabaseSession session, SchemaManager schemaManager, boolean build) {
        buildConstraints(schemaManager, build);

        for (TableDefinition table : getTableDefinitions()) {
            try {
                schemaManager.dropConstraints(table);
            } catch (DatabaseException exception) {
                //ignore
            }
        }
    }

    /**
     * Drop the tables from the database.
     */
    public void dropTables(DatabaseSession session) {
        //CR2612669
        dropTables(session, new SchemaManager(session));
    }

    /**
     * Drop the tables from the database.
     */
    public void dropTables(DatabaseSession session, SchemaManager schemaManager) {
        dropTables(session, schemaManager, true);
    }

    /**
     * Drop the tables from the database.
     */
    public void dropTables(DatabaseSession session, SchemaManager schemaManager, boolean build) {
        buildConstraints(schemaManager, build);

        // CR 3870467, do not log stack, or log at all if not fine
        boolean shouldLogExceptionStackTrace = session.getSessionLog().shouldLogExceptionStackTrace();
        int level = session.getSessionLog().getLevel();
        if (shouldLogExceptionStackTrace) {
            session.getSessionLog().setShouldLogExceptionStackTrace(false);
        }
        if (level > SessionLog.FINE) {
            session.getSessionLog().setLevel(SessionLog.SEVERE);
        }
        try {
            dropConstraints(session, schemaManager, false);

            String sequenceTableName = getSequenceTableName(session);
            List<TableDefinition> tables = getTableDefinitions();
            int trys = 1;
            if (SchemaManager.FORCE_DROP) {
                trys = 5;
            }
            while ((trys > 0) && !tables.isEmpty()) {
                trys--;
                List<TableDefinition> failed = new ArrayList<TableDefinition>();
                for (TableDefinition table : tables) {
                    // Must not create sequence table as done in createSequences.
                    if (!table.getName().equals(sequenceTableName)) {
                        try {
                            schemaManager.dropObject(table);
                        } catch (DatabaseException exception) {
                            failed.add(table);
                            if (!shouldIgnoreDatabaseException()) {
                                throw exception;
                            }
                        }
                    }
                }
                tables = failed;
            }
        } finally {
            if (shouldLogExceptionStackTrace) {
                session.getSessionLog().setShouldLogExceptionStackTrace(true);
            }
            if (level > SessionLog.FINE) {
                session.getSessionLog().setLevel(level);
            }
        }
    }

    /**
     * Return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Return the tables.
     */
    public List<TableDefinition> getTableDefinitions() {
        return tableDefinitions;
    }

    /**
     * Recreate the tables on the database.
     * This will drop the tables if they exist and recreate them.
     */
    public void replaceTables(DatabaseSession session) {
        replaceTables(session, new SchemaManager(session));
    }

    /**
     * Recreate the tables on the database.
     * This will drop the tables if they exist and recreate them.
     */
    public void replaceTables(DatabaseSession session, SchemaManager schemaManager) {
        replaceTablesAndConstraints(schemaManager, session);
        schemaManager.createSequences();
    }
    
    /**
     * Recreate the tables on the database.
     * This will drop the tables if they exist and recreate them.
     */
    public void replaceTables(DatabaseSession session, SchemaManager schemaManager, boolean keepSequenceTable) {
        replaceTablesAndConstraints(schemaManager, session);
        schemaManager.createOrReplaceSequences(keepSequenceTable, false);
    }
    
    protected void replaceTablesAndConstraints(SchemaManager schemaManager, DatabaseSession session) {
        buildConstraints(schemaManager, true);
        boolean ignore = shouldIgnoreDatabaseException();
        setIgnoreDatabaseException(true);
        try {
            dropTables(session, schemaManager, false);
        } finally {
            setIgnoreDatabaseException(ignore);            
        }
        createTables(session, schemaManager, false);
    }
    
    /**
     * Convert any field constraint to constraint objects.
     */
    protected void buildConstraints(SchemaManager schemaManager, boolean build) {
        if (build) {
            for (TableDefinition table : getTableDefinitions()) {
                schemaManager.buildFieldTypes(table);
            }
        }
    }

    /**
     * Set the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the tables.
     */
    public void setTableDefinitions(Vector tableDefinitions) {
        this.tableDefinitions = tableDefinitions;
    }

    /**
     * Return true if DatabaseException is to be ignored.
     */
    protected boolean shouldIgnoreDatabaseException() {
        return ignoreDatabaseException;
    }

    /**
     * Set flag whether DatabaseException should be ignored. 
     */
    protected void setIgnoreDatabaseException(boolean ignoreDatabaseException) {
        this.ignoreDatabaseException = ignoreDatabaseException;
    }

    protected String getSequenceTableName(Session session) {
        String sequenceTableName = null;
        if (session.getProject().usesSequencing()) {
            Sequence sequence = session.getLogin().getDefaultSequence();
            if (sequence instanceof TableSequence) {
                sequenceTableName = ((TableSequence)sequence).getTableName();
            }
        }
        return sequenceTableName;
    }
}
