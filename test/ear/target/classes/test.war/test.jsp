<%@page import="com.acme.tests.AccountIT"%>
<html>
<head>
<title>AccountBean Test</title>
</head>
<body>
<h1>AccountBean Test:  no output expected,  check logs</h1>
<%

    AccountIT thing = new AccountIT();
    thing.test();
%>
</body>
</html>