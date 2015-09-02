package liquibase.actionlogic.core;

import liquibase.Scope;
import liquibase.action.Action;
import liquibase.action.core.SnapshotDatabaseObjectsAction;
import liquibase.actionlogic.ActionLogic;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.structure.DatabaseObject;
import liquibase.structure.ObjectName;
import liquibase.structure.ObjectReference;
import liquibase.structure.core.Catalog;
import liquibase.structure.core.Column;
import liquibase.structure.core.Relation;
import liquibase.structure.core.Schema;
import liquibase.util.CollectionUtil;

public class SnapshotColumnsLogicOffline extends AbstractSnapshotDatabaseObjectsLogicOffline implements ActionLogic.InteractsExternally {

    @Override
    protected Class<? extends DatabaseObject> getTypeToSnapshot() {
        return Column.class;
    }

    @Override
    protected Class<? extends DatabaseObject>[] getSupportedRelatedTypes() {
        return new Class[]{
                Column.class,
                Relation.class,
                Schema.class,
                Catalog.class
        };
    }

    @Override
    public boolean interactsExternally(Action action, Scope scope) {
        return true;
    }

    protected CollectionUtil.CollectionFilter<? extends DatabaseObject> getDatabaseObjectFilter(SnapshotDatabaseObjectsAction action, Scope scope) {
        final DatabaseObject relatedTo = action.relatedTo;

        return new CollectionUtil.CollectionFilter<Column>() {
            @Override
            public boolean include(Column column) {
                if (relatedTo instanceof Column) {
                    return column.getName().equals(relatedTo.getName());
                } else if (relatedTo instanceof Relation) {
                    ObjectName tableName = column.getName().container;
                    return tableName != null && tableName.equals((relatedTo).getName());
                } else if (relatedTo instanceof Schema) {
                    ObjectName tableName = column.getName().container;
                    return tableName != null && tableName.container != null && tableName.container.equals((relatedTo.getName()));
                } else if (relatedTo instanceof Catalog) {
                    ObjectName tableName = column.getName().container;
                    if (tableName == null || tableName.container == null) {
                        return false;
                    }
                    ObjectName schemaName = tableName.container;

                    return schemaName.container != null && schemaName.container.equals((relatedTo.getName()));
                } else {
                    throw new UnexpectedLiquibaseException("Unexpected relatedTo type: "+relatedTo.getClass().getName());
                }
            }
        };

    }
}