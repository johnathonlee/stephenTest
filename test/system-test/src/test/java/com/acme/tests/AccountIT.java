package com.acme.tests;

import com.acme.model.Task;
import com.acme.service.AccountService;
import com.acme.service.TaskGeneratorService;
import com.acme.service.TaskService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NamingException;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;

/**
 * User: bven
 * Date: 9/8/16.
 */
public class AccountIT extends AbstractIntegrationTest{

    @Test
    public void test(){
        AccountService service = getBean("ejb:acme-ear/ejb/AccountBean!" + AccountService.class.getName());
        TaskGeneratorService taskService = getBean("ejb:acme-ear/ejb/TaskGeneratorBean!" + TaskGeneratorService.class.getName());
        taskService.deleteTasks();

        Long id = service.createAccount("test");

        for (int i = 0; i<1; i++){
            service.updateAccount(id, Integer.toString(i));
        }

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Task> result = taskService.getNotUpdatedTasks();
        assertTrue(result.isEmpty());

    }


}
