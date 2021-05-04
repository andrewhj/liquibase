package liquibase.extension.testing.command

import liquibase.change.ColumnConfig
import liquibase.change.core.CreateTableChange
import liquibase.exception.CommandValidationException

import java.util.regex.Pattern

CommandTests.define {
    command = ["diffChangelog"]
    signature = """
Short Description: Compare two databases to produce changesets and write them to a changelog file
Long Description: NOT SET
Required Args:
  changelogFile (String) Changelog file to write results
  referenceUrl (String) The JDBC reference database connection URL
  url (String) The JDBC target database connection URL
Optional Args:
  password (String) The target database password
    Default: null
  referencePassword (String) The reference database password
    Default: null
  referenceUsername (String) The reference database username
    Default: null
  username (String) The target database username
    Default: null
"""

    run "Running diffChangelog against itself finds no differences", {
        arguments = [
                url              : { it.url },
                username         : { it.username },
                password         : { it.password },
                referenceUrl     : { it.url },
                referenceUsername: { it.username },
                referencePassword: { it.password },
                changelogFile: "target/test-classes/diffChangelog-test.xml"
        ]

        setup {
            cleanResources("diffChangelog-test.xml")
            database = [
                    new CreateTableChange(
                            tableName: "FirstTable",
                            columns: [
                                    ColumnConfig.fromName("FirstColumn")
                                            .setType("VARCHAR(255)")
                            ]
                    ),
                    new CreateTableChange(
                            tableName: "SecondTable",
                            columns: [
                                    ColumnConfig.fromName("SecondColumn")
                                            .setType("VARCHAR(255)")
                            ]
                    ),
            ]

        }

        expectedFileContent = [
                //
                // Empty changelog contains no changeSet tags and an empty databaseChangeLog tag
                //
                "target/test-classes/diffChangeLog-test.xml" : [CommandTests.assertNotContains("<changeSet"),
                                                                Pattern.compile("^.*<?xml.*databaseChangeLog.*xsd./>", Pattern.MULTILINE|Pattern.DOTALL)]
        ]

        expectedOutput = [
                """
"""
        ]

        expectedUI = "Liquibase command 'diffChangelog' was executed successfully"
    }

    run "Running diffChangelog should add change sets", {
        arguments = [
                url              : { it.url },
                username         : { it.username },
                password         : { it.password },
                referenceUrl     : { it.altUrl },
                referenceUsername: { it.altUsername },
                referencePassword: { it.altPassword },
                changelogFile: "target/test-classes/diffChangeLog-test.xml"
        ]

        setup {
            cleanResources("diffChangeLog-test.xml")
            database = [
                    new CreateTableChange(
                            tableName: "SharedTable",
                            columns: [
                                    ColumnConfig.fromName("Id")
                                            .setType("VARCHAR(255)"),
                                    ColumnConfig.fromName("Shared")
                                            .setType("VARCHAR(255)")
                            ]
                    ),
                    new CreateTableChange(
                            tableName: "PrimaryTable",
                            columns: [
                                    ColumnConfig.fromName("Id")
                                            .setType("VARCHAR(255)")
                            ]
                    ),
            ]

            altDatabase = [
                    new CreateTableChange(
                            tableName: "SharedTable",
                            columns: [
                                    ColumnConfig.fromName("Name")
                                            .setType("VARCHAR(255)"),
                                    ColumnConfig.fromName("Shared")
                                            .setType("VARCHAR(3)")
                            ]
                    ),
                    new CreateTableChange(
                            tableName: "SecondaryTable",
                            columns: [
                                    ColumnConfig.fromName("Id")
                                            .setType("VARCHAR(255)")
                            ]
                    ),
            ]

        }
        expectedFileContent = [
                "target/test-classes/diffChangeLog-test.xml" : [CommandTests.assertContains("<changeSet ", 5),
                                                                CommandTests.assertContains("<dropTable ", 1)]
        ]
        expectedOutput = [
                """
"""
        ]
        expectedUI = "Liquibase command 'diffChangelog' was executed successfully"
    }

    run "Running diff against differently structured databases finds changed objects", {
        arguments = [
                url              : { it.url },
                username         : { it.username },
                password         : { it.password },
                referenceUrl     : { it.altUrl },
                referenceUsername: { it.altUsername },
                referencePassword: { it.altPassword },
                changelogFile: "target/test-classes/diffChangeLog-test.xml"
        ]

        setup {
            cleanResources("diffChangeLog-test.xml")
            database = [
                    new CreateTableChange(
                            tableName: "SharedTable",
                            columns: [
                                    ColumnConfig.fromName("Id")
                                            .setType("VARCHAR(255)"),
                                    ColumnConfig.fromName("Shared")
                                            .setType("VARCHAR(255)")
                            ]
                    ),
                    new CreateTableChange(
                            tableName: "PrimaryTable",
                            columns: [
                                    ColumnConfig.fromName("Id")
                                            .setType("VARCHAR(255)")
                            ]
                    ),
            ]

            altDatabase = [
                    new CreateTableChange(
                            tableName: "SharedTable",
                            columns: [
                                    ColumnConfig.fromName("Name")
                                            .setType("VARCHAR(255)"),
                                    ColumnConfig.fromName("Shared")
                                            .setType("VARCHAR(3)")
                            ]
                    ),
                    new CreateTableChange(
                            tableName: "SecondaryTable",
                            columns: [
                                    ColumnConfig.fromName("Id")
                                            .setType("VARCHAR(255)")
                            ]
                    ),
            ]

        }
        expectedUI = "Liquibase command 'diffChangelog' was executed successfully"
    }

    run "Running diffChangelog without changelogFile gives an error", {
        arguments = [
                url              : { it.url },
                username         : { it.username },
                password         : { it.password },
                referenceUrl     : { it.altUrl },
                referenceUsername: { it.altUsername },
                referencePassword: { it.altPassword },
        ]

        setup {
            cleanResources("diffChangeLog-test.xml")
            database = [
                    new CreateTableChange(
                            tableName: "SharedTable",
                            columns: [
                                    ColumnConfig.fromName("Id")
                                            .setType("VARCHAR(255)"),
                                    ColumnConfig.fromName("Shared")
                                            .setType("VARCHAR(255)")
                            ]
                    ),
                    new CreateTableChange(
                            tableName: "PrimaryTable",
                            columns: [
                                    ColumnConfig.fromName("Id")
                                            .setType("VARCHAR(255)")
                            ]
                    ),
            ]

            altDatabase = [
                    new CreateTableChange(
                            tableName: "SharedTable",
                            columns: [
                                    ColumnConfig.fromName("Name")
                                            .setType("VARCHAR(255)"),
                                    ColumnConfig.fromName("Shared")
                                            .setType("VARCHAR(3)")
                            ]
                    ),
                    new CreateTableChange(
                            tableName: "SecondaryTable",
                            columns: [
                                    ColumnConfig.fromName("Id")
                                            .setType("VARCHAR(255)")
                            ]
                    ),
            ]

        }
        expectedException = CommandValidationException.class
        expectedOutput = [
                """
"""
        ]
    }
}