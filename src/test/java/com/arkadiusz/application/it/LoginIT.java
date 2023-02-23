package com.arkadiusz.application.it;

import com.vaadin.flow.component.login.testbench.LoginFormElement;
import org.junit.Assert;
import org.junit.Test;

public class LoginIT extends AbstractIT {


    @Test
    public void loginAsValidUserSucceeds() {

        getDriver().get("http://localhost:8080");
        LoginFormElement form = $(LoginFormElement.class).first();

        form.getUsernameField().setValue("admin");
        form.getPasswordField().setValue("admin");
        form.getSubmitButton().click();

        Assert.assertFalse($(LoginFormElement.class).exists());
    }

    @Test
    public void loginAsInvalidUserFails() {
        getDriver().get("http://localhost:8080");
        LoginFormElement form = $(LoginFormElement.class).first();
        form.getUsernameField().setValue("admin");
        form.getPasswordField().setValue("blednehaslo");
        form.getSubmitButton().click();

        Assert.assertTrue($(LoginFormElement.class).exists());
    }
}