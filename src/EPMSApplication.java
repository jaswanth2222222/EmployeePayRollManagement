import dao.*;
import model.Employee;
import service.UIService;

import java.util.Scanner;

public class EPMSApplication {

	public static void main(String[] args) {
		UIService uiService = new UIService();
		// Calling performOperation() to perform the Tasks wanted to User
		uiService.performOperation();
		
		// Presenting Thank you to the User
		uiService.presentThankful();
	}

}