ext-jdbcdslog
=============

Fork of jdbcdslog(http://code.google.com/p/jdbcdslog/) with more configuration options to control transactions and logged information.

##Newly added important features##

1. Skip calls to commit. This will work only if connection is not in auto commit mode.

2. Option to not print SELECT statements. This is useful to get all UPDATE/DELETE statements.

3. Option to print only DML statements by disabling other log messages like time taken, method name.

##Added additional configuration options##

1. jdbcdslog.noCommit - Skip calls to Connection.commit().

2. jdbcdslog.logSelect - Do not print SELECT statements.

3. jdbcdslog.logTime - Do not print time taken to execute SQL.

4. jdbcdslog.printMethodName - Do not print method name of calling method.

##TODO##
1. Add option to rollback connection on closing connection. Oracle commits connection on close when in non auto commit mode.

2. Call rollback on connection if in noCommit mode.