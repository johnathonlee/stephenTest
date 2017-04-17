<%@page import="java.util.Properties,javax.naming.*,com.acme.service.*,com.acme.tests.*,java.util.List,com.acme.model.*"%>
<html>
<head>
<title>AccountBean Test</title>
</head>
<body>
<h1>AccountBean Test:  no output expected,  check logs</h1>
<%

Properties prop = new Properties();
Context ct = new InitialContext();

AccountService service = null;
TaskGeneratorService taskService = null;

try {

  service = (AccountService) ct.lookup("ejb:acme-ear/ejb/AccountBean!" + AccountService.class.getName());
  taskService = (TaskGeneratorService) ct.lookup("ejb:acme-ear/ejb/TaskGeneratorBean!" + TaskGeneratorService.class.getName());
  taskService.deleteTasks();

} catch (Exception e){
	e.printStackTrace();
}

Long id = service.createAccount("test");

for (int i = 0; i<1; i++){
    service.updateAccount(id, Integer.toString(i));
}

try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    e.printStackTrace();
}

List<Task> result = taskService.getNotUpdatedTasks();
System.out.println("The result is empty: " + result.isEmpty());
%>
</body>
</html>