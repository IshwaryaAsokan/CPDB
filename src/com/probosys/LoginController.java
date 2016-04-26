package com.probosys;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean
public class LoginController {

	private String username;

	private String password;

	public void login(ActionEvent event) {
		FacesMessage message = null;
		String result = "admin/login.xhtml";
		try {
			if (username != null && username.equals("admin") && password != null && password.equals("admin")) {
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
				result = "/admin/fileUpload.xhtml";
				ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
				context.redirect(context.getRequestContextPath() + result);
//				FacesContext.getCurrentInstance().getExternalContext().dispatch("fileUpload.xhtml");
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
			}
		} catch (IOException e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Loggin Error", "ServerError");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}