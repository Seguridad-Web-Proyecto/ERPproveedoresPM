/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import dao.UsuarioJpaController;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;
import jsf.clases.util.JsfUtil;

/**
 *
 * @author jaker
 */
@Named(value = "login")
@SessionScoped
public class login implements Serializable
{

    private String usuario, contrasenia;
    private String errorMessage;
    private int errorsCounter;

    public String getUsuario()
    {
        return usuario;
    }

    public void setUsuario(String usuario)
    {
        this.usuario = usuario;
    }

    public String getContrasenia()
    {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia)
    {
        this.contrasenia = contrasenia;
    }

    public String login3()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try
        {
            request.login(usuario, contrasenia);
        } catch (ServletException ex)
        {
            System.out.println(ex.getMessage());
            if (ex.getMessage().contains("Login failed"))
            {
                errorMessage = "login.failed";
            }
            return "/faces/login.xhtml";
        }
        Principal user = request.getUserPrincipal();
//    setPersons(new UsersJpaController(utx,emf).findUsers(usuario));
//    context.getExternalContext().getSessionMap().put("persons", persons);

        if (request.isUserInRole("ADMINS"))
        {
//            return "/secured/admin/menu.xhtml";
//        } else if (request.isUserInRole("COMPRAS"))
//        {
//            return "/secured/compras/menu.xhtml";
//        
//        } else if (request.isUserInRole("USERS"))
//        {
            return "/secured/admin/menu.xhtml";
        } else
        {
            return "/secured/user/menu.xhtml";
        }
    }

    public boolean isRolCompras()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.isUserInRole("COMPRAS") || request.isUserInRole("ADMINS"))
        {
            return true;
        } else
        {
            return false;
        }

    }

    public boolean isRoUsers()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.isUserInRole("USERS") || request.isUserInRole("ADMINS"))
        {
            return true;
        } else
        {
            return false;
        }

    }

    public boolean isRolAdmin()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        return request.isUserInRole("ADMINS");
    }

    public boolean isRolVentas()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.isUserInRole("VENTAS") || request.isUserInRole("ADMINS"))
        {
            return true;
        } else
        {
            return false;
        }

    }

    public boolean isRolRh()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        if (request.isUserInRole("RH") || request.isUserInRole("ADMINS"))
        {
            return true;
        } else
        {
            return false;
        }

    }

    public boolean isRolInventario()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        if (request.isUserInRole("INVENTARIO") || request.isUserInRole("ADMINS"))
        {
            return true;
        } else
        {
            return false;
        }

    }

    public String logout()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try
        {
            externalContext.invalidateSession();
            request.logout();
        } catch (ServletException ex)
        {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }

        errorMessage = "";
        errorsCounter = 0;
        return "/login.xhtml?faces-redirect=true";
    }

}
