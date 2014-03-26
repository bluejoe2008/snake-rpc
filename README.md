snake
=====

An RPC framework based on Hessian(http://hessian.caucho.com)

the name 'snake' comes from the Animal Year of snake in Chinese(the year 2013).

snake has two new features:

* ablity to serialize InputStream/OutputStream;
* ablity to reuse remote objects returned by a method call;

for example:

    Connection con = client.getConnection();
    ResultSet rs = con.queryForResultSet("select * from users");

this feature is very useful for those objects which are valid only on the server-side, e.g, the Connection or ResultSet objects;
